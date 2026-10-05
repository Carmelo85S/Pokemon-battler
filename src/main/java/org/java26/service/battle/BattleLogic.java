package org.java26.service.battle;

import org.java26.models.Attack;
import org.java26.models.Pokemon;
import org.java26.models.PokemonType;

import java.util.Random;

public class BattleLogic {
    static int calculateDamage(Pokemon pokemon, Attack attack, Random random) {
        double effect = effectiveness(pokemon, attack);
        double randomFactor = 0.85 + random.nextDouble() * 0.15;
        return (int) (
                attack.getBaseDamage()
                        * effect
                        * randomFactor
        );
    }

    public static double effectiveness(Pokemon defender, Attack attack) {
        PokemonType attackType = attack.getType();
        PokemonType opponentType = defender.getType();

        if (attackType == PokemonType.FIRE && opponentType == PokemonType.GRASS) {
            return 2.0;
        }

        if (attackType == PokemonType.WATER && opponentType == PokemonType.FIRE) {
            return 2.0;
        }

        if (attackType == PokemonType.GRASS && opponentType == PokemonType.WATER) {
            return 2.0;
        }

        if (attackType == PokemonType.ELECTRIC && opponentType == PokemonType.WATER) {
            return 2.0;
        }

        if (attackType == PokemonType.FIRE && opponentType == PokemonType.WATER) {
            return 0.5;
        }

        if (attackType == PokemonType.WATER && opponentType == PokemonType.GRASS) {
            return 0.5;
        }

        if (attackType == PokemonType.GRASS && opponentType == PokemonType.FIRE) {
            return 0.5;
        }

        if (attackType == PokemonType.ELECTRIC && opponentType == PokemonType.GRASS) {
            return 0.5;
        }

        return 1.0;
    }

    static Attack chooseRandomAttack(Pokemon pokemon, Random random) {
        int randomAttack = random.nextInt(pokemon.getAttacks().size());
        return pokemon.getAttacks().get(randomAttack);
    }


}
