package org.java26;

import java.util.Scanner;
import static org.java26.ActionMenu.*;

public class Menu {

    public static void showAllPokemon() {
        System.out.println("Pikatchu");
        System.out.println("Bulbasaut");
        System.out.println("Charizard");
    }

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

        while (true) {
            System.out.print("Your choice is: ");

            String input = scanner.nextLine().trim();

            try {
                int choice = Integer.parseInt(input);

                if (choice >= 1 && choice <= 8) {
                    return choice;
                }

                System.out.println("Please insert a number between 1 and 8.");

            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter only one number.");
            }
        }
    }

    public static void runAction(int choice) {
        switch (choice) {
            case 1 -> showAllPokemon();
            case 2 -> insertNewPokemon();
            case 3 -> customizePokemon();
            case 4 -> deletePokemon();
            case 5 -> saveToFile();
            case 6 -> loadFromFile();
            case 7 -> resetToSeedData();
            case 8 -> System.out.println("Goodbye, Trainer!");
            default -> System.out.println("Invalid input");
        }
    }
}
