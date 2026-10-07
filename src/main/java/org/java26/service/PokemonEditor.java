package org.java26.service;

import org.java26.exceptions.InvalidPokemonException;
import org.java26.exceptions.QuitPokemonOperationException;
import org.java26.models.Attack;
import org.java26.models.DamageAttack;
import org.java26.models.Pokedex;
import org.java26.models.Pokemon;

import java.util.Locale;
import java.util.Scanner;

import static org.java26.consoleLayout.Layout.*;
import static org.java26.consoleLayout.Layout.menuOption;
import static org.java26.consoleLayout.Layout.separator;
import static org.java26.inputHelpers.InputHelper.*;
import static org.java26.service.PokemonViewer.showPokemons;

public class PokemonEditor {
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
            title("CUSTOMIZE " + pokemon.getName().toUpperCase(Locale.ROOT));
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
                        pokemon.setName(readString(scanner, "  Enter new name: ", "  New name is "));
                        System.out.println();

                    } catch (QuitPokemonOperationException e) {
                        System.out.println(e.getMessage());
                    }
                }

                case 2 -> {
                    try {
                        pokemon.setType(readType(scanner, "  Enter new type: "));
                        System.out.println();
                    } catch (QuitPokemonOperationException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 3 -> {
                    try {
                        pokemon.setMaxHp(readIntBetween(scanner, 200, 300, "  Enter new hp value: "));
                        if (pokemon.getCurrentHp() > pokemon.getMaxHp()) {
                            pokemon.setCurrentHp(pokemon.getMaxHp());
                        }

                        System.out.println();

                    } catch (QuitPokemonOperationException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 4 -> {
                        try {
                            if (pokemon.hasMaxAttacks()) {
                                System.out.println("  Attack slots are already full.");
                                break;
                            }

                            while (!pokemon.hasMaxAttacks()) {

                                Attack newAttack = AttackCreator.createAttack(
                                        scanner,
                                        pokemon.getType()
                                );

                                pokemon.addAttack(newAttack);

                                if (pokemon.hasMaxAttacks()) {
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

                    for (Attack attack : pokemon.getAttacks()) {
                        System.out.println("  - " + attack.getName());
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

                            for (int i = 0; i < pokemon.getAttacks().size(); i++) {
                                if (pokemon.getAttacks().get(i).getName().equalsIgnoreCase(attackToRemove)) {
                                    pokemon.getAttacks().remove(i);
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
