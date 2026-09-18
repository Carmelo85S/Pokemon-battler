package org.java26;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
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
        } catch (Exception e) {
            throw new PokemonLoadException(
                    "  Could not load Pokemon from file " + filePath);

        }
    }

    public void savePokemon(String filePath, List<Pokemon> pokemons) throws PokemonSaveException {
        try{
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), pokemons);
        }catch (Exception e){
            throw new PokemonSaveException(
                    "  Could not save Pokemon to file: " + filePath);
        }
    }
}
