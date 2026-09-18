package org.java26;

import java.util.Scanner;
import static org.java26.Menu.*;

public class Main {
    public static void main(String[] args) {
        Pokedex pokedex = new Pokedex();
        JsonHandler jsonHandler = new JsonHandler();

        loadPokemonData(pokedex, jsonHandler);

        Scanner scanner = new Scanner(System.in);
        int choice;

        printWelcome();
        do {
            showMenu();
            choice = InputHelper.readIntBetween(scanner, 1, 8, "  Select an option %n  > ", "  Redirect to menu for operation ");
            runAction(choice, scanner, pokedex, jsonHandler);
        } while (choice != 8);
        scanner.close();
    }
}

