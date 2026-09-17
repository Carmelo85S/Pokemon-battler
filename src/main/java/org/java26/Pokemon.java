package org.java26;

import java.util.ArrayList;

public class Pokemon {
    String name;
    PokemonType type;
    int maxHp;
    int currentHp;
    ArrayList<Attack> attacks = new ArrayList<>();

    public Pokemon(){};

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
