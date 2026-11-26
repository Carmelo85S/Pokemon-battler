package org.java26.repository;

import org.java26.models.Pokemon;
import org.java26.exceptions.*;
import org.java26.models.Pokedex;
import org.java26.repository.battleLogRepository.BattleLogRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.java26.UI.Layout.*;

public class SaveLoadPokemons {

    public static List<Pokemon> loadWildPokemons(
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

    public static void loadFromFile(Pokedex pokedex, PokemonRepositoryImpl impl) {
        try {
            List<Pokemon> pokemons = impl.findAll();
            pokedex.removeAllPokemon();
            for (Pokemon p : pokemons) {
                pokedex.addPokemon(p);
            }
            System.out.println("  Pokemons loaded successfully.");
        } catch (PokemonLoadException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void loadInitialPokemonData(Pokedex pokedex, JsonHandler jsonHandler, PokemonRepositoryImpl impl) {

        List<Pokemon> isPresent = impl.findAll();
        if (!isPresent.isEmpty()) {
            for (Pokemon p : isPresent) {
                pokedex.addPokemon(p);
            }
            return;
        }
        try {
           List<Pokemon> seedPokemon = jsonHandler.loadPokemon("seed-pokemons.json");
           for(Pokemon p : seedPokemon){
               impl.save(p);
               pokedex.addPokemon(p);
           }
           title("Loading");
           subTitle("Seed data loaded successfully");
        } catch (PokemonLoadException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void resetToSeedData(
            Pokedex pokedex,
            PokemonRepositoryImpl impl,
            BattleLogRepository blr,
            JsonHandler jsonHandler
    ) {
        try {
            // Load and validate seed data before deleting anything
            List<Pokemon> pokemons =
                    jsonHandler.loadPokemon("seed-pokemons.json");

            if (pokemons.isEmpty()) {
                System.out.println("  Seed data is empty. Reset cancelled.");
                return;
            }

            // Reset PostgreSQL
            impl.deleteAll();

            for (Pokemon pokemon : pokemons) {
                impl.save(pokemon);
            }

            // Reset MongoDB battle logs
            blr.deleteAll();

            // Refresh in-memory Pokedex from PostgreSQL
            pokedex.removeAllPokemon();

            for (Pokemon pokemon : impl.findAll()) {
                pokedex.addPokemon(pokemon);
            }

            System.out.println(
                    "  Pokemons and battle statistics reset successfully."
            );

        } catch (PokemonLoadException e) {
            System.out.println("  Reset failed: " + e.getMessage());
        }
    }}

