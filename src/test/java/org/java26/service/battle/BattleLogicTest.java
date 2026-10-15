package org.java26.service.battle;

import org.java26.models.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;
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
        opponent.setMaxHp(200);
        opponent.setCurrentHp(200);

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

    @Test
    void pokemonWith_0Attacks_shouldNotStart_TheBattle() {
        Pokemon player = new Pokemon();
        Pokemon opponent = new Pokemon();

        player.setMaxHp(200);
        player.setCurrentHp(0);

        opponent.setMaxHp(200);
        opponent.setCurrentHp(200);

        List<Attack> attack = new ArrayList<>();
        player.setAttacks(attack);
        Attack opponentAttack = new DamageAttack(
                "TestAttack",
                50,
                100,
                PokemonType.FIRE
        );
        opponent.addAttack(opponentAttack);

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


    @ParameterizedTest
    @CsvSource({
            "1, false",
            "2, false",
            "3, true",
            "4, false",
            "5, false",
            "6, true",
            "7, false",
            "8, false",
            "9, true"
    })
    void evolve_EveryThree_Wins(int wins, boolean isEvolving) {
        Pokemon pokemon = new Pokemon();
        BattleStatistics statistics = new BattleStatistics();
        statistics.getPokemonWins().put(pokemon.getName(), wins);

        boolean evolve = BattleLogic.evolvePokemon(
                pokemon,
                statistics
        );

        assertEquals(evolve, isEvolving);
    }

    @Test
    void emptyPokedex_shouldNotStartBattle() {

        Pokedex pokedex = new Pokedex();
        BattleStatistics statistics = new BattleStatistics();
        Scanner scanner = new Scanner("2\n");

        Pokemon result = BattleLogic.preparePlayerPokemon(scanner, pokedex, statistics);
        assertNull(result);
        assertTrue(pokedex.getPokemons().isEmpty());
    }

    @Test
    void cancelInputDuringBattle_shouldStopBattle() {
        Pokemon player = new Pokemon();
        player.setName("Player");
        player.setMaxHp(200);
        player.setCurrentHp(200);
        player.setSpeed(50);
        player.addAttack(
                new DamageAttack("Thunder", 50, 100, PokemonType.ELECTRIC)
        );

        Pokemon opponent = new Pokemon();
        opponent.setName("Opponent");
        opponent.setMaxHp(200);
        opponent.setCurrentHp(200);
        opponent.setSpeed(30);
        opponent.addAttack(
                new DamageAttack("Thunder", 50, 100, PokemonType.ELECTRIC)
        );

        Scanner scanner = new Scanner("B\n");
        BattleStatistics statistics = new BattleStatistics();

        Random random = new Random() {
            @Override
            public int nextInt(int bound) {
                return 0;
            }
        };

        boolean result = BattleLogic.runBattle(
                scanner,
                player,
                opponent,
                random,
                statistics,
                0,
                0
        );

        assertFalse(result);
        assertEquals(200, player.getCurrentHp());
        assertEquals(200, opponent.getCurrentHp());
    }

}