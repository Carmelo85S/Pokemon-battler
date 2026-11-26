
package org.java26.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.java26.exceptions.PokemonLoadException;
import org.java26.models.Attack;
import org.java26.models.DamageAttack;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class JsonHandlerTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void corruptedJson_ShouldThrowPokemonLoadException(
            @TempDir Path tempDir) throws IOException {

        Path file = tempDir.resolve("pokemon.json");
        Files.writeString(file, "{invalid.json}");

        JsonHandler handler = new JsonHandler();

        assertThrows(
                PokemonLoadException.class,
                () -> handler.loadPokemon(file.toString())
        );
    }

    @Test
    void missingJson_ShouldThrowPokemonLoadException(
            @TempDir Path tempDir) {

        Path file = tempDir.resolve("missing.json");

        JsonHandler handler = new JsonHandler();

        assertThrows(
                PokemonLoadException.class,
                () -> handler.loadPokemon(file.toString())
        );
    }

    @Test
    void damageAttack_ShouldDeserializeCorrectly() throws IOException {

        String json = """
                {
                    "attackClassType": "damage",
                    "name": "Thunder Shock",
                    "power": 40,
                    "accuracy": 95,
                    "type": "ELECTRIC"
                }
                """;

        Attack attack = mapper.readValue(json, Attack.class);

        assertInstanceOf(DamageAttack.class, attack);
        assertEquals("Thunder Shock", attack.getName());
    }
}
