package org.java26;

import org.java26.exceptions.QuitPokemonOperationException;
import org.java26.handlers.JsonHandler;
import org.java26.models.Pokedex;
import org.java26.inputHelpers.InputHelper;

import java.util.Scanner;

import static org.java26.methods.Menu.*;
import static org.java26.methods.PokemonFileService.loadInitialPokemonData;

public class Main {
    public static void main(String[] args) throws QuitPokemonOperationException {
        Pokedex pokedex = new Pokedex();
        JsonHandler jsonHandler = new JsonHandler();
        printWelcome();
        loadInitialPokemonData(pokedex, jsonHandler);

        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
                showMenu();
                choice = InputHelper.readMenuChoice(
                        scanner,
                        1,
                        9,
                        "  Select an option %n  > "
                );
                runAction(choice, scanner, pokedex, jsonHandler);

        } while (choice != 9);
        scanner.close();
    }
}

