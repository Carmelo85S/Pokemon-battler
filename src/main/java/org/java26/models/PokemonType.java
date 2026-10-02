package org.java26.models;

public enum PokemonType {
    GRASS("Grass"),
    FIRE("Fire"),
    WATER("Water"),
    ELECTRIC("Electric"),
    NORMAL("Normal");

    private final String label;

    private PokemonType(String label){
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}