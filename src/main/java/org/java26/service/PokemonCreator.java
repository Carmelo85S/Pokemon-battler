package org.java26.service;

import org.java26.models.*;
import org.java26.exceptions.InvalidPokemonException;
import org.java26.exceptions.QuitPokemonOperationException;

import java.util.Scanner;

import static org.java26.UI.Layout.backOption;
import static org.java26.UI.Layout.menuOption;
import static org.java26.UI.Layout.separator;
import static org.java26.UI.Layout.subTitle;
import static org.java26.UI.Layout.title;
import static org.java26.inputHelpers.InputHelper.*;
import static org.java26.inputHelpers.InputHelper.readIntBetween;

public class PokemonCreator {
    public static void createPokemon(Scanner scanner, Pokedex pokedex) {
        // Pokemon obj
        String name;
        PokemonType type;
        int maxHp;
        int speed;

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

            speed = readIntBetween(
                    scanner,
                    10,
                    50,
                    "  Enter speed between %d and %d: "
            );
            System.out.println();

            Pokemon pokemon = new Pokemon(name, type, maxHp, speed);

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
