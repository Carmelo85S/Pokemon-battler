package org.java26.service;

import org.java26.exceptions.InvalidPokemonException;
import org.java26.exceptions.QuitPokemonOperationException;
import org.java26.models.Attack;
import org.java26.models.Pokedex;
import org.java26.models.Pokemon;

import static org.java26.consoleLayout.Layout.backOption;
import static org.java26.service.PokemonViewer.showPokemons;

public class PokemonStatsViewer {

    public static void getPokemonsStats(Pokedex pokedex) {

        if (pokedex.getPokemons().isEmpty()) {
            System.out.println("  No pokemons listed");
            return;
        }

        System.out.println(
                "+----------------+----------------+--------+--------+"
        );

        System.out.printf(
                "| %-14s | %-14s | %-6s | %-6s |%n",
                "Name", "Type", "Win", "Loss"
        );

        System.out.println(
                "+----------------+----------------+--------+--------+"
        );

        for (Pokemon pokemon : pokedex.getPokemons()) {
            System.out.printf(
                    "| %-14s | %-14s | %-6d | %-6d |%n",
                    pokemon.getName(),
                    pokemon.getType().getLabel(),
                    pokemon.getWin(),
                    pokemon.getLoss()
            );
        }

        System.out.println(
                "+----------------+----------------+--------+--------+"
        );
    }
}
