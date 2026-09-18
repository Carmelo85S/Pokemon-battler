package org.java26;

import java.util.ArrayList;

public class Pokemon {

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
        attacks.add(attack);
    }
}