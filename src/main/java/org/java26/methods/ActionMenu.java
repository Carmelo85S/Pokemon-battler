package org.java26.methods;

import org.java26.models.Attack;
import org.java26.models.Pokemon;
import org.java26.enums.PokemonType;
import org.java26.exceptions.*;
import org.java26.handlers.JsonHandler;
import org.java26.models.Pokedex;

import java.util.List;
import java.util.Locale;
import java.util.MissingFormatArgumentException;
import java.util.Scanner;

import static org.java26.inputHelpers.InputHelper.*;

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
                    "  Select an option %d or %d: "
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
        String name;
        PokemonType type;
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

        System.out.println("  Back to main menu by entering 'quit'.");
        while (true) {
            try {
                name = readString(scanner, "  Enter new pokemon name: ", "  Chosen name: ");
                if (name.length() > 11) {
                    throw new InvalidPokemonNameException(
                            "  Pokemon name cannot be longer than 11 characters."
                    );
                } else if (name.length() < 2) {
                    throw new InvalidPokemonNameException(
                            "  Pokemon name cannot be shorter than 2 characters."
                    );
                }
                type = readType(scanner, "  Which type is your new pokemon?: ");
                maxHp = readIntBetween(scanner, 1, 100, "  Enter max HP between %d and %d: ");
                currentHp = readIntBetween(scanner, 1, maxHp, "  Enter current HP between %d and %d: ");
                while (true) {

                    String answer = readString(
                            scanner,
                            "  Do you want to enter attacks? Yes / No: ",
                            "  You choose "
                    );

                    if (answer.equalsIgnoreCase("Yes")) {
                        break;
                    } else if (answer.equalsIgnoreCase("No")) {
                        System.out.println("  You can not create a pokemons without attacks.");
                        System.out.println("  Redirect to 'Main' menu");
                        return;
                    } else {
                        System.out.println("  Invalid input. Please enter Yes or No.");
                    }
                }
                //Attack
                Pokemon pokemon = new Pokemon(name, type, maxHp, currentHp);
                boolean addAttack = true;
                while (attackCount < 4 && addAttack) {
                    attackName = readString(
                            scanner,
                            "  Enter attack name: ",
                            "  Your attack name is "
                    );

                    baseDamage = readIntBetween(
                            scanner,
                            0,
                            100,
                            "  How much damage should your attack have? Enter a value between %d and %d: "
                    );

                    accuracy = readIntBetween(
                            scanner,
                            0,
                            100,
                            "  Enter accuracy between %d and %d: "
                    );

                    Attack attack = new Attack(
                            attackName,
                            baseDamage,
                            accuracy,
                            type
                    );

                    if (attackCount == 0) {
                        pokedex.addPokemon(pokemon);
                        System.out.println("  Pokemon '" + pokemon.name + "' created successfully.");
                    }

                    pokemon.addAttack(attack);
                    System.out.println("  Attack '" + attackName + "' added successfully.");

                    attackCount++;

                    if (attackCount == 4) {
                        System.out.println("  Attack slots filled");
                        break;
                    }
                    while (true) {
                        String choice = readString(
                                scanner,
                                "  Do you want to enter another attack? (yes/no): ",
                                "  You choose "
                        );
                        if (choice.equalsIgnoreCase("yes")) {
                            break;
                        }
                        if (choice.equalsIgnoreCase("no")) {
                            System.out.println("  Back to main menu.");
                            addAttack = false;
                            break;
                        }
                        System.out.println(
                                "  Invalid input. Please enter Yes or No."
                        );
                    }
                }
                break;
            } catch (InvalidPokemonNameException e) {
                System.out.println(e.getMessage());
            } catch (QuitPokemonOperationException e) {
                System.out.println("  Operation cancelled.");
                return;
            }
        }
    }

    public static void customizePokemon(Scanner scanner, Pokedex pokedex) {
        if (pokedex.getPokemons().isEmpty()) {
            System.out.println("  No pokemons listed");
            return;
        }

        showPokemons(pokedex);
        String choice = readString(scanner, "  Choose pokemon to customize: ", "  Your chosen pokemon is ");

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

            int chooseOperation = readIntBetween(
                    scanner,
                    1,
                    6,
                    "  Choose an option between %d and %d%n  > ");

            switch (chooseOperation) {
                case 1 -> pokemon.name = readString(scanner, "  Enter new name: ", "  New name is ");
                case 2 -> pokemon.type = readType(scanner, "  Enter new type: ");
                case 3 -> pokemon.maxHp = readIntBetween(scanner, 1, 100, "  Enter new hp value");
                case 4 -> {
                    System.out.println("  Current attacks:");
                    for (Attack attack : pokemon.attacks) {
                        System.out.println("  - " + attack.name);
                    }
                    if (pokemon.attacks.size() == 4) {
                        System.out.println("  Attack slots are already full.");
                        break;
                    }
                    while (pokemon.attacks.size() < 4) {
                        String attackName = readString(
                                scanner,
                                "  Enter attack name: ",
                                "  Attack name "
                        );
                        int baseDamage = readIntBetween(
                                scanner,
                                0,
                                100,
                                "  How much damage should your attack have? Enter a value between %d and %d: "
                        );
                        int accuracy = readIntBetween(
                                scanner,
                                0,
                                100,
                                "  Enter accuracy between %d and %d: "
                        );
                        Attack newAttack = new Attack(
                                attackName,
                                baseDamage,
                                accuracy,
                                pokemon.type
                        );
                        pokemon.addAttack(newAttack);

                        System.out.println(
                                "  Attack '" + attackName + "' added successfully."
                        );
                        if (pokemon.attacks.size() == 4) {
                            System.out.println("  Attack slots filled");
                            break;
                        }
                        String userChoice;
                        while (true) {
                            userChoice = readString(
                                    scanner,
                                    "  Do you want to enter another attack? (yes/no): ",
                                    "  Your choice is "
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
                        System.out.println("  - " + attack.name);
                    }

                    String attackToRemove = readString(
                            scanner,
                            "  Which attack do you want to remove?: ",
                            "  You want to remove "
                    );

                    boolean attackRemoved = false;

                    for (int i = 0; i < pokemon.attacks.size(); i++) {
                        if (pokemon.attacks.get(i).name.equalsIgnoreCase(attackToRemove)) {
                            pokemon.attacks.remove(i);
                            System.out.println("  Attack removed.");
                            attackRemoved = true;
                            break;
                        }
                    }

                    if (!attackRemoved) {
                        System.out.println("  Attack not found");
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

    public static void deletePokemon(Scanner scanner, Pokedex pokedex) throws QuitPokemonOperationException {

        showPokemons(pokedex);

        if (pokedex.getPokemons().isEmpty()) {
            System.out.println("  There are no Pokemon to delete.");
            return;
        }

        System.out.println("+========================================+");
        System.out.println("|              DELETE POKEMON            |");
        System.out.println("+========================================+");
        System.out.println("|                                        |");
        System.out.println("|    Which method do you want to use?    |");
        System.out.println("|                                        |");
        System.out.println("|        [1]. String                     |");
        System.out.println("|        [2]. Index                      |");
        System.out.println("|                                        |");
        System.out.println("+----------------------------------------+");

        int chooseMethod = readIntBetween(
                scanner,
                1,
                2,
                "  Choose your method (%d-%d): "
        );

        if (chooseMethod == 1) {

            while (true) {
                String pokemonToRemove = readString(
                        scanner,
                        "  Which Pokemon do you wanna delete?: ",
                        "  You wanna delete "
                );

                try {
                    pokedex.removePokemon(pokemonToRemove);

                    System.out.println(
                            "  " + pokemonToRemove + " has been removed from list."
                    );
                    return;

                } catch (InvalidPokemonException e) {
                    System.out.println(e.getMessage());
                    System.out.println("  Try again.");
                }
            }

        } else {
            while (true) {
                try {
                    int indexToRemove = readIntBetween(
                            scanner,
                            0,
                            pokedex.getPokemons().size() - 1,
                            "  Which Pokemon index do you wanna delete? (%d-%d): "
                    );

                    pokedex.removePokemonIndex(indexToRemove);

                    System.out.println(
                            "  Pokemon with index [" + indexToRemove
                                    + "] has been removed from list."
                    );
                    return;
                } catch (InvalidPokemonException e) {
                    System.out.println(e.getMessage());
                    System.out.println("  Try again.");
                }
            }
        }
    }

    public static void saveToFile(Pokedex pokedex, JsonHandler jsonHandler) {
        try {
            jsonHandler.savePokemon(
                    "pokemon.json",
                    pokedex.getPokemons()
            );
            System.out.println("  Pokemon saved successfully.");
        } catch (PokemonSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void loadFromFile(Pokedex pokedex, JsonHandler jsonHandler) {
        try {
            List<Pokemon> pokemons = jsonHandler.loadPokemon("pokemon.json");
            pokedex.removeAllPokemon();
            for (Pokemon p : pokemons) {
                pokedex.addPokemon(p);
            }
            System.out.println("  Pokemons loaded successfully.");
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
            System.out.println("  Pokemons loaded successfully.");
            jsonHandler.savePokemon(
                    "pokemon.json",
                    pokedex.getPokemons()
            );

            System.out.println("  Pokemons saved successfully.");
        } catch (PokemonLoadException | PokemonSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void getPokemonInfo(Scanner scanner, Pokedex pokedex) {
        if (pokedex.getPokemons().isEmpty()) {
            System.out.println("  No pokemons listed");
            return;
        }

        showPokemons(pokedex);

        String choice = readString(
                scanner,
                "  Retrieve pokemon info: ",
                "  Your chosen pokemon is "
        );

        try {
            Pokemon pokemon = pokedex.getPokemon(choice);
            System.out.println();
            System.out.println("+----------------+----------------+----------------+----------------+");
            System.out.printf(
                    "| %-14s | %-14s | %-14s | %-14s |%n",
                    "Name", "Type", "Max HP", "Current HP"
            );
            System.out.println("+----------------+----------------+----------------+----------------+");

            System.out.printf(
                    "| %-14s | %-14s | %-14d | %-14d |%n",
                    pokemon.name,
                    pokemon.type,
                    pokemon.maxHp,
                    pokemon.currentHp
            );

            System.out.println("+----------------+----------------+----------------+----------------+");

            // Attack information
            System.out.println();
            System.out.println("  ATTACKS");
            System.out.println("+----------------+----------------+----------------+----------------+");
            System.out.printf(
                    "| %-14s | %-14s | %-14s | %-14s |%n",
                    "Name", "Damage", "Accuracy", "Type"
            );
            System.out.println("+----------------+----------------+----------------+----------------+");

            for (Attack attack : pokemon.attacks) {
                System.out.printf(
                        "| %-14s | %-14d | %-14d | %-14s |%n",
                        attack.name,
                        attack.baseDamage,
                        attack.accuracy,
                        attack.type
                );
            }

            System.out.println("+----------------+----------------+----------------+----------------+");

        } catch (InvalidPokemonException e) {
            System.out.println(e.getMessage());
        } catch (MissingFormatArgumentException e) {
            System.out.println("  Missing data");
        }
    }
}

