package org.java26.methods;

import org.java26.enums.PokemonType;
import org.java26.exceptions.QuitPokemonOperationException;
import org.java26.models.Attack;

import java.util.Scanner;

import static org.java26.inputHelpers.InputHelper.readIntBetween;
import static org.java26.inputHelpers.InputHelper.readString;

public class AttackCreator {
    public static Attack createAttack(Scanner scanner, PokemonType type)throws QuitPokemonOperationException{
        String attackName = readString(
                scanner,
                "  Enter attack name: ",
                "  Your attack name is "
        );
        System.out.println();

        int baseDamage = readIntBetween(
                scanner,
                10,
                100,
                "  Enter attack damage. Value between %d and %d: "
        );
        System.out.println();

        int accuracy = readIntBetween(
                scanner,
                10,
                100,
                "  Enter attack accuracy. Value between %d and %d: "
        );
        System.out.println();
        System.out.println("  Attack added successfully");

        return new Attack(
                attackName,
                baseDamage,
                accuracy,
                type
        );
    }
}
