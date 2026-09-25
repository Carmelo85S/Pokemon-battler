package org.java26.methods;
import org.java26.models.Pokemon;
import org.java26.exceptions.*;
import org.java26.handlers.JsonHandler;
import org.java26.models.Pokedex;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

import static org.java26.consoleLayout.Layout.*;
import static org.java26.inputHelpers.InputHelper.*;
import static org.java26.methods.PokemonCRUD.insertNewPokemon;

public class ActionMenu {

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
                insertNewPokemon(scanner, pokedex);
            } else {
                return;
            }
        }

        showPokemons(pokedex);
    }

    public static void showPokemons(Pokedex pokedex) {

        if (pokedex.getPokemons().isEmpty()) {
            separator();
            title("Pokedex empty");
            subTitle("Nothing to show. ");
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
            Path path = Path.of("pokemon.json");
            if (!Files.exists(path)) {
                System.out.println("  File pokemon.json does not exist.");
                return;
            }
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
        try {
            List<Pokemon> pokemons = jsonHandler.loadPokemon("seed-pokemons.json");
            pokedex.removeAllPokemon();

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

}

