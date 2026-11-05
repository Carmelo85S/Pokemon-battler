package org.java26.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.java26.exceptions.InvalidPokemonException;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pokemon")
public class Pokemon implements Evolveable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "max_hp", nullable = false)
    private int maxHp;

    @Column(name = "current_hp", nullable = false)
    private int currentHp;

    @Column(name = "speed", nullable = false)
    private int speed;

    @Column(name = "level", nullable = false)
    private int level = 1;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    private PokemonType type;


    @OneToMany(
            mappedBy = "pokemon",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Attack> attacks = new ArrayList<>();

    private static final int MAX_ATTACKS = 4;

    public Pokemon() {
    }

    public Pokemon(String name, PokemonType type, int maxHp, int speed) {
        setName(name);
        setType(type);
        setMaxHp(maxHp);
        setCurrentHp(maxHp);
        setSpeed(speed);
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        if (speed < 10 || speed > 50) {
            throw new IllegalArgumentException(
                    "  Speed must be between 10 and 50"
            );
        }
        this.speed = speed;
    }

    public void addAttack(Attack attack) {
        if (attack == null || attacks.size() >= MAX_ATTACKS) {
            throw new InvalidPokemonException(
                    "  " + name + " has max number of attacks available (" + MAX_ATTACKS + ")."
            );
        }
        attacks.add(attack);
        attack.setPokemon(this);
    }

    public List<Attack> getAttacks() {
        return attacks;
    }

    public void setAttacks(List<Attack> attacks) {
        if (attacks == null || attacks.size() > MAX_ATTACKS) {
            throw new IllegalArgumentException(
                    "  Pokemon cannot have more than " + MAX_ATTACKS + " attacks."
            );
        }

        this.attacks = new ArrayList<>(attacks);

        for (Attack attack : this.attacks) {
            attack.setPokemon(this);
        }
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

    public PokemonType getType() {
        return type;
    }

    public void setMaxHp(int maxHp) {
        if (maxHp < 200 || maxHp > 300) {
            throw new IllegalArgumentException(
                    "  Max HP must be between 200 and 300"
            );
        }
        this.maxHp = maxHp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(int currentHp) {
        if (currentHp < 0 || currentHp > maxHp) {
            throw new IllegalArgumentException(
                    "  Current HP must be between 0 and max HP"
            );
        }

        this.currentHp = currentHp;
    }

    public void setType(PokemonType type) {
        if (type == null) {
            throw new IllegalArgumentException(
                    "  Pokemon type cannot be null"
            );
        }
        this.type = type;
    }

    public boolean hasMaxAttacks() {
        return attacks.size() >= MAX_ATTACKS;
    }


    public void heal(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException(
                    "  Heal value can not be negative"
            );
        }
        currentHp = Math.min(maxHp, currentHp + amount);
    }

    public void fullHeal() {
        currentHp = maxHp;
    }

    public void takeDamage(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException(
                    "  Damage value can not be negative"
            );
        }
        currentHp = Math.max(0, currentHp - amount);
    }

    @JsonIgnore
    public boolean isFainted() {
        return currentHp == 0;
    }

    public void setLevel(int level) {
        if (level < 1) {
            throw new IllegalArgumentException(
                    "Level must be at least 1"
            );
        }

        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void resetLevel() {
        this.level = 1;
    }

    @JsonIgnore
    public boolean isPlayable() {
        return !isFainted()
                && attacks != null
                && !attacks.isEmpty();
    }

    @Override
    public String toString() {
        return "Pokemon: " + name +
                " | Type: " + type.getLabel() +
                " | HP: " + currentHp + " / " + maxHp;
    }

    @Override
    public void evolve() {
        level++;
        maxHp += 20;
        currentHp = maxHp;
    }
}