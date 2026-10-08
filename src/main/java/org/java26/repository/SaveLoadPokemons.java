package org.java26.repository;

import org.java26.models.Pokemon;
import org.java26.exceptions.*;
import org.java26.handlers.JsonHandler;
import org.java26.models.Pokedex;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.java26.UI.Layout.*;

public class SaveLoadPokemons {

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

    public static List<Pokemon> loadPokemonList(
            JsonHandler jsonHandler,
            String filename) {
        try {
            Path path = Path.of(filename);
            if (!Files.exists(path)) {
                System.out.println("  File " + filename + " does not exist.");
                return List.of();
            }
            return jsonHandler.loadPokemon(filename);
        } catch (PokemonLoadException e) {
            System.out.println(e.getMessage());
            return List.of();
        }
    }

    public static void loadFromFile(Pokedex pokedex, JsonHandler jsonHandler, String filePath) {
        try {
            List<Pokemon> pokemons = jsonHandler.loadPokemon(filePath);
            pokedex.removeAllPokemon();
            for (Pokemon p : pokemons) {
                pokedex.addPokemon(p);
            }
            System.out.println("  Pokemons loaded successfully.");
        } catch (PokemonLoadException e) {
            System.out.println(e.getMessage());
        }
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
                subTitle("No saved data, load seed data");
            }

            for (Pokemon p : listPokemon) {
                pokedex.addPokemon(p);
            }

        } catch (PokemonLoadException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void resetToSeedData(Pokedex pokedex, JsonHandler jsonHandler) {
        try{
            boolean delete = Files.deleteIfExists(Path.of("statistic.json"));
        } catch (IOException e){
            System.out.println("  File for statistic does not exist. A file will be created after a battle.");
        }

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

