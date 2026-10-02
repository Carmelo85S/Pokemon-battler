package org.java26.service.battle;

import org.java26.exceptions.InvalidPokemonException;
import org.java26.exceptions.QuitPokemonOperationException;
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
import static org.java26.service.battle.BattleLogic.calculateDamage;
import static org.java26.service.battle.BattleLogic.chooseRandomAttack;

public class HandicapBattle {
    public static void startHandicapBattle(Scanner scanner, Pokedex pokedex, List<Pokemon> wildPokemons) {
        title("Handicap Battle");

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
                runTurnAccuracyBonus(selectedPlayerAttack, myPokemon, opponent, random, playerAccuracyBonus);


            } else {
                subTitle(opponent.getName() + " turn.");
                Attack opponentAttack = chooseRandomAttack(opponent, random);
                runTurnAccuracyBonus(opponentAttack, opponent, myPokemon, random, opponentAccuracyBonus);
            }
            round++;
            playerTurn = !playerTurn;
        }
        if (myPokemon.isFainted()) {
            handleEndFight(
                    " lost the battle",
                    myPokemon,
                    opponent,
                    false
            );
        } else {
            handleEndFight(
                    " won the battle",
                    myPokemon,
                    opponent,
                    true
            );

            handleVictory(scanner, pokedex, opponent, wildPokemons);
        }
    }

    private static void runTurnAccuracyBonus(Attack attack, Pokemon attacker, Pokemon defender, Random random, int accuracyBonus) {
        int hitChance = random.nextInt(100) + 1;
        int accuracy = Math.min(100, attack.getAccuracy() - accuracyBonus);
        boolean isHit = hitChance <= accuracy;
        if (!isHit) {
            System.out.println(
                    "  " + attacker.getName() +
                            " used " + attack.getName() +
                            " but missed!"
            );
            separator();
        } else {
            int damage = calculateDamage(defender, attack, random);
            boolean isCritical = random.nextInt(16) == 0;

            if(isCritical){
                damage *= 2;
                subTitle("!!!Critical hit!!!");
            }
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

    public static void handleEndFight(String prompt, Pokemon myPokemon, Pokemon opponent, boolean won) {

        separator();
        System.out.println("  " + myPokemon.getName() + prompt);
        if (won) {
            myPokemon.addWin();
            myPokemon.heal(myPokemon.getMaxHp());
            opponent.heal(opponent.getMaxHp());
        } else {
            myPokemon.addLoss();
            myPokemon.heal(myPokemon.getMaxHp());
        }
    }

    public static void handleVictory(Scanner scanner, Pokedex pokedex, Pokemon caught, List<Pokemon> wildPokemon) {
        subTitle("Gotta catch them all!");
        menuOption(1, "Yes");
        menuOption(2, "No");
        int choice = readIntBetween(scanner, 1, 2, "  Select on option: > ");
        separator();
        if (choice == 1) {
            pokedex.addPokemon(caught);
        }
        ;
        subTitle("Play again");
        menuOption(1, "Yes");
        menuOption(2, "Exit");
        separator();
        choice = readIntBetween(scanner, 1, 2, "  Select on option: > ");
        separator();

        if (choice == 1) {
            startHandicapBattle(scanner, pokedex, wildPokemon);
            return;
        }
        System.out.println("  Returning to menu");
    }

}

