package org.java26;

import java.util.Scanner;

import static org.java26.Menu.*;

public class Main {
    public static void main(String[] args) throws QuitPokemonOperationException {
        Pokedex pokedex = new Pokedex();
        JsonHandler jsonHandler = new JsonHandler();
        loadPokemonData(pokedex, jsonHandler);
        Scanner scanner = new Scanner(System.in);
        int choice;
        //printWelcome();

        do {
            try {
                showMenu();

                choice = InputHelper.readIntBetween(
                        scanner,
                        1,
                        9,
                        "  Select an option %n  > ",
                        "  Option > "
                );

                if (choice == 9) {
                    System.out.println("  Goodbye!");
                    break;
                }
                runAction(choice, scanner, pokedex, jsonHandler);
            } catch (QuitPokemonOperationException e) {
                System.out.println(e.getMessage());
                break;
            }
        } while (choice != 9);
        scanner.close();
    }
}

