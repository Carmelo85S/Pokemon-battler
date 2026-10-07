package org.java26.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.java26.service.battle.BattleLogic;

import java.util.Random;

public class DamageAttack extends Attack {

    private final int power;

    @JsonCreator
    public DamageAttack(
            @JsonProperty("name") String name,
            @JsonProperty("baseDamage") int baseDamage,
            @JsonProperty("accuracy") int accuracy,
            @JsonProperty("type") PokemonType type

    ) {
        super(name, baseDamage, accuracy, type, AttackClassType.DAMAGE);
        this.power = baseDamage;
    }

    public DamageAttack(
            String name,
            int baseDamage,
            int accuracy,
            PokemonType type,
            int power
    ) {
        super(name, baseDamage, accuracy, type, AttackClassType.DAMAGE
        );
        this.power = power;
    }

    @Override
    public void execute(Pokemon attacker, Pokemon defender, boolean criticalHit) {

        double effectiveness = BattleLogic.effectiveness(defender, this);
        double randomFactor = 0.85 + new Random().nextDouble() * 0.15;
        double damage = getBaseDamage() * randomFactor * effectiveness;

        if (criticalHit) {
            damage *= 2;
        }
        defender.takeDamage((int) damage);
    }
}