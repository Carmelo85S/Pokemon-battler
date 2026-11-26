package org.java26.models;

import org.java26.exceptions.InvalidPokemonException;

import java.util.ArrayList;

public class Pokedex {

    private ArrayList<Pokemon> pokemons = new ArrayList<>();

    public void addPokemon(Pokemon pokemon) {
        pokemons.add(pokemon);
    }

    public Pokemon getPokemon(String choice) {
        for (Pokemon pokemon : pokemons) {
            if (pokemon.getName().equalsIgnoreCase(choice)) {
                return pokemon;
            }
        }
        throw new InvalidPokemonException(
                "  Pokemon '" + choice + "' not found."
        );
    }

    public void removePokemon(String choice) {
        Pokemon pokemon = getPokemon(choice);
        pokemons.remove(pokemon);
    }

    public void removePokemonById(Long id) {
        Pokemon pokemon = pokemons.stream()
                .filter(p -> p.getId()
                        .equals(id))
                .findFirst().orElseThrow(() -> new InvalidPokemonException(
                        "  Pokemon with ID [" + id + "] not found."
                ));

        pokemons.remove(pokemon);
    }

    public void removeAllPokemon() {
        pokemons.clear();
    }

    public ArrayList<Pokemon> getPokemons() {
        return pokemons;
    }

    public void updatePokemon(Pokemon updatedPokemon) {
        for (int i = 0; i < pokemons.size(); i++) {
            if (pokemons.get(i).getId().equals(updatedPokemon.getId())) {
                pokemons.set(i, updatedPokemon);
                return;
            }
        }
    }
}
