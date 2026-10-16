package org.java26.service.battle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class BattleStatistics {

    public int totalWins;
    private int totalLosses;

    private HashMap<String, Integer> pokemonWins = new HashMap<>();
    private HashMap<String, Integer> pokemonLosses = new HashMap<>();
    private HashMap<String, Integer> pokemonUse = new HashMap<>();
    private HashMap<String, Integer> pokemonCatch = new HashMap<>();
    private HashMap<String, String> pokemonLastUse = new HashMap<>();

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public void trackPokemon(String pokemon){
        String localeDateTime = LocalDateTime.now().format(formatter);
        pokemonLastUse.put(pokemon, localeDateTime);
    }

    private HashMap<String, HashMap<String, Integer>> pokemonAttackUse = new HashMap<>();

    public int getTotalWins() {
        return totalWins;
    }

    public HashMap<String, String> getPokemonLastUse() {
        return pokemonLastUse;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    public int getTotalLosses() {
        return totalLosses;
    }

    public void setTotalLosses(int totalLosses) {
        this.totalLosses = totalLosses;
    }

    public HashMap<String, Integer> getPokemonWins() {
        return pokemonWins;
    }

    public HashMap<String, Integer> getPokemonLosses() {
        return pokemonLosses;
    }

    public HashMap<String, Integer> getPokemonUse() {
        return pokemonUse;
    }

    public HashMap<String, HashMap<String, Integer>> getPokemonAttackUse() {
        return pokemonAttackUse;
    }

    public HashMap<String, Integer> getPokemonCatch() {
        return pokemonCatch;
    }

    public HashMap<String, String> getLastTime(){
        return pokemonLastUse;
    }

    public void resetStatistics() {
        totalWins = 0;
        totalLosses = 0;

        pokemonWins.clear();
        pokemonLosses.clear();
        pokemonUse.clear();
        pokemonCatch.clear();
        pokemonLastUse.clear();
        pokemonAttackUse.clear();
    }

    public boolean isEmpty(){
        return totalWins == 0
                && totalLosses == 0
                && pokemonWins.isEmpty()
                && pokemonLosses.isEmpty()
                && pokemonUse.isEmpty()
                && pokemonCatch.isEmpty()
                && pokemonLastUse.isEmpty()
                && pokemonAttackUse.isEmpty();
    }
}