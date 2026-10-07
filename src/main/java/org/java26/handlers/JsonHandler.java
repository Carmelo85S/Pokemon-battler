package org.java26.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java26.models.Pokemon;
import org.java26.exceptions.PokemonLoadException;
import org.java26.exceptions.PokemonSaveException;
import org.java26.service.battle.BattleStatistics;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class JsonHandler {
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Pokemon> loadPokemon(String filePath) throws PokemonLoadException {
        try {
            return mapper.readValue(
                    new File(filePath),
                    new TypeReference<List<Pokemon>>() {
                    }
            );
        } catch (IOException e) {
            throw new PokemonLoadException(

                    "  Could not load Pokemon from file " + filePath);
        }
    }

    public void savePokemon(String filePath, List<Pokemon> pokemons) throws PokemonSaveException {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), pokemons);
        } catch (IOException e) {
            throw new PokemonSaveException(
                    "  Could not save Pokemon to file: " + filePath);
        }
    }

    public void saveStatistics(String filePath, BattleStatistics statistics) throws PokemonSaveException {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), statistics);
        } catch (IOException e) {
            throw new PokemonSaveException(
                    "  Could not save statistics to file: " + filePath);
        }
    }

    public BattleStatistics loadStatistics(String filePath) throws IOException {
        Path path = Path.of(filePath);

        if (!Files.exists(path)) {
            return new BattleStatistics();
        }


        String json = Files.readString(path);

        if (json.isBlank()) {
            System.out.println("  Json file is empty.");
            return new BattleStatistics();
        }

        try {
            return mapper.readValue(json, BattleStatistics.class);

        } catch (IOException e) {
            System.out.println(
                    "  Could not load statistics from file: " + filePath
            );
            return new BattleStatistics();
        }
    }

    public boolean isValidJSON(final String json) {
        try {
            mapper.readTree(json);
            return true;

        } catch (JsonProcessingException e) {
            System.out.println("  File is corrupted.");
            System.out.println();
            return false;
        }
    }
}
