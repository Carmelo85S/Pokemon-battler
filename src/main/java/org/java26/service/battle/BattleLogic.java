package org.java26.service.battle;

import org.java26.handlers.JsonHandler;
import org.java26.models.Attack;
import org.java26.models.Pokedex;
import org.java26.models.Pokemon;
import org.java26.models.PokemonType;

import java.util.Random;
import java.util.Scanner;

import static org.java26.UI.BattleUI.chooseMyPokemon;
import static org.java26.UI.BattleUI.showOptions;
import static org.java26.UI.Layout.*;
import static org.java26.UI.Layout.menuOption;
import static org.java26.UI.Layout.subTitle;
import static org.java26.inputHelpers.InputHelper.readIntBetween;
import static org.java26.service.AttackCreator.createAttack;
import static org.java26.service.PokemonViewer.showPokemons;
import static org.java26.service.StatisticsService.*;

public class BattleLogic {

    public static Pokemon preparePlayerPokemon(Scanner scanner, Pokedex pokedex) {
        if (pokedex.getPokemons().isEmpty()) {
            showOptions(scanner, pokedex);

            if (pokedex.getPokemons().isEmpty()) {
                return null;
            }
        }

        title("Choose your pokemon");
        showPokemons(pokedex);

        Pokemon myPokemon = chooseMyPokemon(scanner, pokedex);
        hasPokemonAttack(scanner, myPokemon);

        return myPokemon;
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

    public static void evolvePokemon(Pokemon pokemon, BattleStatistics statistics) {
        int wins = statistics.getPokemonWins().getOrDefault(pokemon.getName(), 0);

        if (wins > 0 && wins % 3 == 0) {
            pokemon.evolve();
        }
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

    public static void hasPokemonAttack(Scanner scanner, Pokemon pokemon) {
        if (pokemon.getAttacks().isEmpty()) {
            System.out.println("  No attacks available.");
            title("Create attack");
            menuOption(1, "Yes");
            menuOption(2, "No");
            separator();
            int choice = readIntBetween(scanner, 1, 2, "  Select an option: >");
            switch (choice) {
                case 1 -> {
                    Attack attack = createAttack(scanner, pokemon.getType());
                    pokemon.addAttack(attack);
                }
                case 2 -> {
                    return;
                }
            }
        }
    }

}
