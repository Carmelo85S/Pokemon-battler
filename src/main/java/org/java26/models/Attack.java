package org.java26.models;

import org.java26.enums.PokemonType;

public class Attack {
    public String name;
    public int baseDamage;
    public int accuracy;
    public PokemonType type;

    public Attack(){}

    public Attack(String name, int baseDamage, int accuracy, PokemonType type){
        this.name = name;
        this.baseDamage = baseDamage;
        this.accuracy = accuracy;
        this.type = type;
    }




}
