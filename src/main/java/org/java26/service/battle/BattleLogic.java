package org.java26.service.battle;

import org.java26.repository.JsonHandler;
import org.java26.models.Attack;
import org.java26.models.Pokedex;
import org.java26.models.Pokemon;
import org.java26.models.PokemonType;

import java.util.Random;
import java.util.Scanner;

import static org.java26.UI.BattleUI.*;
import static org.java26.UI.Layout.*;
import static org.java26.UI.Layout.menuOption;
import static org.java26.UI.Layout.subTitle;
import static org.java26.handlers.InputHelper.readIntBetween;
import static org.java26.service.AttackCreator.createAttack;
import static org.java26.service.PokemonViewer.showPokemons;
import static org.java26.service.StatisticsService.*;

public class BattleLogic {

    public static Pokemon preparePlayerPokemon(Scanner scanner, Pokedex pokedex, BattleStatistics statistics) {
        if (pokedex.getPokemons().isEmpty()) {
            showOptions(scanner, pokedex);

            if (pokedex.getPokemons().isEmpty()) {
                return null;
            }
        }

        title("Choose your pokemon");
        showPokemons(pokedex);

        boolean hasPlayablePokemon = pokedex.getPokemons()
                .stream()
                .anyMatch(Pokemon::isPlayable);

        if (!hasPlayablePokemon) {
            title("No playable Pokemon available.");
            subTitle("Create or edit your available Pokemons");
            return null;
        }

        Pokemon myPokemon = chooseMyPokemon(scanner, pokedex);
        statistics.trackPokemon(myPokemon.getName());

        return myPokemon;
    }

    public static boolean runBattle(
            Scanner scanner,
            Pokemon myPokemon,
            Pokemon opponent,
            Random random,
            BattleStatistics statistics,
            int playerAccuracyPenalty,
            int opponentAccuracyPenalty
    ) {
        int round = 1;

        if(myPokemon.isFainted() || opponent.isFainted()){
            System.out.println("  Cannot start battle: Pokemon has 0 HP.");
            return false;
        }

        if (myPokemon.getAttacks() == null ||
                opponent.getAttacks() == null ||
                myPokemon.getAttacks().isEmpty() ||
                opponent.getAttacks().isEmpty()) {

            System.out.println("  Cannot start battle: Pokemon has no attacks.");
            return false;
        }

        boolean playerTurn = isTurn(myPokemon, opponent, random);

        title("---Random Start---");

        while (!myPokemon.isFainted() && !opponent.isFainted()) {

            title("ROUND " + round);

            if (playerTurn) {
                subTitle(myPokemon.getName() + " turn.");

                Attack selectedPlayerAttack = chooseAttack(scanner, myPokemon);

                runTurn(
                        selectedPlayerAttack,
                        myPokemon,
                        opponent,
                        random,
                        playerAccuracyPenalty,
                        statistics
                );

            } else {
                subTitle(opponent.getName() + " turn.");

                Attack opponentAttack =
                        chooseRandomAttack(opponent, random);

                runTurn(
                        opponentAttack,
                        opponent,
                        myPokemon,
                        random,
                        opponentAccuracyPenalty,
                        statistics
                );
            }

            round++;
            playerTurn = !playerTurn;
        }
        return true;
    }

    public static void handleEndFight(String prompt, Pokemon myPokemon, Pokemon opponent, boolean won, BattleStatistics statistics, JsonHandler jsonHandler) {

        separator();
        System.out.println("  " + myPokemon.getName() + prompt);
        recordPokemonUse(statistics, myPokemon);

        if (won) {
            recordWin(statistics, myPokemon);
            recordLoss(statistics, opponent);
        } else {
            recordWin(statistics, opponent);
            recordLoss(statistics, myPokemon);
        }
        myPokemon.fullHeal();
        opponent.fullHeal();
        jsonHandler.saveStatistics("statistic.json", statistics);
        System.out.println("  Statistic saved!");
    }

    public static void handleVictory(
            Scanner scanner,
            Pokedex pokedex,
            Pokemon caught,
            BattleStatistics statistics,
            Runnable playAgain
    ) {

        subTitle("Gotta catch them all!");
        menuOption(1, "Yes");
        menuOption(2, "No");

        int choice = readIntBetween(
                scanner,
                1,
                2,
                "  Select an option: > "
        );

        separator();

        if (choice == 1) {
            recordPokemonCatch(statistics, caught);
            pokedex.addPokemon(caught);
        }

        subTitle("Play again");
        menuOption(1, "Yes");
        menuOption(2, "Exit");

        separator();

        choice = readIntBetween(
                scanner,
                1,
                2,
                "  Select an option: > "
        );

        separator();

        if (choice == 1) {
            playAgain.run();
            return;
        }

        System.out.println("  Returning to menu");
    }

    public static double calculateEffectiveness(Pokemon defender, Attack attack) {
        return calculateEffectiveness(
                attack.getType(),
                defender.getType()
        );
    }

    public static double calculateEffectiveness(
            PokemonType attackType,
            PokemonType defenderType
    ) {
        if (attackType == PokemonType.FIRE && defenderType == PokemonType.GRASS) {
            return 2.0;
        }

        if (attackType == PokemonType.WATER && defenderType == PokemonType.FIRE) {
            return 2.0;
        }

        if (attackType == PokemonType.GRASS && defenderType == PokemonType.WATER) {
            return 2.0;
        }

        if (attackType == PokemonType.ELECTRIC && defenderType == PokemonType.WATER) {
            return 2.0;
        }

        if (attackType == PokemonType.FIRE && defenderType == PokemonType.WATER) {
            return 0.5;
        }

        if (attackType == PokemonType.WATER && defenderType == PokemonType.GRASS) {
            return 0.5;
        }

        if (attackType == PokemonType.GRASS && defenderType == PokemonType.FIRE) {
            return 0.5;
        }

        if (attackType == PokemonType.ELECTRIC && defenderType == PokemonType.GRASS) {
            return 0.5;
        }

        return 1.0;
    }

    static Attack chooseRandomAttack(Pokemon pokemon, Random random) {
        int randomAttack = random.nextInt(pokemon.getAttacks().size());
        return pokemon.getAttacks().get(randomAttack);
    }

    public static boolean evolvePokemon(
            Pokemon pokemon,
            BattleStatistics statistics
    ) {
        int wins = statistics.getPokemonWins()
                .getOrDefault(pokemon.getName(), 0);

        if (wins > 0 && wins % 3 == 0) {
            pokemon.evolve();
            return true;
        }
        return false;
    }

    public static void runTurn(Attack attack, Pokemon attacker, Pokemon defender, Random random, int accuracyPenalty, BattleStatistics statistics) {
        recordPokemonAttackUse(statistics, attacker, attack);

        int hitChance = random.nextInt(100) + 1;
        int accuracy = Math.clamp(attack.getAccuracy() - accuracyPenalty, 0, 100);
        boolean isHit = hitChance <= accuracy;
        if (!isHit) {
            System.out.println("  " + attacker.getName() + " used " + attack.getName() + " but missed!");
            separator();
        } else {
            boolean criticalHit = random.nextInt(15) == 0;
            double effectiveness = calculateEffectiveness(defender, attack);

            attack.execute(attacker, defender, criticalHit, effectiveness, random);
            System.out.println("  " + attacker.getName() + " used " + attack.getName() + "!");

            System.out.println("  " + defender.getName() + " HP: " + defender.getCurrentHp() + "/" + defender.getMaxHp());

            separator();
        }
    }

    public static boolean hasPokemonAttack(Scanner scanner, Pokemon pokemon) {
        if (!pokemon.getAttacks().isEmpty()) {
            return true;
        }

        System.out.println("  NO attacks available.");

        title("Create attack");
        menuOption(1, "Yes");
        menuOption(2, "No");
        separator();

        int choice = readIntBetween(scanner, 1, 2, "  Select an option: >");
        if (choice == 2) {
            System.out.println("  You will be redirected to battle menu.");
            return false;
        }

        Attack attack = createAttack(scanner, pokemon.getType());
        if (attack == null) {
            return false;
        }
        pokemon.addAttack(attack);
        return true;

    }


    public static boolean isTurn(Pokemon player, Pokemon opponent, Random random) {
        int playerStart = player.getSpeed() + random.nextInt(50);
        int opponentStart = opponent.getSpeed() + random.nextInt(50);

        return playerStart > opponentStart;
    }

}
