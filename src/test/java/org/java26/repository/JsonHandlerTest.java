package org.java26.repository;

import org.java26.exceptions.PokemonLoadException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class JsonHandlerTest {
    @Test
    void corruptedJson_ShouldThrowPokemonLoadException(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("pokemon.json");
        Files.writeString(file, "{invalid.json}");
        JsonHandler handler = new JsonHandler();

        assertThrows(
                PokemonLoadException.class, ()-> handler.loadPokemon(file.toString())
        );

    }

    @Test
    void missingJson_ShouldThrowException(@TempDir Path tempDir){
        Path file = tempDir.resolve("missing.json");
        JsonHandler handler = new JsonHandler();
        assertThrows(
                PokemonLoadException.class, () ->handler.loadPokemon(file.toString())
        );
    }

}