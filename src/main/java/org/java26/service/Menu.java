package org.java26.service;

import org.java26.exceptions.QuitPokemonOperationException;
import org.java26.repository.JsonHandler;
import org.java26.models.Pokedex;
import org.java26.service.battle.BattleStatistics;

import java.nio.file.Path;
import java.util.Scanner;

import static org.java26.UI.Layout.*;
import static org.java26.repository.SaveLoadPokemons.*;
import static org.java26.service.BattleMenu.battleMenu;
import static org.java26.service.PokemonCreator.createPokemon;
import static org.java26.service.PokemonEditor.deletePokemon;
import static org.java26.service.PokemonEditor.updatePokemon;
import static org.java26.service.PokemonStatsViewer.getPokemonsStats;
import static org.java26.service.PokemonStatsViewer.resetPokemonsStats;
import static org.java26.service.PokemonViewer.getPokemonInfo;
import static org.java26.service.PokemonViewer.showAllPokemon;
import static org.java26.service.StatisticsService.resetAllStatistic;

public class Menu {

    public static void printWelcome() {
        title("WELCOME TRAINER");
        separator();
        title("POKEARENA CLI");
        separator();
        subTitle("Manage your Pokemon collection");
        subTitle("Customize stats and attacks");
        subTitle("Battle opponents and track your progress");
        subTitle("Save your journey and master the arena");
        subTitle("by Carmelo Salis.");
        separator();
    }

    public static void showMenu() {
        title("POKEARENA");
        menuOption(1, "View all Pokemon");
        menuOption(2, "Add a new Pokemon");
        menuOption(3, "Customize Pokemon");
        menuOption(4, "Delete Pokemon");
        menuOption(5, "Save Pokemon");
        menuOption(6, "Load Pokemon");
        menuOption(7, "Restore default Pokemon");
        menuOption(8, "View Pokemon details");
        menuOption(9, "Enter Battle Arena");
        menuOption(10, "View Battle Statistics");
        menuOption(11, "Reset Battle Statistics");
        menuOption(0, "Save & Exit");

    }

    public static void runAction(int choice, Scanner scanner, Pokedex pokedex, JsonHandler jsonHandler, BattleStatistics statistics) {
        try {
            switch (choice) {
                case 1 -> showAllPokemon(scanner, pokedex);
                case 2 -> createPokemon(scanner, pokedex);
                case 3 -> updatePokemon(scanner, pokedex);
                case 4 -> deletePokemon(scanner, pokedex);
                case 5 -> saveToFile(pokedex, jsonHandler);
                case 6 -> loadFromFile(pokedex, jsonHandler, "pokemon.json");
                case 7 -> resetToSeedData(pokedex, jsonHandler);
                case 8 -> getPokemonInfo(scanner, pokedex);
                case 9 -> battleMenu(scanner, pokedex, jsonHandler, statistics);
                case 10 -> getPokemonsStats(pokedex, statistics);
                case 11 -> resetAllStatistic(Path.of("statistic.json"), pokedex, statistics, jsonHandler);
                case 0 -> {
                    saveToFile(pokedex, jsonHandler);
                    jsonHandler.saveStatistics("statistic.json", statistics);
                    System.out.println("  Goodbye!");
                }
                default -> System.out.println("  Invalid input");
            }
        } catch (QuitPokemonOperationException e) {
            System.out.println(e.getMessage());
        }
    }
}

