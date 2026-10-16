package org.java26.service;

import org.java26.models.Attack;
import org.java26.models.Pokedex;
import org.java26.models.Pokemon;
import org.java26.repository.JsonHandler;
import org.java26.service.battle.BattleStatistics;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import static org.java26.UI.Layout.subTitle;
import static org.java26.repository.SaveLoadPokemons.saveToFile;

public class StatisticsService {
    public static void recordWin(BattleStatistics statistics, Pokemon pokemon) {
        statistics.setTotalWins(statistics.getTotalWins() + 1);

        String name = pokemon.getName();
        statistics.getPokemonWins().put(name, statistics.getPokemonWins().getOrDefault(name, 0) + 1);
    }

    public static void recordLoss(BattleStatistics statistics, Pokemon pokemon) {
        statistics.setTotalLosses(statistics.getTotalLosses() + 1);

        String name = pokemon.getName();
        statistics.getPokemonLosses().put(name, statistics.getPokemonLosses().getOrDefault(name, 0) + 1);
    }

    public static void recordPokemonUse(BattleStatistics statistics, Pokemon pokemon) {
        String name = pokemon.getName();

        statistics.getPokemonUse().put(
                name, statistics.getPokemonUse().getOrDefault(name, 0) + 1
        );
    }


    public static void recordPokemonAttackUse(
            BattleStatistics statistics,
            Pokemon pokemon,
            Attack attack
    ) {
        String pokemonName = pokemon.getName();
        String attackName = attack.getName();

        statistics.getPokemonAttackUse()
                .computeIfAbsent(pokemonName, key -> new HashMap<>())
                .merge(attackName, 1, Integer::sum);
    }

    public static void recordPokemonCatch(
            BattleStatistics statistics,
            Pokemon pokemon
    ) {
        statistics.getPokemonCatch()
                .merge(pokemon.getName(), 1, Integer::sum);
    }

    public static void resetAllStatistic(Path path, Pokedex pokedex, BattleStatistics statistics, JsonHandler jsonHandler) {
        boolean level = pokedex.getPokemons()
                .stream()
                .allMatch(p -> p.getLevel() == 1);

        if (!Files.exists(path)) {
            subTitle("File doesn't exist. Play battles and save your Pokemon progress");
        } else if (statistics.isEmpty() && level) {
            subTitle("Nothing to reset.");
        } else {
            statistics.resetStatistics();

            for (Pokemon pokemon : pokedex.getPokemons()) {
                pokemon.resetLevel();
            }

            subTitle("All statistics reset to default values.");
            jsonHandler.saveStatistics(path.toString(), statistics);
            saveToFile(pokedex, jsonHandler);
        }
    }

}
