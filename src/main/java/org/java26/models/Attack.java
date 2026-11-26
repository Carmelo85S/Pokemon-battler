package org.java26.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;

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

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        name = "attack_subtype",
        discriminatorType = DiscriminatorType.STRING
)

@Table(name = "attacks")

public abstract class Attack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pokemon_id", nullable = false)
    private Pokemon pokemon;

    @Column(name = "attack_name", nullable = false, length = 30)
    private String name;

    @Column(name = "power", nullable = false)
    private int power;

    @Column(name = "attack_accuracy", nullable = false)
    private int accuracy;

    @Enumerated(EnumType.STRING)
    @Column(name = "attack_type", nullable = false)
    private PokemonType type;

    protected Attack(){}

    protected Attack(PokemonType type) {

        if (type == null) {
            throw new IllegalArgumentException(
                    "  Pokemon type cannot be null"
            );
        }
        this.type = type;
    }

    protected Attack(String name, int power, int accuracy, PokemonType type) {
        setName(name);
        setPower(power);
        setAccuracy(accuracy);
        if (type == null) {
            throw new IllegalArgumentException(
                    "  Pokemon type cannot be null"
            );
        }
        this.type = type;
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

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        if (power < 10 || power > 100) {
            throw new IllegalArgumentException(
                    "  Power must be between 10 and 100"
            );
        }
        this.power = power;
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
    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }



    public abstract void execute(
            Pokemon attacker,
            Pokemon defender,
            boolean criticalHit,
            double effectiveness,
            Random random
    );
}
