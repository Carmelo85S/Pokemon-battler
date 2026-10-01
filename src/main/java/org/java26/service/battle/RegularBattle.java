package org.java26.service.battle;

import org.java26.exceptions.InvalidPokemonException;
import org.java26.models.Attack;
import org.java26.models.Pokedex;
import org.java26.models.Pokemon;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static org.java26.consoleLayout.Layout.*;
import static org.java26.inputHelpers.InputHelper.readIntBetween;
import static org.java26.inputHelpers.InputHelper.readString;
import static org.java26.service.PokemonCreator.createPokemon;
import static org.java26.service.PokemonViewer.showPokemons;

public class RegularBattle {
    public static void startBattle(Scanner scanner, Pokedex pokedex, List<Pokemon> wildPokemons) {
        int round = 1;

        Random random = new Random();

        if (wildPokemons.isEmpty()) {
            title("No wild pokemons available");
            return;
        }

        if (pokedex.getPokemons().isEmpty()) {
            showOptions(scanner, pokedex);
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
                runPlayerTurn(scanner, myPokemon, opponent, random);
                round ++;
            } else {
                runOpponentTurn(opponent, myPokemon, random);
                round ++;
            }
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

    public static void showOptions(Scanner scanner, Pokedex pokedex) {
        title("No pokemons available");
        subTitle("Do you wanna create pokemons?");
        menuOption(1, "Yes");
        menuOption(2, "No");
        separator();
        try {
            int choice = readIntBetween(scanner, 1, 2, "Select one option: ");
            if (choice == 1) {
                createPokemon(scanner, pokedex);
            }
            return;
        } catch (InvalidPokemonException e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    private static Pokemon chooseMyPokemon(Scanner scanner, Pokedex pokedex) {

        while (true) {
            try {
                String choice = readString(
                        scanner,
                        "  Choose your Pokemon for the battle: ",
                        "  You choose "
                );

                return pokedex.getPokemon(choice);

            } catch (InvalidPokemonException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static Pokemon chooseOpponent(Random random, List<Pokemon> wildPokemons) {
        int randomOpponent = random.nextInt(wildPokemons.size());
        return wildPokemons.get(randomOpponent);
    }

    private static Attack chooseAttack(Scanner scanner, Pokemon pokemon) {
        showAttacks(pokemon);
        while (true) {
            try {
                int choice = readIntBetween(
                        scanner,
                        1,
                        pokemon.getAttacks().size(),
                        "Choose attack: "
                );
                separator();
                return pokemon.getAttacks().get(choice - 1);
            } catch (InvalidPokemonException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static Attack chooseRandomAttack(Pokemon pokemon, Random random) {
        int randomAttack = random.nextInt(pokemon.getAttacks().size());
        return pokemon.getAttacks().get(randomAttack);
    }

    private static void runPlayerTurn(Scanner scanner, Pokemon myPokemon, Pokemon opponent, Random random) {
        subTitle(myPokemon.getName() + " Starts");
        Attack selectedPlayerAttack = chooseAttack(scanner, myPokemon);

        int hitChance = random.nextInt(100) + 1;
        boolean isHit = hitChance <= selectedPlayerAttack.getAccuracy();
        if (!isHit) {
            System.out.println(
                    "  " + myPokemon.getName() +
                            " used " + selectedPlayerAttack.getName() +
                            " but missed!"
            );
            separator();
        } else {
            opponent.takeDamage(selectedPlayerAttack.getBaseDamage());
            System.out.println(
                    "  " + myPokemon.getName() +
                            " used " + selectedPlayerAttack.getName() + "!"
            );
            System.out.println(
                    "  " + opponent.getName() +
                            " HP: " +
                            opponent.getCurrentHp() +
                            "/" +
                            opponent.getMaxHp()
            );
            separator();
        }
    }

    private static void runOpponentTurn(Pokemon opponent, Pokemon myPokemon, Random random) {
        Attack selectedOpponentAttack = chooseRandomAttack(opponent, random);
        int damagePlayer = selectedOpponentAttack.getBaseDamage();

        int hitChance = random.nextInt(100) + 1;
        boolean isHit = hitChance <= selectedOpponentAttack.getAccuracy();
        if (!isHit) {
            System.out.println(
                    "  " + opponent.getName() +
                            " used " + selectedOpponentAttack.getName() +
                            " but missed!"
            );
            separator();
        } else {
            myPokemon.takeDamage(damagePlayer);

            System.out.println(
                    "  " + opponent.getName() +
                            " used " + selectedOpponentAttack.getName() + "!"
            );
            System.out.println(
                    "  " + myPokemon.getName() +
                            " HP: " +
                            myPokemon.getCurrentHp() +
                            "/" +
                            myPokemon.getMaxHp()
            );
            separator();
        }
    }

    public static void showAttacks(Pokemon pokemon) {
        System.out.println();
        System.out.println("  ATTACKS");
        System.out.println("+----------------+----------------+----------------+----------------+----------------+");
        System.out.printf(
                "| %-14s | %-14s | %-14s | %-14s | %-14s |%n",
                "Choice", "Name", "Damage", "Accuracy", "Type"
        );
        System.out.println("+----------------+----------------+----------------+----------------+----------------+");

        for (int i = 0; i < pokemon.getAttacks().size(); i++) {

            Attack attack = pokemon.getAttacks().get(i);

            System.out.printf(
                    "| %-14d | %-14s | %-14d | %-14d | %-14s |%n",
                    i + 1,
                    attack.getName(),
                    attack.getBaseDamage(),
                    attack.getAccuracy(),
                    attack.getType()
            );
        }

        System.out.println("+----------------+----------------+----------------+----------------+----------------+");
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

    public static void handleVictory(Scanner scanner, Pokedex pokedex, Pokemon opponent, List<Pokemon> wildPokemon) {
        subTitle("Gotta catch them all!");
        menuOption(1, "Yes");
        menuOption(2, "No");
        int choice = readIntBetween(scanner, 1, 2, "  Select on option: > ");
        separator();
        if (choice == 1) {
            pokedex.addPokemon(opponent);
        }
        ;
        subTitle("Play again");
        menuOption(1, "Yes");
        menuOption(2, "Exit");
        separator();
        choice = readIntBetween(scanner, 1, 2, "  Select on option: > ");
        separator();

        if (choice == 1) {
            startBattle(scanner, pokedex, wildPokemon);
            return;
        }
        System.out.println("  Returning to menu");
    }

}

