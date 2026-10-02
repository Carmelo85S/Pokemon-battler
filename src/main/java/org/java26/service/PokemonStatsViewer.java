package org.java26.service;

import org.java26.exceptions.InvalidPokemonException;
import org.java26.exceptions.QuitPokemonOperationException;
import org.java26.models.Attack;
import org.java26.models.Pokedex;
import org.java26.models.Pokemon;
import org.java26.service.battle.BattleStatistics;

import static org.java26.consoleLayout.Layout.backOption;
import static org.java26.service.PokemonViewer.showPokemons;

public class PokemonStatsViewer {

    public static void getPokemonsStats(Pokedex pokedex, BattleStatistics statistics) {

        if (pokedex.getPokemons().isEmpty()) {
            System.out.println("  No pokemons listed");
            return;
        }

        System.out.println(
                "+----------------+----------------+--------+--------+--------+"
        );

        System.out.printf(
                "| %-14s | %-14s | %-6s | %-6s | %-6s |%n",
                "Name", "Type", "Win", "Loss", "Ratio"
        );

        System.out.println(
                "+----------------+----------------+--------+--------+--------+"
        );

        for (Pokemon pokemon : pokedex.getPokemons()) {
            int wins = statistics.getPokemonWins()
                    .getOrDefault(pokemon.getName(), 0);

            int losses = statistics.getPokemonLosses()
                    .getOrDefault(pokemon.getName(), 0);
            double ratio = (wins + losses) == 0
                    ? 0.0
                    : (double) wins / (wins + losses);
            System.out.printf(
                    "| %-14s | %-14s | %-6d | %-6d | %-6.2f |%n",
                    pokemon.getName(),
                    pokemon.getType().getLabel(),
                    wins,
                    losses,
                    ratio
            );
        }

        System.out.println(
                "+----------------+----------------+--------+--------+--------+"
        );
    }
}
