package org.java26.service;

import org.java26.exceptions.QuitPokemonOperationException;
import org.java26.handlers.JsonHandler;
import org.java26.models.Pokedex;

import java.util.Scanner;

import static org.java26.consoleLayout.Layout.*;
import static org.java26.inputHelpers.InputHelper.readIntBetween;

public class BattleMenu {
    public static void battleMenu(Scanner scanner, Pokedex pokedex, JsonHandler jsonHandler) {

        while (true) {
            title("Battle Menu");
            subTitle("Choose your mode");
            menuOption(1, "Regular battle");
            menuOption(2, "Handicap match");
            menuOption(3, "Survival");
            backOption();

            int choice = readIntBetween(scanner, 0, 3, "Choose you battle mode: ");

            runBattleMenuAction(
                    choice,
                    scanner,
                    pokedex,
                    jsonHandler
            );
        }
    }

    public static void runBattleMenuAction(int choice, Scanner scanner, Pokedex pokedex, JsonHandler jsonHandler) {
        try {
            switch (choice) {
                case 1 -> System.out.println("Start regular battle");
                case 2 -> System.out.println("Start Handicap match battle");
                case 3 -> System.out.println("Start survival battle");
                default -> System.out.println("  Invalid input");
            }
        } catch (QuitPokemonOperationException e) {
            System.out.println(e.getMessage());
        }
    }
}
