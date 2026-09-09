package org.java26;

import java.util.ArrayList;

public class Pokemon {
    String name;
    PokemonType type;
    int maxHp;
    int currentHp;
    ArrayList<Attack> attacks = new ArrayList<>();

    public Pokemon(String name, PokemonType type, int maxHp) {
        this.name = name;
        this.type = type;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
    }

    public void addAttack(Attack attack) {
        attacks.add(attack);
    }
}
