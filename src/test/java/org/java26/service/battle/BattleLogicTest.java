package org.java26.service.battle;

import org.java26.models.Pokemon;
import org.java26.models.PokemonType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Random;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

class BattleLogicTest {

    @ParameterizedTest
    @CsvSource({
            "FIRE, GRASS, 2.0",
            "WATER, FIRE, 2.0",
            "GRASS, WATER, 2.0",
            "ELECTRIC, WATER, 2.0",
            "FIRE, WATER, 0.5",
            "WATER, GRASS, 0.5",
            "GRASS, FIRE, 0.5",
            "ELECTRIC, GRASS, 0.5",
            "NORMAL, FIRE, 1.0"
    })
    void test_CalculateEffectiveness(
            PokemonType attack,
            PokemonType defender,
            double expectedEffect) {

        double actualEffect =
                BattleLogic.calculateEffectiveness(attack, defender);

        assertEquals(expectedEffect, actualEffect, 0.001);
    }

    @Test
    void pokemonWith_0Hp_shouldNotStart_TheBattle() {
        Pokemon player = new Pokemon();
        Pokemon opponent = new Pokemon();

        player.setCurrentHp(0);

        Scanner scanner = new Scanner("");
        Random random = new Random(1);
        BattleStatistics statistics = new BattleStatistics();

        boolean result =
                BattleLogic.runBattle(
                        scanner,
                        player,
                        opponent,
                        random,
                        statistics,
                        0,
                        0
                );
        assertFalse(result);
    }


}