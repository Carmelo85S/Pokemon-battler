package org.java26;

import java.util.Scanner;

import static org.java26.ActionMenu.*;

public class Menu {

    public static void printWelcome() {
        System.out.println("+---------------------------+ ");
        System.out.println("|     WELCOME TRAINER       |");
        System.out.println("|            by             |");
        System.out.println("|      Carmelo Salis        |");
        System.out.println("+---------------------------+ ");
        System.out.println();
    }

    public static void showMenu() {
        System.out.println("1 - Show all Pokemon.");
        System.out.println("2 - Insert a new Pokemon");
        System.out.println("3 - Customize your Pokemon");
        System.out.println("4 - Delete your Pokemon");
        System.out.println("5 - Save to file.");
        System.out.println("6 - Load from file.");
        System.out.println("7 - Reset to seed data");
        System.out.println("8 - Exit.");
    }

    public static int getChoice(Scanner scanner) {

        return InputHelper.readIntBetween(
                scanner,
                1,
                8,
                "Your choice is: "
        );
    }

    public static void runAction(int choice, Scanner scanner, Pokedex pokedex) {
        switch (choice) {
            case 1 -> showAllPokemon(scanner, pokedex);
            case 2 -> insertNewPokemon(scanner, pokedex);
            case 3 -> customizePokemon(scanner, pokedex);
            case 4 -> deletePokemon(scanner, pokedex);
            case 5 -> saveToFile();
            case 6 -> loadFromFile();
            case 7 -> resetToSeedData(pokedex);
            case 8 -> System.out.println("Goodbye, Trainer!");
            default -> System.out.println("Invalid input");
        }
    }
}
