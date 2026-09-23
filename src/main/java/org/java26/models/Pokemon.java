package org.java26.models;

import org.java26.enums.PokemonType;
import org.java26.exceptions.InvalidPokemonException;

import java.util.ArrayList;

public class Pokemon {

    private static final int MAX_ATTACKS = 4;
    public String name;
    public PokemonType type;
    public int maxHp;
    public int currentHp;
    public ArrayList<Attack> attacks = new ArrayList<>();

    public Pokemon() {
    }

    public Pokemon(String name, PokemonType type, int maxHp, int currentHp) {
        this.name = name;
        this.type = type;
        this.maxHp = maxHp;
        this.currentHp = currentHp;
    }

    public void addAttack(Attack attack) {
        if (attacks.size() >= MAX_ATTACKS) {
            throw new InvalidPokemonException(
                    "  " + name + " has max number of attacks available (" + MAX_ATTACKS + ")."
            );
        }
        attacks.add(attack);
    }}