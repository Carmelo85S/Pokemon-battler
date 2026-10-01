package org.java26.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.java26.exceptions.InvalidPokemonException;

import java.util.ArrayList;
import java.util.List;

public class Pokemon {

    private static final int MAX_ATTACKS = 4;

    private int win = 0;
    private int loss = 0;

    private String name;
    private PokemonType type;
    private int maxHp;
    private int currentHp;
    private ArrayList<Attack> attacks = new ArrayList<>();

    public Pokemon() {
    }

    public Pokemon(String name, PokemonType type, int maxHp, int win, int loss) {
        setName(name);
        setType(type);
        setMaxHp(maxHp);
        setCurrentHp(maxHp);
        setWin(win);
        setLoss(loss);
    }

    public void addAttack(Attack attack) {
        if (attacks.size() >= MAX_ATTACKS) {
            throw new InvalidPokemonException(
                    "  " + name + " has max number of attacks available (" + MAX_ATTACKS + ")."
            );
        }
        attacks.add(attack);
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
        if (maxHp < 10 || maxHp > 1000) {
            throw new IllegalArgumentException(
                    "  Max HP must be between 10 and 1000"
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

    public void addWin() {
        win++;
    }

    public void addLoss() {
        loss++;
    }

    public int getLoss() {
        return loss;
    }

    public void setLoss(int loss) {
        this.loss = loss;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }


    public void heal(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException(
                    "  Heal value can not be negative"
            );
        }
        currentHp = Math.min(maxHp, currentHp + amount);
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

    @Override
    public String toString() {
        return "Pokemon: " + name +
                " | Type: " + type.getType() +
                " | HP: " + currentHp + " / " + maxHp;
    }
}