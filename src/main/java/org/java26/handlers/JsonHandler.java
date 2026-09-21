package org.java26.handlers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java26.models.Pokemon;
import org.java26.exceptions.PokemonLoadException;
import org.java26.exceptions.PokemonSaveException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonHandler {
    ObjectMapper mapper = new ObjectMapper();

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
        try{
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), pokemons);
        }catch (IOException e){
            throw new PokemonSaveException(
                    "  Could not save Pokemon to file: " + filePath);
        }
    }
}
