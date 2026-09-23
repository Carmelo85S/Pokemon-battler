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

import static org.java26.methods.ActionMenu.*;
import static org.java26.methods.PokemonCRUD.*;

public class Menu {

    public static void printWelcome() {
        System.out.println("+----------------------------------------+");
        System.out.println("|            WELCOME TRAINER             |");
        System.out.println("|                                        |");
        System.out.println("|              POKEDEX APP               |");
        System.out.println("|                                        |");
        System.out.println("|   Manage your Pokemon collection,      |");
        System.out.println("|   customize their stats and attacks,   |");
        System.out.println("|   and keep your Pokedex organized.     |");
        System.out.println("|                                        |");
        System.out.println("|            by Carmelo Salis.           |");
        System.out.println("+----------------------------------------+");
        System.out.println();
    }

    public static void loadInitialPokemondata(Pokedex pokedex, JsonHandler jsonHandler) {
        try {
            Path path = Path.of("pokemon.json");

            List<Pokemon> listPokemon;

            if (Files.exists(path)) {
                String json = Files.readString(path);

                if (jsonHandler.isValidJSON(json)) {
                    listPokemon = jsonHandler.loadPokemon("pokemon.json");
                } else {
                    listPokemon = jsonHandler.loadPokemon("seed-pokemons.json");
                }
            } else {
                listPokemon = jsonHandler.loadPokemon("seed-pokemons.json");
            }

            for (Pokemon p : listPokemon) {
                pokedex.addPokemon(p);
            }

        } catch (PokemonLoadException | IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void showMenu() {
        System.out.println();
        System.out.println("+========================================+");
        System.out.println("|                POKEDEX                 |");
        System.out.println("+========================================+");
        System.out.println("|                                        |");
        System.out.println("|        [1]. Show all Pokemon           |");
        System.out.println("|        [2]. Insert a new Pokemon       |");
        System.out.println("|        [3]. Customize your Pokemon     |");
        System.out.println("|        [4]. Delete your Pokemon        |");
        System.out.println("|        [5]. Save to file               |");
        System.out.println("|        [6]. Load from file             |");
        System.out.println("|        [7]. Reset to seed data         |");
        System.out.println("|        [8]. Pokemon info               |");
        System.out.println("|        [9]. Exit                       |");
        System.out.println("|                                        |");
        System.out.println("+----------------------------------------+");
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

