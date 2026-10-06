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
        super(name, baseDamage, accuracy, type, AttackClassType.HEAL);
        this.heal = heal;
    }

    public int getHeal() {
        return heal;
    }

    @Override
    public void execute(
            Pokemon attacker,
            Pokemon defender,
            boolean criticalHit
    ) {
        System.out.println("DEBUG heal = " + heal);
        attacker.heal(heal);
    }
}