package org.java26.service.battle;

import org.java26.handlers.JsonHandler;
import org.java26.models.Attack;
import org.java26.models.Pokedex;
import org.java26.models.Pokemon;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static org.java26.UI.BattleUI.*;
import static org.java26.UI.Layout.*;
import static org.java26.service.battle.BattleLogic.*;

public class RegularBattle {
    public static void startBattle(Scanner scanner, Pokedex pokedex, List<Pokemon> wildPokemons, BattleStatistics statistics, JsonHandler jsonHandler) {
        title("Regular Battle");
        showRegularBattleRules();

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

        runBattle(scanner, myPokemon, opponent, random, statistics);

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
            evolvePokemon(myPokemon, statistics);
            handleVictory(
                    scanner,
                    pokedex,
                    opponent,
                    statistics,
                    () -> startBattle(
                            scanner,
                            pokedex,
                            wildPokemons,
                            statistics,
                            jsonHandler
                    )
            );



        }
    }

    private static void runBattle(
            Scanner scanner,
            Pokemon myPokemon,
            Pokemon opponent,
            Random random,
            BattleStatistics statistics
    ) {
        int round = 1;
        boolean playerTurn = random.nextBoolean();

        title("---Random Start---");

        while (!myPokemon.isFainted() && !opponent.isFainted()) {

            title("ROUND " + round);

            if (playerTurn) {
                subTitle(myPokemon.getName() + " turn.");

                Attack selectedPlayerAttack =
                        chooseAttack(scanner, myPokemon);

                runTurn(
                        selectedPlayerAttack,
                        myPokemon,
                        opponent,
                        random,
                        0,
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
                        0,
                        statistics
                );
            }

            round++;
            playerTurn = !playerTurn;
        }
    }
}

