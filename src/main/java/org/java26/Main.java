package org.java26;

import org.java26.exceptions.QuitPokemonOperationException;
import org.java26.handlers.JsonHandler;
import org.java26.models.Pokedex;
import org.java26.inputHelpers.InputHelper;
import org.java26.service.battle.BattleStatistics;

import java.util.Scanner;

import static org.java26.service.Menu.*;
import static org.java26.repository.SaveLoadPokemons.loadInitialPokemonData;

public class Main {
    public static void main(String[] args) throws QuitPokemonOperationException {
        Pokedex pokedex = new Pokedex();
        JsonHandler jsonHandler = new JsonHandler();
        BattleStatistics statistics = jsonHandler.loadStatistics("statistic.json");

        printWelcome();
        loadInitialPokemonData(pokedex, jsonHandler);

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
                showMenu();
                choice = InputHelper.readMenuChoice(
                        scanner,
                        0,
                        10,
                        "  Select an option %n  > "
                );
                runAction(choice, scanner, pokedex, jsonHandler, statistics);

        } while (choice != 0);
        scanner.close();
    }
}

