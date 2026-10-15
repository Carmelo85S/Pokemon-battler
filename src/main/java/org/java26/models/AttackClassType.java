package org.java26.models;

public enum AttackClassType {
    DAMAGE("Damage"),
    HEAL("Heal");

    private final String label;

    private AttackClassType(String label){
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
