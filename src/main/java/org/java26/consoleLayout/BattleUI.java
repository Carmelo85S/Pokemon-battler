package org.java26.consoleLayout;

import org.java26.exceptions.InvalidPokemonException;
import org.java26.models.Attack;
import org.java26.models.Pokedex;
import org.java26.models.Pokemon;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static org.java26.consoleLayout.Layout.*;
import static org.java26.consoleLayout.Layout.menuOption;
import static org.java26.inputHelpers.InputHelper.readIntBetween;
import static org.java26.inputHelpers.InputHelper.readString;
import static org.java26.service.PokemonCreator.createPokemon;

public class BattleUI {
    public static Attack chooseAttack(Scanner scanner, Pokemon pokemon) {
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

    public static Pokemon chooseMyPokemon(Scanner scanner, Pokedex pokedex) {

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

    public static Pokemon chooseOpponent(Random random, List<Pokemon> wildPokemons) {
        int randomOpponent = random.nextInt(wildPokemons.size());
        return wildPokemons.get(randomOpponent);
    }



}
