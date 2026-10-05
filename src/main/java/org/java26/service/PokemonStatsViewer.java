package org.java26.service;

import org.java26.exceptions.InvalidPokemonException;
import org.java26.exceptions.QuitPokemonOperationException;
import org.java26.models.Attack;
import org.java26.models.Pokedex;
import org.java26.models.Pokemon;
import org.java26.service.battle.BattleStatistics;

import java.util.HashMap;
import java.util.Map;

import static org.java26.consoleLayout.Layout.backOption;
import static org.java26.service.PokemonViewer.showPokemons;

public class PokemonStatsViewer {

    public static void getPokemonsStats(Pokedex pokedex, BattleStatistics statistics) {

        if (pokedex.getPokemons().isEmpty()) {
            System.out.println("  No pokemons listed");
            return;
        }


        System.out.println(
                "+----------------+------------+------------+--------+--------+--------+----------------+--------+"
        );

        System.out.printf(
                "| %-14s | %-10s | %-10s | %-6s | %-6s | %-6s | %-14s | %-6s | %n",
                "Name", "Caught ", "Used Times", "Win", "Loss", "Ratio", "Attack used", "Times"
        );

        System.out.println(
                "+----------------+------------+------------+--------+--------+--------+----------------+--------+"
        );

        for (Pokemon pokemon : pokedex.getPokemons()) {
            String caught = statistics.getPokemonCatch()
                    .containsKey(pokemon.getName()) ? "Yes" : "No";

            int pokemonUsedTimes = statistics.getPokemonUse()
                    .getOrDefault(pokemon.getName(), 0);

            int wins = statistics.getPokemonWins()
                    .getOrDefault(pokemon.getName(), 0);

            int losses = statistics.getPokemonLosses()
                    .getOrDefault(pokemon.getName(), 0);

            double ratio = (wins + losses) == 0
                    ? 0.0
                    : (double) wins / (wins + losses);

            HashMap<String, Integer> attacks =
                    statistics.getPokemonAttackUse()
                            .getOrDefault(pokemon.getName(), new HashMap<>());

            String mostUsedAttack = attacks.entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("-");

            int attackUsed = attacks.getOrDefault(mostUsedAttack, 0);

            System.out.printf(
                    "| %-14s | %-10s | %-10s | %-6d | %-6d | %-6.2f | %-14s | %-6d |%n",
                    pokemon.getName(),
                    caught,
                    pokemonUsedTimes,
                    wins,
                    losses,
                    ratio,
                    mostUsedAttack,
                    attackUsed
            );
        }

        System.out.println(
                "+----------------+------------+------------+--------+--------+--------+----------------+--------+"
        );
    }
}
