package org.java26.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java26.models.Pokemon;
import org.java26.exceptions.PokemonLoadException;

import java.io.File;
import java.io.IOException;
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
            e.printStackTrace();

            throw new PokemonLoadException(

                    "  Could not load Pokemon from file " + filePath);
        }
    }

}
