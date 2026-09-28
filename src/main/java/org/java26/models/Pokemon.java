package org.java26.models;

import org.java26.enums.PokemonType;
import org.java26.exceptions.InvalidPokemonException;

import java.util.ArrayList;

public class Pokemon {

    private static final int MAX_ATTACKS = 4;
    private String name;
    private PokemonType type;
    private int maxHp;
    private int currentHp;
    private ArrayList<Attack> attacks = new ArrayList<>();

    public Pokemon() {
    }

    public Pokemon(String name, PokemonType type, int maxHp) {
        setName(name);
        setType(type);
        setMaxHp(maxHp);
        setCurrentHp(maxHp);
    }

    public void addAttack(Attack attack) {
        if (attacks.size() >= MAX_ATTACKS) {
            throw new InvalidPokemonException(
                    "  " + name + " has max number of attacks available (" + MAX_ATTACKS + ")."
            );
        }
        attacks.add(attack);
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
        if(type == null){
            throw new IllegalArgumentException(
                    "  Pokemon type cannot be null"
            );
        }
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
        this.type = type;
    }
}