package org.java26.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DamageAttack extends Attack {

    private final int power;

    @JsonCreator
    public DamageAttack(
            @JsonProperty("name") String name,
            @JsonProperty("baseDamage") int baseDamage,
            @JsonProperty("accuracy") int accuracy,
            @JsonProperty("type") PokemonType type
    ) {
        super(name, baseDamage, accuracy, type);
        this.power = baseDamage;
    }

    public DamageAttack(
            String name,
            int baseDamage,
            int accuracy,
            PokemonType type,
            int power
    ) {
        super(name, baseDamage, accuracy, type);
        this.power = power;
    }

    @Override
    public void execute(Pokemon attacker, Pokemon defender) {
        defender.takeDamage(power);
    }
}