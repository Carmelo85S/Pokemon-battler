package org.java26.service;

import org.java26.models.Attack;
import org.java26.models.Pokemon;
import org.java26.service.battle.BattleStatistics;
import java.util.HashMap;
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

}
