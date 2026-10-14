package org.java26.service;

import org.java26.models.Pokedex;
import org.java26.models.Pokemon;
import org.java26.service.battle.BattleStatistics;

import java.util.HashMap;
import java.util.Map;

public class PokemonStatsViewer {

    public static void getPokemonsStats(Pokedex pokedex, BattleStatistics statistics) {

        if (pokedex.getPokemons().isEmpty()) {
            System.out.println("  No pokemons listed");
            return;
        }


        System.out.println(
                "+----------------+--------+------------+------------+--------+--------+--------+----------------+--------+----------------------+"
        );

        System.out.printf(
                "| %-14s | %-6s | %-10s | %-10s | %-6s | %-6s | %-6s | %-14s | %-6s | %-20s | %n",
                "Name", "Level", "Caught ", "Used Times", "Win", "Loss", "Ratio", "Attack used", "Times", "Last time"
        );

        System.out.println(
                "+----------------+--------+------------+------------+--------+--------+--------+----------------+--------+----------------------+"
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
            String lastUse = statistics.getPokemonLastUse()
                    .getOrDefault(pokemon.getName(), "Never");

            System.out.printf(
                    "| %-14s | %-6d | %-10s | %-10s | %-6d | %-6d | %-6.2f | %-14s | %-6d | %-20s |%n",
                    pokemon.getName(),
                    pokemon.getLevel(),
                    caught,
                    pokemonUsedTimes,
                    wins,
                    losses,
                    ratio,
                    mostUsedAttack,
                    attackUsed,
                    lastUse
            );
        }

        System.out.println(
                "+----------------+--------+------------+------------+--------+--------+--------+----------------+--------+----------------------+"
        );
    }


    public static void resetPokemonsStats(
            Pokedex pokedex,
            BattleStatistics statistics
    ) {
        statistics.setTotalWins(0);
        statistics.setTotalLosses(0);

        statistics.getPokemonWins().clear();
        statistics.getPokemonLosses().clear();
        statistics.getPokemonCatch().clear();
        statistics.getPokemonUse().clear();
        statistics.getPokemonAttackUse().clear();
        statistics.getPokemonLastUse().clear();

        System.out.println("  Pokemon statistics reset successfully.");
    }

}
