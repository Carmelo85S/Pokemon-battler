package org.java26.service.battle;

import org.java26.handlers.JsonHandler;
import org.java26.models.Attack;
import org.java26.models.Pokedex;
import org.java26.models.Pokemon;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static org.java26.consoleLayout.BattleUI.*;
import static org.java26.consoleLayout.Layout.*;
import static org.java26.inputHelpers.InputHelper.readIntBetween;
import static org.java26.service.PokemonViewer.showPokemons;
import static org.java26.service.battle.BattleLogic.*;

public class RegularBattle {
    public static void startBattle(Scanner scanner, Pokedex pokedex, List<Pokemon> wildPokemons, BattleStatistics statistics, JsonHandler jsonHandler) {
        title("Regular Battle");

        int round = 1;

        Random random = new Random();

        if (wildPokemons.isEmpty()) {
            title("No wild pokemons available");
            return;
        }

        if (pokedex.getPokemons().isEmpty()) {
            showOptions(scanner, pokedex);

            if (pokedex.getPokemons().isEmpty()) {
                return;
            }
        }

        title("Choose your pokemon");
        showPokemons(pokedex);

        Pokemon myPokemon = chooseMyPokemon(scanner, pokedex);

        Pokemon opponent = chooseOpponent(random, wildPokemons);

        System.out.println("  Your opponent for this battle is '" + opponent.getName() + "'");

        title("---Random Start---");
        boolean playerTurn = random.nextBoolean();

        while (!myPokemon.isFainted() && !opponent.isFainted()) {

            title("ROUND " + round);
            if (playerTurn) {
                subTitle(myPokemon.getName() + " turn.");
                Attack selectedPlayerAttack = chooseAttack(scanner, myPokemon);

                runTurn(selectedPlayerAttack, myPokemon, opponent, random);
            } else {
                subTitle(opponent.getName() + " turn.");
                Attack opponentAttack = chooseRandomAttack(opponent, random);

                runTurn(opponentAttack, opponent, myPokemon, random);
            }
            round++;
            playerTurn = !playerTurn;
        }
        if (myPokemon.isFainted()) {
            handleEndFight(
                    " lost the battle",
                    myPokemon,
                    opponent,
                    false,
                    statistics,
                    jsonHandler
            );
        } else {
            handleEndFight(
                    " won the battle",
                    myPokemon,
                    opponent,
                    true,
                    statistics,
                    jsonHandler
            );

            handleVictory(scanner, pokedex, opponent, wildPokemons, statistics, jsonHandler);
        }
    }

    private static void runTurn(Attack attack, Pokemon attacker, Pokemon defender, Random random) {
        int hitChance = random.nextInt(100) + 1;
        boolean isHit = hitChance <= attack.getAccuracy();
        if (!isHit) {
            System.out.println(
                    "  " + attacker.getName() +
                            " used " + attack.getName() +
                            " but missed!"
            );
            separator();
        }
        else {
            int damage = calculateDamage(defender, attack, random);
            defender.takeDamage(damage);
            System.out.println(
                    "  " + attacker.getName() +
                            " used " + attack.getName() + "!"
            );
            System.out.println(
                    "  Damage: " + damage
            );

            System.out.println(
                    "  " + defender.getName() +
                            " HP: " +
                            defender.getCurrentHp() +
                            "/" +
                            defender.getMaxHp()
            );

            separator();
        }
    }

    public static void handleEndFight(String prompt, Pokemon myPokemon, Pokemon opponent, boolean won, BattleStatistics statistics, JsonHandler jsonHandler) {

        separator();
        System.out.println("  " + myPokemon.getName() + prompt);
        statistics.addPokemonUse(myPokemon.getName());

        if (won) {
            statistics.addWin();
            statistics.addPokemonWin(myPokemon.getName());

            myPokemon.heal(myPokemon.getMaxHp());
            opponent.heal(opponent.getMaxHp());
        } else {
            statistics.addLoss();
            statistics.addPokemonLoss(myPokemon.getName());

            myPokemon.heal(myPokemon.getMaxHp());
        }

        jsonHandler.saveStatistics("statistic.json", statistics);
        System.out.println("  Statistics saved!");
    }

    public static void handleVictory(Scanner scanner, Pokedex pokedex, Pokemon catched, List<Pokemon> wildPokemon, BattleStatistics statistics, JsonHandler jsonHandler) {
        subTitle("Gotta catch them all!");
        menuOption(1, "Yes");
        menuOption(2, "No");
        int choice = readIntBetween(scanner, 1, 2, "  Select on option: > ");
        separator();
        if (choice == 1) {
            pokedex.addPokemon(catched);
        }
        ;
        subTitle("Play again");
        menuOption(1, "Yes");
        menuOption(2, "Exit");
        separator();
        choice = readIntBetween(scanner, 1, 2, "  Select on option: > ");
        separator();

        if (choice == 1) {
            startBattle(scanner, pokedex, wildPokemon, statistics, jsonHandler);
            return;
        }
        System.out.println("  Returning to menu");
    }

}

