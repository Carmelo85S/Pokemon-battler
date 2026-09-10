package org.java26;

import java.util.ArrayList;

public class Pokedex  {
    private ArrayList<Pokemon> pokemons = new ArrayList<>();

    public void addPokemon(Pokemon pokemon) {
        pokemons.add(pokemon);
    }

    public Pokemon getPokemon(String choice){
        for(Pokemon pokemon : pokemons){
            if(pokemon.name.equalsIgnoreCase(choice)){
                return pokemon;
            }
        }return null;
    }

    public ArrayList<Pokemon> getPokemons() {
        return pokemons;
    }
}
