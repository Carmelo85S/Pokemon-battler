package org.java26.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.Random;
@Entity
@DiscriminatorValue("DAMAGE")
public class DamageAttack extends Attack {

    protected DamageAttack() {
        super();
    }

    @JsonCreator
    public DamageAttack(
            @JsonProperty("name") String name,
            @JsonProperty("power") int power,
            @JsonProperty("accuracy") int accuracy,
            @JsonProperty("type") PokemonType type

    ) {
        super(name, power, accuracy, type, AttackClassType.DAMAGE);
    }


    @Override
    public void execute(Pokemon attacker, Pokemon defender, boolean criticalHit, double effectiveness, Random random) {

        double randomFactor = 0.85 + random.nextDouble() * 0.15;
        double damage = getPower() * randomFactor * effectiveness;

        if (criticalHit) {
            damage *= 2;
        }
        defender.takeDamage((int) damage);
    }
}