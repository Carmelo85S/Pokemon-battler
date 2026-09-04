package org.java26;

public class Attack {
    String name;
    int baseDamage;
    int accuracy;
    PokemonType type;

    public Attack(String name, int baseDamage, int accuracy, PokemonType type){
        this.name = name;
        this.baseDamage = baseDamage;
        this.accuracy = accuracy;
        this.type = type;
    }
}
