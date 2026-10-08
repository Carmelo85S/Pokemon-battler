package org.java26.service.battle;

import org.java26.exceptions.QuitPokemonOperationException;
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
import static org.java26.service.battle.BattleLogic.*;

public class HandicapBattle {
    public static void startHandicapBattle(Scanner scanner, Pokedex pokedex, List<Pokemon> wildPokemons, BattleStatistics statistics, JsonHandler jsonHandler) {
        title("Handicap Battle");
        showHandicapBattleRules();
        Random random = new Random();

        if (wildPokemons.isEmpty()) {
            title("No wild pokemons available");
            return;
        }

        Pokemon myPokemon = preparePlayerPokemon(scanner, pokedex);
        if (myPokemon == null) {
            return;
        }
        Pokemon opponent = chooseOpponent(random, wildPokemons);
        if (opponent == null) {
            return;
        }

        System.out.println("  Your opponent for this battle is '" + opponent.getName() + "'");

        int[] handicap = chooseHandicap(scanner, myPokemon, opponent);

        if (handicap == null) {
            return;
        }

        int playerAccuracyPenalty = handicap[0];
        int opponentAccuracyPenalty = handicap[1];

        runBattle(scanner, myPokemon, opponent, random, statistics, playerAccuracyPenalty, opponentAccuracyPenalty);

        if (myPokemon.isFainted()) {
            handleEndFight(" lost the battle", myPokemon, opponent, false, statistics, jsonHandler
            );
        } else {
            handleEndFight(" won the battle", myPokemon, opponent, true, statistics, jsonHandler);
            handleVictory(scanner, pokedex, myPokemon, opponent, statistics, jsonHandler, () -> startHandicapBattle(scanner, pokedex, wildPokemons, statistics, jsonHandler));
        }
    }

    private static int[] chooseHandicap(
            Scanner scanner,
            Pokemon myPokemon,
            Pokemon opponent
    ) {
        int playerAccuracyPenalty = 0;
        int opponentAccuracyPenalty = 0;

        title("Choose handicap for the match");
        menuOption(1, "Player + 50 maxHp");
        menuOption(2, "Opponent + 50 maxHp");
        menuOption(3, "Player - 20 accuracy");
        menuOption(4, "Opponent - 20 accuracy");
        menuOption(5, "Exit");
        backOption();

        int choice = readIntBetween(scanner, 1, 5, "Select an option: > ");

        try {
            switch (choice) {
                case 1 -> {
                    myPokemon.setMaxHp(myPokemon.getMaxHp() + 50);
                    myPokemon.setCurrentHp(myPokemon.getMaxHp());
                }
                case 2 -> {
                    opponent.setMaxHp(opponent.getMaxHp() + 50);
                    opponent.setCurrentHp(opponent.getMaxHp());
                }
                case 3 -> playerAccuracyPenalty = 20;
                case 4 -> opponentAccuracyPenalty = 20;
                case 5 -> {
                    return null;
                }
            }
        } catch (QuitPokemonOperationException e) {
            System.out.println(e.getMessage());
            return null;
        }

        return new int[]{playerAccuracyPenalty, opponentAccuracyPenalty};
    }

    private static void runBattle(
            Scanner scanner,
            Pokemon myPokemon,
            Pokemon opponent,
            Random random,
            BattleStatistics statistics,
            int playerAccuracyPenalty,
            int opponentAccuracyPenalty
    ) {
        int round = 1;
        boolean playerTurn = random.nextBoolean();

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
                        BattleLogic.chooseRandomAttack(opponent, random);

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
    }
}


