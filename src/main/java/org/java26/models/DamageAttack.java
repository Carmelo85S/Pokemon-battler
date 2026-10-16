package org.java26.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Random;

public class DamageAttack extends Attack {


    @JsonCreator
    public DamageAttack(
            @JsonProperty("name") String name,
            @JsonProperty("baseDamage") int baseDamage,
            @JsonProperty("accuracy") int accuracy,
            @JsonProperty("type") PokemonType type

    ) {
        super(name, baseDamage, accuracy, type, AttackClassType.DAMAGE);
    }

    @Override
    public void execute(Pokemon attacker, Pokemon defender, boolean criticalHit, double effectiveness, Random random) {

        double randomFactor = 0.85 + random.nextDouble() * 0.15;
        double damage = getBaseDamage() * randomFactor * effectiveness;

        if (criticalHit) {
            damage *= 2;
        }
        defender.takeDamage((int) damage);
    }
}