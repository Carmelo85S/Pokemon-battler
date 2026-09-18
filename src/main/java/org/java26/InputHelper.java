package org.java26;

import java.util.Locale;
import java.util.Scanner;

public class InputHelper {
    public static int readIntBetween(Scanner scanner, int min, int max, String prompt, String confirm) {
        while (true) {
            System.out.println();
            System.out.printf(prompt, min, max);
            try {
                String userInput = scanner.nextLine().trim();
                validateInt(userInput, min, max);
                int value = Integer.parseInt(userInput);
                System.out.println(confirm + " " + value);
                return value;
            } catch (InvalidPokemonException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static String readString(Scanner scanner, String prompt, String confirm) {
        while (true) {
            System.out.println();
            System.out.print(prompt);
            try {
                String userInput = scanner.nextLine().trim();
                validateName(userInput);
                System.out.println(confirm + userInput);
                return userInput;
            } catch (InvalidPokemonException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static PokemonType readType(Scanner scanner, String prompt) {
        while (true) {
            System.out.println();
            System.out.print(prompt);
            try {
                String userInput = scanner.nextLine().toUpperCase(Locale.ROOT);
                if (userInput.isBlank()) {
                    System.out.println("  Type cannot be empty.");
                    continue;
                }
                System.out.println("  Your pokemon type is: "+ userInput.toUpperCase());
                return PokemonType.valueOf(userInput);
            } catch (IllegalArgumentException e) {
                System.out.println("  Invalid input. Type not found in database...");
            }
        }
    }

    public static void validateName(String name) {

        if (name == null || name.isBlank()) {
            throw new InvalidPokemonException("  Name cannot be empty.");
        }
        if (!name.matches("[a-zA-Z0-9 ]+")) {
            throw new InvalidPokemonException("  No special char allowed.");
        }
        if (!name.matches(".*[a-zA-Z].*")) {
            throw new InvalidPokemonException("  Name must contain at least one letter.");
        }

    }

    public static void validateInt(String userInput, int min, int max) {
        if (userInput.isBlank()) {
            throw new InvalidPokemonException("  Your input is blank");
        }
        int value;
        try {
            value = Integer.parseInt(userInput);
        } catch (NumberFormatException e) {
            throw new InvalidPokemonException("  Input must be a number");
        }
        if (value < min || value > max) {
            throw new InvalidPokemonException(
                    "  Value should be between " + min + " and " + max
            );
        }
    }
}