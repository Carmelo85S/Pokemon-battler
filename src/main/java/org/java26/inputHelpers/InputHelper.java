package org.java26.inputHelpers;

import org.java26.enums.PokemonType;
import org.java26.exceptions.InvalidPokemonException;
import org.java26.exceptions.QuitPokemonOperationException;

import java.util.Locale;
import java.util.Scanner;

public class InputHelper {

    public static int readMenuchoice(Scanner scanner, int min, int max, String prompt) {
        while (true) {
            System.out.println();
            System.out.printf(prompt, min, max);
            try {
                String userInput = scanner.nextLine().trim();
                validateInt(userInput, min, max);
                return Integer.parseInt(userInput);
            } catch (InvalidPokemonException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static int readIntBetween(Scanner scanner, int min, int max, String prompt) throws QuitPokemonOperationException {
        while (true) {
            System.out.println();
            System.out.printf(prompt, min, max);
            try {
                String userInput = scanner.nextLine().trim();
                if (userInput.equalsIgnoreCase("quit")) {
                    throw new QuitPokemonOperationException("  Operation cancelled");
                }
                validateInt(userInput, min, max);
                return Integer.parseInt(userInput);
            } catch (InvalidPokemonException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static String readString(Scanner scanner, String prompt, String confirm) throws QuitPokemonOperationException {
        while (true) {
            System.out.println();
            System.out.print(prompt);
            try {
                String userInput = scanner.nextLine().trim();
                if (userInput.equalsIgnoreCase("quit")) {
                    throw new QuitPokemonOperationException("  Operation cancelled");
                }
                validateName(userInput);
                System.out.print(confirm + userInput + "\n");
                return userInput;
            } catch (InvalidPokemonException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static PokemonType readType(Scanner scanner, String prompt) throws QuitPokemonOperationException {
        while (true) {
            System.out.println();
            System.out.print(prompt);
            try {
                String userInput = scanner.nextLine().trim().toUpperCase(Locale.ROOT);
                if (userInput.equalsIgnoreCase("quit")) {
                    throw new QuitPokemonOperationException("  Operation cancelled");
                }
                if (userInput.isBlank()) {
                    System.out.println("  Type cannot be empty.");
                    continue;
                }
                PokemonType type = PokemonType.valueOf(userInput);
                System.out.println("  Your pokemon type is: "+ type);
                return type;
            } catch (IllegalArgumentException e) {

                System.out.println("  Invalid input. Choose between: ");
                for (PokemonType type : PokemonType.values()) {
                    System.out.println("  - " + type);
                }
            }
        }
    }

    public static void validateName(String userInput) {

        if (userInput == null || userInput.isBlank()) {
            throw new InvalidPokemonException("  Input cannot be empty.");
        }
        if (!userInput.matches("[a-zA-Z0-9 ]+")) {
            throw new InvalidPokemonException("  No special char allowed.");
        }
        if (!userInput.matches(".*[a-zA-Z].*")) {
            throw new InvalidPokemonException("  Input must contain at least one letter.");
        }
        if(userInput.length() > 11 || userInput.length() < 2) {
            throw new InvalidPokemonException(" Input must be between 2 and 11 characters. ");
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