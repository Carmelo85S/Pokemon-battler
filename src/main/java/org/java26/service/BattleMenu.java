package org.java26.service;

import org.java26.exceptions.QuitPokemonOperationException;
import org.java26.handlers.JsonHandler;
import org.java26.models.Pokedex;
import org.java26.models.Pokemon;
import org.java26.service.battle.BattleStatistics;
import org.java26.service.battle.RegularBattle;

import java.util.List;
import java.util.Scanner;

import static org.java26.UI.Layout.*;
import static org.java26.inputHelpers.InputHelper.readIntBetween;
import static org.java26.repository.SaveLoadPokemons.*;
import static org.java26.service.battle.HandicapBattle.startHandicapBattle;

public class BattleMenu {
    public static void battleMenu(Scanner scanner, Pokedex pokedex, JsonHandler jsonHandler, BattleStatistics statistics) {
        int choice;
        do {
            title("Battle Menu");
            subTitle("Choose your mode");
            menuOption(1, "Regular battle");
            menuOption(2, "Handicap match");
            menuOption(3, "Exit & Save");
            backOption();

            choice = readIntBetween(scanner, 0,3, "Choose your battle mode: ");

            runBattleMenuAction(
                    choice,
                    scanner,
                    pokedex,
                    jsonHandler,
                    statistics
            );
        } while(choice != 3);
    }

    public static void runBattleMenuAction(int choice, Scanner scanner, Pokedex pokedex, JsonHandler jsonHandler, BattleStatistics statistics) {

        try {
            switch (choice) {
                case 1 -> {
                    List<Pokemon> wildPokemons =
                            loadPokemonList(jsonHandler, "wild-pokemons.json");

                    RegularBattle.startBattle(
                            scanner,
                            pokedex,
                            wildPokemons,
                            statistics,
                            jsonHandler
                    );
                }
                case 2 ->  {
                    List<Pokemon> wildPokemons =
                            loadPokemonList(jsonHandler, "wild-pokemons.json");

                    startHandicapBattle(
                            scanner,
                            pokedex,
                            wildPokemons,
                            statistics,
                            jsonHandler
                    );
                }
                case 3 -> {
                    jsonHandler.saveStatistics("statistic.json", statistics);
                    saveToFile(pokedex, jsonHandler);
                    System.out.println("  Statistics saved correctly!");
                }
                default -> System.out.println("  Invalid input");
            }
        } catch (QuitPokemonOperationException e) {
            System.out.println(e.getMessage());
        }
    }
}
