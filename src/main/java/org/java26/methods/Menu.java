package org.java26.methods;

import org.java26.models.Pokemon;
import org.java26.exceptions.PokemonLoadException;
import org.java26.exceptions.QuitPokemonOperationException;
import org.java26.handlers.JsonHandler;
import org.java26.models.Pokedex;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

import static org.java26.consoleLayout.Layout.*;
import static org.java26.methods.ActionMenu.*;
import static org.java26.methods.PokemonCRUD.*;

public class Menu {

    public static void printWelcome() {
        title("WELCOME TRAINER");
        separator();
        title("POKEDEX APP");
        separator();
        subTitle("Manage your Pokemon collection");
        subTitle("customize their stats and attacks,");
        subTitle("and keep your Pokedex organized");
        subTitle("by Carmelo Salis.");
        separator();
    }

    public static void loadInitialPokemonData(Pokedex pokedex, JsonHandler jsonHandler) {
        try {
            Path path = Path.of("pokemon.json");

            List<Pokemon> listPokemon;

            if (Files.exists(path)) {
                String json = Files.readString(path);

                if (!json.isBlank() && jsonHandler.isValidJSON(json)) {

                    listPokemon = jsonHandler.loadPokemon("pokemon.json");

                    if (listPokemon.isEmpty()) {
                        listPokemon = jsonHandler.loadPokemon("seed-pokemons.json");
                        title("Loading..");
                        subTitle("Seed data loaded successfully.");
                    } else {
                        title("Loading..");
                        subTitle("Data loaded successfully.");
                    }

                } else {

                    listPokemon = jsonHandler.loadPokemon("seed-pokemons.json");
                    title("Loading..");
                    subTitle("Seed data loaded successfully.");
                }
            } else {
                listPokemon = jsonHandler.loadPokemon("seed-pokemons.json");
                title("Loading..");
                subTitle("File not found, load seed data");
            }

            for (Pokemon p : listPokemon) {
                pokedex.addPokemon(p);
            }

        } catch (PokemonLoadException | IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void showMenu() {
        title("Pokedex");
        menuOption(1, "Show all Pokemon");
        menuOption(2, "Insert a new Pokemon");
        menuOption(3, "Customize your Pokemon");
        menuOption(4, "Delete your Pokemon");
        menuOption(5, "Save to file");
        menuOption(6, "Load from file");
        menuOption(7, "Reset to seed data");
        menuOption(8, "Pokemon info");
        menuOption(9, "Exit");

    }

    public static void runAction(int choice, Scanner scanner, Pokedex pokedex, JsonHandler jsonHandler) {
        try {
            switch (choice) {
                case 1 -> showAllPokemon(scanner, pokedex);
                case 2 -> insertNewPokemon(scanner, pokedex);
                case 3 -> customizePokemon(scanner, pokedex);
                case 4 -> deletePokemon(scanner, pokedex);
                case 5, 9 -> saveToFile(pokedex, jsonHandler);
                case 6 -> loadFromFile(pokedex, jsonHandler);
                case 7 -> resetToSeedData(pokedex, jsonHandler);
                case 8 -> getPokemonInfo(scanner, pokedex);
                default -> System.out.println("  Invalid input");
            }
        } catch (QuitPokemonOperationException e) {
            System.out.println(e.getMessage());
        }
    }
}

