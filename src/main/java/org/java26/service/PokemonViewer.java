package org.java26.service;

import org.java26.exceptions.InvalidPokemonException;
import org.java26.exceptions.QuitPokemonOperationException;
import org.java26.models.Attack;
import org.java26.models.Pokedex;
import org.java26.models.Pokemon;

import java.util.Scanner;

import static org.java26.consoleLayout.Layout.*;
import static org.java26.consoleLayout.Layout.separator;
import static org.java26.consoleLayout.Layout.subTitle;
import static org.java26.inputHelpers.InputHelper.readIntBetween;
import static org.java26.inputHelpers.InputHelper.readString;
import static org.java26.service.PokemonCreator.createPokemon;

public class PokemonViewer {
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
                separator();
                title(pokemon.getName());
                System.out.println("+----------------+----------------+----------------+----------------+----------------+");
                System.out.printf(
                        "| %-14s | %-14s | %-14s | %-14s | %-14s |%n",
                        "Name", "Level", "Type", "Max HP", "Current HP"
                );
                System.out.println("+----------------+--------------------------------++----------------+----------------+");

                System.out.printf(
                        "| %-14s | %-14d | %-14s | %-14d | %-14d |%n",
                        pokemon.getName(),
                        pokemon.getLevel(),
                        pokemon.getType(),
                        pokemon.getMaxHp(),
                        pokemon.getCurrentHp()
                );

                System.out.println("+----------------+--------------------------------++----------------+----------------+");

                // Attack information
                separator();
                title("ATTACKS");
                System.out.println("+----------------+----------------+----------------+----------------+----------------+");
                System.out.printf(
                        "| %-14s | %-14s | %-14s | %-14s | %-14s | %-14s |%n",
                        "Name", "Damage", "Heal", "Accuracy", "Type", "Category"
                );
                System.out.println("+----------------+----------------+----------------+----------------+----------------+");

                for (Attack attack : pokemon.getAttacks()) {
                    System.out.printf(
                            "| %-14s | %-14d | %-14d | %-14d | %-14s | %-14s |%n",
                            attack.getName(),
                            attack.getBaseDamage(),
                            attack.getHeal(),
                            attack.getAccuracy(),
                            attack.getType().getLabel(),
                            attack.getAttackClassType().getLabel()
                    );
                }

                System.out.println("+----------------+----------------+----------------+----------------+----------------+");

            } catch (InvalidPokemonException e) {
                System.out.println(e.getMessage());
            } catch (QuitPokemonOperationException e) {
                System.out.println(e.getMessage());
                return;
            }
        }
    }

    public static void showPokemons(Pokedex pokedex) {

        if (pokedex.getPokemons().isEmpty()) {
            separator();
            title("Pokedex empty");
            subTitle("Nothing to show. ");
            return;
        }

        System.out.println();

        System.out.println("+----------------+---------+---------------+");
        System.out.println("|              AVAILABLE POKEMONS          |");
        System.out.println("+----------------+---------+---------------+");
        System.out.printf(
                "| %-14s | %-6s | %-14s |%n",
                "Name", "Level", "Type"
        );

        System.out.println(
                "+----------------+---------+---------------+"
        );
        for (Pokemon p : pokedex.getPokemons()) {
            System.out.printf(
                    "| %-14s | %-6s | %-14s |%n",
                    p.getName(),
                    p.getLevel(),
                    p.getType().getLabel()
            );
        }
        System.out.println(
                "+----------------+---------+---------------+"
        );

    }

    public static void showAllPokemon(Scanner scanner, Pokedex pokedex) {

        if (pokedex.getPokemons().isEmpty()) {
            separator();
            title("Pokedex empty");
            subTitle("Nothing to show. ");
            subTitle("Do you want to create");
            subTitle("a pokemon?");
            menuOption(1, "yes");
            menuOption(2, "No");
            separator();

            int choice = readIntBetween(
                    scanner,
                    1,
                    2,
                    "  Select an option %d or %d: "
            );
            System.out.println();
            if (choice == 1) {
                createPokemon(scanner, pokedex);
            } else {
                return;
            }
        }

        showPokemons(pokedex);
    }


}
