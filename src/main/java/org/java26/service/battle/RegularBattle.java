package org.java26.service.battle;

import org.java26.exceptions.InvalidPokemonException;
import org.java26.models.Attack;
import org.java26.models.Pokedex;
import org.java26.models.Pokemon;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static org.java26.consoleLayout.Layout.*;
import static org.java26.inputHelpers.InputHelper.readIntBetween;
import static org.java26.inputHelpers.InputHelper.readString;
import static org.java26.service.Menu.runAction;
import static org.java26.service.PokemonCreator.createPokemon;
import static org.java26.service.PokemonViewer.showPokemons;

public class RegularBattle {
    public static void startBattle(
            Scanner scanner,
            Pokedex pokedex,
            List<Pokemon> wildPokemons) {
        try {
            if (wildPokemons.isEmpty()) {
                title("No wild pokemons available");
                return;
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        if (pokedex.getPokemons().isEmpty()) {
            title("No pokemons available");
            subTitle("Do you wanna create pokemons?");
            menuOption(1, "Yes");
            menuOption(2, "No");
            separator();
            try {
                int choice = readIntBetween(scanner, 1, 2, "Select one option: ");
                if (choice == 1) {
                    createPokemon(scanner, pokedex);
                    return;
                }
                ;
            } catch (InvalidPokemonException e) {
                System.out.println(e.getMessage());
            }
        }

        title("Choose your pokemon");
        showPokemons(pokedex);

        Pokemon myPokemon = choosePokemon(scanner, pokedex);

        Random random = new Random();
        int randomOpponent = random.nextInt(wildPokemons.size());
        Pokemon opponent = wildPokemons.get(randomOpponent);

        System.out.println("  Your opponent for this battle is '" + opponent.getName() + "'");

        title("---Random Start---");
        boolean playerTurn = random.nextBoolean();

        while (!isFainted(myPokemon) && !isFainted(opponent)) {
            //PLAYER TURN
            if (playerTurn) {
                subTitle(myPokemon.getName() + " Starts");

                showAttacks(myPokemon);

                int choice;
                while (true) {
                    try {
                        choice = readIntBetween(
                                scanner,
                                1,
                                myPokemon.getAttacks().size(),
                                "Choose attack: "
                        );
                        separator();
                        break;
                    } catch (InvalidPokemonException e) {
                        System.out.println(e.getMessage());
                    }
                }
                Attack selectedAttack = myPokemon.getAttacks().get(choice - 1);

                boolean isHit = random.nextBoolean();
                if (!isHit) {
                    System.out.println("  You missed!");
                    separator();
                } else {
                    int damageOpponent = selectedAttack.getBaseDamage();

                    takeDamage(opponent, damageOpponent);
                    System.out.println(
                            myPokemon.getName() +
                                    " used " +
                                    selectedAttack.getName()
                    );
                    separator();
                    System.out.println(
                            opponent.getName() +
                                    " HP: " +
                                    opponent.getCurrentHp() +
                                    "/" +
                                    opponent.getMaxHp()
                    );
                }

                playerTurn = !playerTurn;

            } else {
                int randomAttack = random.nextInt(opponent.getAttacks().size());
                Attack selectedAttack = opponent.getAttacks().get(randomAttack);

                int damagePlayer = selectedAttack.getBaseDamage();

                takeDamage(myPokemon, damagePlayer);
                System.out.println(
                        opponent.getName() +
                                " used " +
                                selectedAttack.getName()
                );
                separator();
                System.out.println(
                        myPokemon.getName() +
                                " HP: " +
                                myPokemon.getCurrentHp() +
                                "/" +
                                myPokemon.getMaxHp()
                );
                playerTurn = !playerTurn;
            }
        }
        if (isFainted(myPokemon)) {
            separator();
            System.out.println("  " + myPokemon.getName() + " lost the battle");

            //GOOD FOR NOW, IF THERE IS TIME, ADD POKEMON HEAL CENTER
            healPokemons(myPokemon, opponent, 1000);
        } else {
            separator();
            System.out.println("  " + myPokemon.getName() + " won the battle.");

            //GOOD FOR NOW, IF THERE IS TIME, ADD POKEMON HEAL CENTER
            healPokemons(myPokemon, opponent, 1000);

            subTitle("Gotta catch them all!");
            menuOption(1, "Yes");
            menuOption(2, "No");
            int choice = readIntBetween(scanner, 1, 2, "  Do you want to catch opponent's pokemon?");
            if (choice == 1) {
                pokedex.addPokemon(wildPokemons.get(randomOpponent));
            } else {
                subTitle("Play again");
                menuOption(1, "Yes");
                menuOption(2, "Exit");
                choice = readIntBetween(scanner, 1, 2, "  Play again?");

                if (choice == 1) {
                    startBattle(scanner, pokedex, wildPokemons);
                    return;
                } else {
                    System.out.println("  Returning to menu");
                    return;
                }
            }
        }
    }

    private static Pokemon choosePokemon(Scanner scanner, Pokedex pokedex) {

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

    public static void healPokemons(Pokemon myPokemon, Pokemon opponent, int amount) {
        if (amount < 0)
            throw new IllegalArgumentException(
                    "  Heal value can not be negative"
            );
        myPokemon.setCurrentHp(Math.min(myPokemon.getMaxHp(), myPokemon.getCurrentHp() + amount));
        opponent.setCurrentHp(Math.min(opponent.getMaxHp(), opponent.getCurrentHp() + amount));

    }

    public static void takeDamage(Pokemon pokemon, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("  Damage value can not be negative");
        }
        pokemon.setCurrentHp(Math.max(0, pokemon.getCurrentHp() - amount));
    }

    public static boolean isFainted(Pokemon pokemon) {
        return pokemon.getCurrentHp() == 0;
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

}

