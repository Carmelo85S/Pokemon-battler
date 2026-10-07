package org.java26.service.battle;

import org.java26.handlers.JsonHandler;
import org.java26.models.Attack;
import org.java26.models.Pokedex;
import org.java26.models.Pokemon;
import org.java26.models.PokemonType;

import java.util.Random;
import java.util.Scanner;

import static org.java26.consoleLayout.Layout.*;
import static org.java26.consoleLayout.Layout.menuOption;
import static org.java26.consoleLayout.Layout.subTitle;
import static org.java26.inputHelpers.InputHelper.readIntBetween;
import static org.java26.service.StatisticsService.*;

public class BattleLogic {

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
    }

    public static void handleVictory(
            Scanner scanner,
            Pokedex pokedex,
            Pokemon pokemon,
            Pokemon caught,
            BattleStatistics statistics,
            JsonHandler jsonHandler,
            Runnable playAgain
    ) {
        evolvePokemon(pokemon, statistics);

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

        jsonHandler.saveStatistics("statistic.json", statistics);
        System.out.println("  Statistics saved!");

        if (choice == 1) {
            playAgain.run();
            return;
        }

        System.out.println("  Returning to menu");
    }

    public static double effectiveness(Pokemon defender, Attack attack) {
        PokemonType attackType = attack.getType();
        PokemonType opponentType = defender.getType();

        if (attackType == PokemonType.FIRE && opponentType == PokemonType.GRASS) {
            return 2.0;
        }

        if (attackType == PokemonType.WATER && opponentType == PokemonType.FIRE) {
            return 2.0;
        }

        if (attackType == PokemonType.GRASS && opponentType == PokemonType.WATER) {
            return 2.0;
        }

        if (attackType == PokemonType.ELECTRIC && opponentType == PokemonType.WATER) {
            return 2.0;
        }

        if (attackType == PokemonType.FIRE && opponentType == PokemonType.WATER) {
            return 0.5;
        }

        if (attackType == PokemonType.WATER && opponentType == PokemonType.GRASS) {
            return 0.5;
        }

        if (attackType == PokemonType.GRASS && opponentType == PokemonType.FIRE) {
            return 0.5;
        }

        if (attackType == PokemonType.ELECTRIC && opponentType == PokemonType.GRASS) {
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

    public static void runTurn(Attack attack, Pokemon attacker, Pokemon defender, Random random, BattleStatistics statistics) {
        recordPokemonAttackUse(statistics, attacker, attack);
        int hitChance = random.nextInt(100) + 1;
        boolean isHit = hitChance <= attack.getAccuracy();
        if (!isHit) {
            System.out.println(
                    "  " + attacker.getName() +
                            " used " + attack.getName() +
                            " but missed!"
            );
            separator();
        } else {
            boolean criticalHit = random.nextInt(15) == 0;
            attack.execute(attacker, defender, criticalHit);
            System.out.println(
                    "  " + attacker.getName() +
                            " used " + attack.getName() + "!"
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
}
