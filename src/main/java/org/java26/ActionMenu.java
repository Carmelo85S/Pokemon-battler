package org.java26;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import static org.java26.InputHelper.*;

public class ActionMenu {

    public static void showAllPokemon(Scanner scanner, Pokedex pokedex) {

        if (pokedex.getPokemons().isEmpty()) {
            System.out.println();
            System.out.println("+========================================+");
            System.out.println("|              POKEDEX EMPTY             |");
            System.out.println("+========================================+");
            System.out.println("|                                        |");
            System.out.println("|        Nothing to show.                |");
            System.out.println("|                                        |");
            System.out.println("|        Do you want to create           |");
            System.out.println("|        a Pokemon?                      |");
            System.out.println("|                                        |");
            System.out.println("|        [1]. Yes                        |");
            System.out.println("|        [2]. No                         |");
            System.out.println("|                                        |");
            System.out.println("+----------------------------------------+");

            int choice = readIntBetween(
                    scanner,
                    1,
                    2,
                    "  Select an option: "
            );
            if (choice == 1) {
                insertNewPokemon(scanner, pokedex);
            } else {
                return;
            }
        }

        showPokemons(pokedex);
    }

    public static void insertNewPokemon(Scanner scanner, Pokedex pokedex) {
        //Pokemon obj
        String name = "";
        int maxHp;
        int currentHp;

        //Attack obj
        String attackName;
        int baseDamage;
        int accuracy;
        int attackCount = 0;

        System.out.println();
        System.out.println("+========================================+");
        System.out.println("|            CREATE POKEMON              |");
        System.out.println("+========================================+");
        System.out.println("|                                        |");
        System.out.println("|        Enter the Pokemon details       |");
        System.out.println("|                                        |");
        System.out.println("+----------------------------------------+");


        try {
            name = readString(scanner, "  Enter new pokemon name: ");
            if (name.length() > 11) {
                throw new InvalidPokemonException(
                        "  Pokemon name cannot be longer than 11 characters."
                );
            }
        } catch (InvalidPokemonException e) {
            System.out.println(e.getMessage());
        }

        PokemonType type = readType(scanner, "  Which type is your new pokemon?: ");
        maxHp = readIntBetween(scanner, 1, 100, "  Enter max HP between %d and %d: ");
        currentHp = readIntBetween(scanner, 1, maxHp, "  Enter current HP between %d and %d: ");
        Pokemon pokemon = new Pokemon(name, type, maxHp, currentHp);
        pokedex.addPokemon(pokemon);


        while (true) {
            String answer = readString(
                    scanner,
                    "  Do you want to enter attacks? Yes / No: "
            );

            if (answer.equalsIgnoreCase("Yes")) {
                break;
            } else if (answer.equalsIgnoreCase("No")) {
                return;
            } else {
                System.out.println("  Invalid input. Please enter Yes or No.");
            }
        }

        //Attack
        while (attackCount < 4) {
            attackName = readString(scanner, "  Enter attack name: ");
            baseDamage = readIntBetween(
                    scanner, 0, 100,
                    "  How much damage should your attack have? Enter a value between %d and %d: "
            );
            accuracy = readIntBetween(
                    scanner, 0, 100,
                    "  Enter accuracy between %d and %d: "
            );
            Attack attack = new Attack(attackName, baseDamage, accuracy, type);
            pokemon.addAttack(attack);

            attackCount++;
            if (attackCount == 4) {
                System.out.println("  Attack slots filled");
                break;
            }

            String choice = readString(
                    scanner,
                    "  Do you want to enter another attack? (yes/no): "
            );

            if (choice.equalsIgnoreCase("no")) {
                break;
            }

            if (!choice.equalsIgnoreCase("yes")) {
                System.out.println("  Invalid input.");
                break;
            }
        }
    }

    public static void customizePokemon(Scanner scanner, Pokedex pokedex) {
        if (pokedex.getPokemons().isEmpty()) {
            System.out.println("  No pokemons listed");
            return;
        }

        showPokemons(pokedex);
        String choice = readString(scanner, "  Choose pokemon to customize: ");

        Pokemon pokemon;

        try {
            pokemon = pokedex.getPokemon(choice);
        } catch (InvalidPokemonException e) {
            System.out.println(e.getMessage());
            return;
        }

        while (true) {
            System.out.println();
            System.out.println("+========================================+");
            System.out.println("         CUSTOMIZE - " + pokemon.name.toUpperCase(Locale.ROOT));
            System.out.println("+========================================+");
            System.out.println("|                                        |");
            System.out.println("|        [1]. Change name                |");
            System.out.println("|        [2]. Change type                |");
            System.out.println("|        [3]. Change HP                  |");
            System.out.println("|        [4]. Add attack                 |");
            System.out.println("|        [5]. Remove attack              |");
            System.out.println("|        [6]. Back to menu               |");
            System.out.println("|                                        |");
            System.out.println("+----------------------------------------+");
            System.out.println();

            int chooseOperation = readIntBetween(scanner, 1, 6, "  Choose an option between %d and %d: ");

            switch (chooseOperation) {
                case 1 -> {
                    pokemon.name = readString(scanner, "  Enter new name: ");

                }
                case 2 -> {
                    pokemon.type = readType(scanner, "  Enter new type: ");
                }
                case 3 -> {
                    pokemon.maxHp = readIntBetween(scanner, 1, 100, "  Enter new hp value");
                }
                case 4 -> {
                    for (Attack attack : pokemon.attacks) {
                        System.out.println(attack.name);
                    }

                    while (pokemon.attacks.size() < 4) {
                        String attackName = readString(scanner, "  Enter attack name: ");
                        int baseDamage = readIntBetween(
                                scanner, 0, 100,
                                "  How much damage should your attack have? Enter a value between %d and %d: "
                        );
                        int accuracy = readIntBetween(
                                scanner, 0, 100,
                                "  Enter accuracy between %d and %d: "
                        );
                        Attack newAttack = new Attack(attackName, baseDamage, accuracy, pokemon.type);
                        pokemon.addAttack(newAttack);

                        if (pokemon.attacks.size() == 4) {
                            System.out.println("  Attack slots filled");
                            break;
                        }

                        String userChoice;

                        while (true) {
                            userChoice = readString(
                                    scanner,
                                    "  Do you want to enter another attack? (yes/no): "
                            );

                            if (userChoice.equalsIgnoreCase("yes") ||
                                    userChoice.equalsIgnoreCase("no")) {
                                break;
                            }

                            System.out.println("  Please enter yes or no.");
                        }

                        if (userChoice.equalsIgnoreCase("no")) {
                            break;
                        }
                    }
                }

                case 5 -> {
                    System.out.println("  Current attacks:");
                    for (Attack attack : pokemon.attacks) {
                        System.out.println(attack.name);
                    }
                    String attackToRemove = readString(scanner, "  Which attack do you want to remove?: ");
                    for (int i = 0; i < pokemon.attacks.size(); i++) {
                        if (pokemon.attacks.get(i).name.equalsIgnoreCase(attackToRemove)) {
                            pokemon.attacks.remove(i);
                            System.out.println("  Attack removed.");
                            break;
                        }
                    }
                }
                case 6 -> {
                    System.out.println("  Back to menu");
                    return;
                }

                default -> System.out.println("  Invalid choice");
            }
        }
    }

    public static void showPokemons(Pokedex pokedex) {

        if (pokedex.getPokemons().isEmpty()) {
            System.out.println();
            System.out.println("+========================================+");
            System.out.println("|              POKEDEX EMPTY             |");
            System.out.println("+========================================+");
            System.out.println("|                                        |");
            System.out.println("|             Nothing to show.           |");
            System.out.println("|                                        |");
            System.out.println("+----------------------------------------+");
            return;
        }

        System.out.println();
        System.out.println("+----------------+----------------+");
        System.out.println("|        AVAILABLE POKEMONS       |");
        System.out.println("+----------------+----------------+");

        System.out.printf(
                "| %-14s | %-14s |%n",
                "Name", "Type"
        );

        System.out.println(
                "+----------------+----------------+"
        );
        for (Pokemon p : pokedex.getPokemons()) {
            System.out.printf(
                    "| %-14s | %-14s |%n",
                    p.name,
                    p.type
            );
        }
        System.out.println(
                "+----------------+----------------+"
        );

    }

    public static void deletePokemon(Scanner scanner, Pokedex pokedex) {
        showPokemons(pokedex);

        while (true) {
            String pokemonToRemove = readString(scanner, "Which Pokemon do you wanna delete?: ");
            try {
                pokedex.removePokemon(pokemonToRemove);
                System.out.println(pokemonToRemove + " has been removed from list.");
                break;
            } catch (InvalidPokemonException e) {
                System.out.println(e.getMessage());
                System.out.println("Try again.");
            }
        }
    }

    public static void saveToFile(Pokedex pokedex, JsonHandler jsonHandler) {
        try {
            jsonHandler.savePokemon(
                    "pokemon.json",
                    pokedex.getPokemons()
            );
            System.out.println("Pokemon saved successfully.");
        } catch (PokemonSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void loadFromFile(Pokedex pokedex, JsonHandler jsonHandler) {
        try {
            List<Pokemon> pokemons = jsonHandler.loadPokemon("pokemon.json");
            for (Pokemon p : pokemons) {
                pokedex.addPokemon(p);
            }
            System.out.println("Pokemon sloaded successfully.");
        } catch (PokemonLoadException e) {
            System.out.println(e.getMessage());
        }
    }

    static void resetToSeedData(Pokedex pokedex, JsonHandler jsonHandler) {
        pokedex.removeAllPokemon();
        try {
            List<Pokemon> pokemons = jsonHandler.loadPokemon("seed-pokemons.json");
            for (Pokemon p : pokemons) {
                pokedex.addPokemon(p);
            }
            System.out.println("Pokemon sloaded successfully.");
        } catch (PokemonLoadException e) {
            System.out.println(e.getMessage());
        }
    }
}

