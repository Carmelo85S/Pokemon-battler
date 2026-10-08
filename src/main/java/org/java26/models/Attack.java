package org.java26.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Random;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "attackClassType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DamageAttack.class, name = "damage"),
        @JsonSubTypes.Type(value = HealAttack.class, name = "heal"),
})

public abstract class Attack {
    private String name;
    private int baseDamage;
    private int accuracy;
    private final PokemonType type;
    private final AttackClassType attackClassType;

    protected Attack(PokemonType type, AttackClassType attackClassType) {

        if (type == null) {
            throw new IllegalArgumentException(
                    "  Pokemon type cannot be null"
            );
        }
        this.type = type;

        if (attackClassType == null) {
            throw new IllegalArgumentException(
                    "  Pokemon type cannot be null"
            );
        }
        this.attackClassType = attackClassType;
    }

    protected Attack(String name, int baseDamage, int accuracy, PokemonType type, AttackClassType attackClassType) {
        setName(name);
        setBaseDamage(baseDamage);
        setAccuracy(accuracy);
        if (type == null) {
            throw new IllegalArgumentException(
                    "  Pokemon type cannot be null"
            );
        }
        this.type = type;

        if (attackClassType == null) {
            throw new IllegalArgumentException(
                    "  Pokemon type cannot be null"
            );
        }
        this.attackClassType = attackClassType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "  Invalid input. Field can't be empty"
            );
        }
        this.name = name;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public void setBaseDamage(int baseDamage) {
        if (baseDamage < 10 || baseDamage > 100) {
            throw new IllegalArgumentException(
                    "  Base damage must be between 10 and 1000"
            );
        }
        this.baseDamage = baseDamage;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        if (accuracy < 10 || accuracy > 100) {
            throw new IllegalArgumentException(
                    "  Accuracy must be between 10 and 100"
            );
        }
        this.accuracy = accuracy;
    }

    public PokemonType getType() {
        return type;
    }

    @JsonIgnore
    public AttackClassType getAttackClassType() {
        return attackClassType;
    }


    public abstract void execute(
            Pokemon attacker,
            Pokemon defender,
            boolean criticalHit,
            double effectiveness,
            Random random
    );
}
