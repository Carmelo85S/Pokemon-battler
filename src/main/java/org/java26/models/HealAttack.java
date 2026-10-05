package org.java26.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HealAttack extends Attack {

    private final int heal;

    @JsonCreator
    public HealAttack(
            @JsonProperty("name") String name,
            @JsonProperty("baseDamage") int baseDamage,
            @JsonProperty("accuracy") int accuracy,
            @JsonProperty("type") PokemonType type,
            @JsonProperty("heal") int heal
    ) {
        super(name, baseDamage, accuracy, type);
        this.heal = heal;
    }


    @Override
    public void execute(
            Pokemon attacker,
            Pokemon defender,
            boolean criticalHit
    ) {
        attacker.heal(heal);

    }

}