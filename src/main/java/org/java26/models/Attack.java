package org.java26.models;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "attackClassType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DamageAttack.class, name = "damage"),
       // @JsonSubTypes.Type(value = HealAttack.class, name = "heal"),
       // @JsonSubTypes.Type(value = StatusAttack.class, name = "status")
})

public abstract class Attack {
    private String name;
    private int baseDamage;
    private int accuracy;
    private PokemonType type;

    protected Attack() {
    }

    protected Attack(String name, int baseDamage, int accuracy, PokemonType type) {
        setName(name);
        setBaseDamage(baseDamage);
        setAccuracy(accuracy);
        setAttackType(type);
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
                    "  Accuracy must be between 10 and 1000"
            );
        }
        this.accuracy = accuracy;
    }

    public PokemonType getType() {
        return type;
    }

    public void setAttackType(PokemonType type) {
        if (type == null) {
            throw new IllegalArgumentException(
                    "  Pokemon type cannot be null"
            );
        }
        this.type = type;
    }

    public abstract void execute(Pokemon attacker, Pokemon defender);
}
