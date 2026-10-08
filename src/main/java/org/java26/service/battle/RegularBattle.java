package org.java26.service.battle;

import org.java26.handlers.JsonHandler;
import org.java26.models.Attack;
import org.java26.models.Pokedex;
import org.java26.models.Pokemon;
import org.java26.models.PokemonType;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static org.java26.consoleLayout.BattleUI.*;
import static org.java26.consoleLayout.Layout.*;
import static org.java26.inputHelpers.InputHelper.readIntBetween;
import static org.java26.service.AttackCreator.createAttack;
import static org.java26.service.PokemonViewer.showPokemons;
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

        Pokemon opponent = chooseOpponent(random, wildPokemons);
        if (isNull(opponent)) {
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

            handleVictory(
                    scanner,
                    pokedex,
                    myPokemon,
                    opponent,
                    statistics,
                    jsonHandler,
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

    private static Pokemon preparePlayerPokemon(Scanner scanner, Pokedex pokedex) {
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
                        statistics
                );
            }

            round++;
            playerTurn = !playerTurn;
        }
    }
}

