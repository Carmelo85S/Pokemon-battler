package org.java26.methods;

import org.java26.enums.PokemonType;
import org.java26.exceptions.InvalidPokemonException;
import org.java26.exceptions.QuitPokemonOperationException;
import org.java26.models.Attack;
import org.java26.models.Pokedex;
import org.java26.models.Pokemon;

import java.util.Locale;
import java.util.Scanner;

import static org.java26.consoleLayout.Layout.*;
import static org.java26.inputHelpers.InputHelper.*;
import static org.java26.methods.ActionMenu.showPokemons;

public class PokemonCRUD {
    public static void createPokemon(Scanner scanner, Pokedex pokedex) {
        // Pokemon obj
        String name;
        PokemonType type;
        int maxHp;
        int currentHp;

        // Attack obj
        String attackName;
        int baseDamage;
        int accuracy;
        int attackCount = 0;

        separator();
        title("Create pokemon");
        subTitle("Enter pokemon details.");
        separator();
        backOption();

        try {
            name = readString(
                    scanner,
                    "  Enter new pokemon name: ",
                    "  Chosen name: "
            );
            System.out.println();

            type = readType(
                    scanner,
                    "  Which type is your new pokemon?: "
            );
            System.out.println();

            maxHp = readIntBetween(
                    scanner,
                    10,
                    1000,
                    "  Enter max HP between %d and %d: "
            );
            System.out.println();

            currentHp = readIntBetween(
                    scanner,
                    10,
                    maxHp,
                    "  Enter current HP between %d and %d: "
            );
            System.out.println();

            int answer = readIntBetween(
                    scanner,
                    1,
                    2,
                    "  Do you want to enter attacks? [1] Yes / [2] No: "
            );
            System.out.println();

            if (answer == 2) {
                System.out.println("  You cannot create a Pokemon without attacks.");
                System.out.println("  Redirect to 'Main' menu");
                return;
            }

            // Create Pokemon
            Pokemon pokemon = new Pokemon(
                    name,
                    type,
                    maxHp,
                    currentHp
            );

            boolean addAttack = true;

            while (attackCount < 4 && addAttack) {

                attackName = readString(
                        scanner,
                        "  Enter attack name: ",
                        "  Your attack name is "
                );
                System.out.println();

                baseDamage = readIntBetween(
                        scanner,
                        10,
                        100,
                        "  Enter attack damage. Value between %d and %d: "
                );
                System.out.println();

                accuracy = readIntBetween(
                        scanner,
                        10,
                        100,
                        "  Enter attack accuracy. Value between %d and %d: "
                );
                System.out.println();

                Attack attack = new Attack(
                        attackName,
                        baseDamage,
                        accuracy,
                        type
                );

                pokemon.addAttack(attack);

                System.out.println(
                        "  Attack '" + attackName + "' added successfully."
                );

                attackCount++;

                if (attackCount == 4) {
                    System.out.println("  Attack slots filled");
                    break;
                }

                separator();
                title("Pokemon attacks");
                subTitle("Do you want to add more attacks?");
                menuOption(1, "Yes");
                menuOption(2, "No");
                separator();

                int choice = readIntBetween(
                        scanner,
                        1,
                        2,
                        "  Select an option [%d] or [%d]: "
                );
                separator();

                if (choice == 2) {
                    System.out.println("  Back to main menu.");
                    addAttack = false;
                }
            }

            // Add Pokemon only after it has at least one attack
            pokedex.addPokemon(pokemon);

            System.out.println("  Pokemon '" + pokemon.name + "' created successfully.");

        } catch (InvalidPokemonException | QuitPokemonOperationException e) {
            System.out.println(e.getMessage());

        }
    }

    public static void updatePokemon(Scanner scanner, Pokedex pokedex) {
        if (pokedex.getPokemons().isEmpty()) {
            System.out.println("  No pokemons listed");
            return;
        }

        showPokemons(pokedex);
        backOption();

        String choice;
        Pokemon pokemon = null;

        while (pokemon == null) {

            try {
                choice = readString(scanner, "  Choose pokemon to customize: ", "  Pokemon: ");
                System.out.println();
            } catch (QuitPokemonOperationException e) {
                System.out.println("  Operation cancelled.");
                return;
            }

            try {
                pokemon = pokedex.getPokemon(choice);
            } catch (InvalidPokemonException e) {
                System.out.println(e.getMessage());
            }
        }

        while (true) {
            title("CUSTOMIZE " + pokemon.name.toUpperCase(Locale.ROOT));
            menuOption(1, "Change name");
            menuOption(2, "Change type");
            menuOption(3, "Change HP");
            menuOption(4, "Add attack");
            menuOption(5, "Remove attack");
            menuOption(6, "Back to menu");
            separator();
            int chooseOperation;
            try {
                chooseOperation = readIntBetween(
                        scanner,
                        1,
                        6,
                        "  Choose an option between %d and %d%n  > ");
            } catch (QuitPokemonOperationException e) {
                System.out.println(e.getMessage());
                return;
            }

            switch (chooseOperation) {
                case 1 -> {
                    try {
                        pokemon.name = readString(scanner, "  Enter new name: ", "  New name is ");
                        System.out.println();

                    } catch (QuitPokemonOperationException e) {
                        System.out.println(e.getMessage());
                    }
                }

                case 2 -> {
                    try {
                        pokemon.type = readType(scanner, "  Enter new type: ");
                        System.out.println();
                    } catch (QuitPokemonOperationException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 3 -> {
                    try {
                        pokemon.maxHp = readIntBetween(scanner, 10, 1000, "  Enter new hp value: ");
                        if (pokemon.currentHp > pokemon.maxHp) {
                            pokemon.currentHp = pokemon.maxHp;
                        }
                        System.out.println();
                    } catch (QuitPokemonOperationException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 4 -> {
                    try {
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
                                    "  Attack name: "
                            );
                            System.out.println();
                            int baseDamage = readIntBetween(
                                    scanner,
                                    10,
                                    100,
                                    "  Enter attack base damage, value between %d and %d: "
                            );
                            System.out.println();
                            int accuracy = readIntBetween(
                                    scanner,
                                    10,
                                    100,
                                    "  Enter attack accuracy, value between %d and %d: "
                            );
                            System.out.println();
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

                            int userChoice = readIntBetween(
                                    scanner,
                                    1,
                                    2,
                                    "  Do you want to enter another attack? [1] Yes / [2] No: "
                            );
                            separator();
                            if (userChoice == 2) {
                                break;
                            }
                        }
                    } catch (QuitPokemonOperationException e) {
                        System.out.println(e.getMessage());
                    }
                }

                case 5 -> {
                    System.out.println("  Current attacks:");

                    for (Attack attack : pokemon.attacks) {
                        System.out.println("  - " + attack.name);
                    }

                    while (true) {
                        try {
                            String attackToRemove = readString(
                                    scanner,
                                    "  Which attack do you want to remove?: ",
                                    "  You want to remove "
                            );

                            System.out.println();

                            boolean attackRemoved = false;

                            for (int i = 0; i < pokemon.attacks.size(); i++) {
                                if (pokemon.attacks.get(i).name.equalsIgnoreCase(attackToRemove)) {
                                    pokemon.attacks.remove(i);
                                    System.out.println(
                                            "  Attack '" + attackToRemove + "' removed."
                                    );
                                    attackRemoved = true;
                                    break;
                                }
                            }

                            if (!attackRemoved) {
                                System.out.println("  Attack not found.");
                                continue;
                            }

                            break;

                        } catch (InvalidPokemonException e) {
                            System.out.println(e.getMessage());

                        } catch (QuitPokemonOperationException e) {
                            System.out.println("  Operation cancelled.");
                            return;
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

    public static void getPokemonInfo(Scanner scanner, Pokedex pokedex) {
        if (pokedex.getPokemons().isEmpty()) {
            System.out.println("  No pokemons listed");
            return;
        }

        showPokemons(pokedex);
        backOption();
        while (true) {
            String choice = readString(
                    scanner,
                    "  Retrieve pokemon info: ",
                    "  Pokemon "
            );
            System.out.println();
            try {
                Pokemon pokemon = pokedex.getPokemon(choice);
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
            } catch (QuitPokemonOperationException e) {
                System.out.println(e.getMessage());
                return;
            }
        }
    }

    public static void deletePokemon(Scanner scanner, Pokedex pokedex) throws QuitPokemonOperationException {

        if (pokedex.getPokemons().isEmpty()) {
            System.out.println("  There are no Pokemon to delete.");
            return;
        }
        showPokemons(pokedex);
        backOption();
        title("Delete pokemon");
        subTitle("Which method do you want to use?");
        menuOption(1, "String");
        menuOption(2, "Index");
        separator();

        int chooseMethod = readIntBetween(
                scanner,
                1,
                2,
                "  Choose your method (%d - %d): "
        );
        separator();

        if (chooseMethod == 1) {

            while (true) {
                String pokemonToRemove = readString(
                        scanner,
                        "  Which Pokemon do you wanna delete?: ",
                        "  Pokemon "
                );
                try {
                    pokedex.removePokemon(pokemonToRemove);

                    System.out.println("  " + pokemonToRemove + " has been removed from list.");
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
}
