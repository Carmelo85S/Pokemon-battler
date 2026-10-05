package org.java26.models;

public class DamageAttack extends Attack {
    private final int power;

    public DamageAttack(
            String name,
            int baseDamage,
            int accuracy,
            PokemonType type,
            int power
    ) {
        super(name, baseDamage, accuracy, type);
        this.power = power;
    }


    @Override
    public void execute(Pokemon attacker, Pokemon defender) {
        defender.takeDamage(power);
    }
}
