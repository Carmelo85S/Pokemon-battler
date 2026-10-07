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
import static org.java26.service.PokemonViewer.showPokemons;
import static org.java26.service.StatisticsService.*;

public class HandicapBattle {
    public static void startHandicapBattle(Scanner scanner, Pokedex pokedex, List<Pokemon> wildPokemons, BattleStatistics statistics, JsonHandler jsonHandler) {
        title("Handicap Battle");
        showHandicapBattleRules();
        int round = 1;
        int choice;
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

        int playerAccuracyBonus = 0;
        int opponentAccuracyBonus = 0;

        title("Choose handicap for the match");
        menuOption(1, "Player + 50 maxHp");
        menuOption(2, "Opponent + 50 maxHp");
        menuOption(3, "Player - 20 accuracy");
        menuOption(4, "Opponent - 20 accuracy");
        menuOption(5, "Exit");
        backOption();

        choice = readIntBetween(scanner, 1, 5, "Select an option: > ");

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
                case 3 -> playerAccuracyBonus = 20;
                case 4 -> opponentAccuracyBonus = 20;
                case 5 -> {
                    return;
                }
            }
        } catch (QuitPokemonOperationException e) {
            System.out.println(e.getMessage());
        }

        title("---Random Start---");
        boolean playerTurn = random.nextBoolean();

        while (!myPokemon.isFainted() && !opponent.isFainted()) {

            title("ROUND " + round);
            if (playerTurn) {
                subTitle(myPokemon.getName() + " turn.");

                Attack selectedPlayerAttack = chooseAttack(scanner, myPokemon);
                runTurnAccuracyBonus(selectedPlayerAttack, myPokemon, opponent, random, playerAccuracyBonus, statistics);

            } else {
                subTitle(opponent.getName() + " turn.");
                Attack opponentAttack = BattleLogic.chooseRandomAttack(opponent, random);
                runTurnAccuracyBonus(opponentAttack, opponent, myPokemon, random, opponentAccuracyBonus, statistics);
            }
            round++;
            playerTurn = !playerTurn;
        }
        if (myPokemon.isFainted()) {
            BattleLogic.handleEndFight(" lost the battle", myPokemon, opponent, false, statistics, jsonHandler
            );
        } else {
            BattleLogic.handleEndFight(" won the battle", myPokemon, opponent, true, statistics, jsonHandler);
            BattleLogic.handleVictory(scanner, pokedex, myPokemon, opponent, statistics, jsonHandler, () -> startHandicapBattle(scanner, pokedex, wildPokemons, statistics, jsonHandler));
        }
    }

    private static void runTurnAccuracyBonus(Attack attack, Pokemon attacker, Pokemon defender, Random random, int accuracyPenalty, BattleStatistics statistics) {
        recordAttacksUse(statistics, attack);
        recordPokemonAttackUse(statistics, attacker, attack);

        int hitChance = random.nextInt(100) + 1;
        int accuracy = Math.clamp(attack.getAccuracy() - accuracyPenalty, 0, 100);
        boolean isHit = hitChance <= accuracy;
        if (!isHit) {
            System.out.println("  " + attacker.getName() + " used " + attack.getName() + " but missed!");
            separator();
        } else {
            boolean criticalHit = random.nextInt(15) == 0;
            attack.execute(attacker, defender, criticalHit);
            System.out.println("  " + attacker.getName() + " used " + attack.getName() + "!");

            System.out.println("  " + defender.getName() + " HP: " + defender.getCurrentHp() + "/" + defender.getMaxHp());

            separator();
        }
    }

}

