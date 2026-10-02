package org.java26.service;

import org.java26.models.PokemonType;
import org.java26.exceptions.InvalidPokemonException;
import org.java26.exceptions.QuitPokemonOperationException;
import org.java26.models.Attack;
import org.java26.models.Pokedex;
import org.java26.models.Pokemon;

import java.util.Scanner;

import static org.java26.consoleLayout.Layout.backOption;
import static org.java26.consoleLayout.Layout.menuOption;
import static org.java26.consoleLayout.Layout.separator;
import static org.java26.consoleLayout.Layout.subTitle;
import static org.java26.consoleLayout.Layout.title;
import static org.java26.inputHelpers.InputHelper.*;
import static org.java26.inputHelpers.InputHelper.readIntBetween;

public class PokemonCreator {
    public static void createPokemon(Scanner scanner, Pokedex pokedex) {
        // Pokemon obj
        String name;
        PokemonType type;
        int maxHp;

        // Attack obj
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
                    200,
                    300,
                    "  Enter max HP between %d and %d: "
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


            Pokemon pokemon = new Pokemon(name, type, maxHp);


            boolean addAttack = true;

            while (attackCount < 4 && addAttack) {

                Attack attack = AttackCreator.createAttack(scanner, type);

                pokemon.addAttack(attack);

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

            System.out.println("  Pokemon '" + pokemon.getName() + "' created successfully.");

        } catch (InvalidPokemonException | QuitPokemonOperationException e) {
            System.out.println(e.getMessage());

        }
    }
}
