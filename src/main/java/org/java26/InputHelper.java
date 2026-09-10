package org.java26;

import java.util.Locale;
import java.util.Scanner;

public class InputHelper {
    public static int readIntBetween(Scanner scanner, int min, int max, String prompt) {
        while (true) {
            System.out.printf(prompt, min, max);
            String userInput = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(userInput);
                if (value < min || value > max) {
                    System.out.printf("Invalid input. Choose a value between %d and %d.%n", min, max);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Input is not a number. Please enter a valid input.");
            }
        }
    }

    public static String readString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String userInput = scanner.nextLine().trim();
            if (userInput.isBlank()) {
                System.out.println("Input cannot be empty.");
                continue;
            }
            return userInput;
        }
    }

    public static PokemonType readType(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String userInput = scanner.nextLine().toUpperCase(Locale.ROOT);
                if (userInput.isBlank()) {
                    System.out.println("Type cannot be empty.");
                    continue;
                }
                return PokemonType.valueOf(userInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input. Type not found in database...");
            }
        }
    }
}
