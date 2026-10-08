package org.java26.service;

import org.java26.models.*;
import org.java26.exceptions.QuitPokemonOperationException;

import java.util.Scanner;

import static org.java26.consoleLayout.Layout.*;
import static org.java26.inputHelpers.InputHelper.readIntBetween;
import static org.java26.inputHelpers.InputHelper.readString;

public class AttackCreator {

    public static Attack createAttack(
            Scanner scanner,
            PokemonType type
    ) throws QuitPokemonOperationException {

        title("Create Attack");
        subTitle("Attack type");
        menuOption(1, "Damage");
        menuOption(2, "Heal");
        separator();

        int choice = readIntBetween(
                scanner,
                1,
                2,
                "Select an option: > "
        );

        String attackName = readString(
                scanner,
                "  Enter attack name: ",
                "  Your attack name is "
        );
        System.out.println();

        int accuracy = readIntBetween(
                scanner,
                10,
                100,
                "  Enter attack accuracy. Value between %d and %d: "
        );
        System.out.println();

        if (choice == 1) {

            int baseDamage = readIntBetween(
                    scanner,
                    10,
                    100,
                    "  Enter attack damage. Value between %d and %d: "
            );

            System.out.println();
            System.out.println("  Attack added successfully");

            return new DamageAttack(
                    attackName,
                    baseDamage,
                    accuracy,
                    type
            );
        }

        int heal = readIntBetween(
                scanner,
                10,
                30,
                "  Enter heal amount. Value between %d and %d: "
        );

        System.out.println();
        System.out.println("  Attack added successfully");

        return new HealAttack(
                attackName,
                10,
                accuracy,
                type,
                heal
        );
    }
}