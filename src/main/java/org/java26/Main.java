package org.java26;

import java.util.Scanner;

import static org.java26.ActionMenu.resetToSeedData;
import static org.java26.Menu.*;

public class Main {
    public static void main(String[] args) {

        Pokedex pokedex = new Pokedex();

        JsonHandler jsonHandler = new JsonHandler();

        //Temporary: use resetToSeedData() to fill the ArrayList wih pokemons.
        resetToSeedData(pokedex);

        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        printWelcome();
        do {
            showMenu();
            choice = InputHelper.readIntBetween(scanner,1,8,"Select an option:");
            runAction(choice, scanner, pokedex, jsonHandler);
        } while (choice != 8);
        scanner.close();
    }
}
