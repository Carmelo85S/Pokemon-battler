package org.java26.service;

import org.java26.exceptions.QuitPokemonOperationException;
import org.java26.handlers.JsonHandler;
import org.java26.models.Pokedex;
import org.java26.service.battle.BattleStatistics;

import java.util.Scanner;

import static org.java26.UI.Layout.*;
import static org.java26.repository.SaveLoadPokemons.*;
import static org.java26.service.BattleMenu.battleMenu;
import static org.java26.service.PokemonCreator.createPokemon;
import static org.java26.service.PokemonEditor.deletePokemon;
import static org.java26.service.PokemonEditor.updatePokemon;
import static org.java26.service.PokemonStatsViewer.getPokemonsStats;
import static org.java26.service.PokemonViewer.getPokemonInfo;
import static org.java26.service.PokemonViewer.showAllPokemon;

public class Menu {

    public static void printWelcome() {
        title("WELCOME TRAINER");
        separator();
        title("POKEDEX APP");
        separator();
        subTitle("Manage your Pokemon collection");
        subTitle("customize their stats and attacks,");
        subTitle("and keep your Pokedex organized");
        subTitle("by Carmelo Salis.");
        separator();
    }

    public static void showMenu() {
        title("Pokedex");
        menuOption(1, "Show all Pokemon");
        menuOption(2, "Insert a new Pokemon");
        menuOption(3, "Customize your Pokemon");
        menuOption(4, "Delete your Pokemon");
        menuOption(5, "Save to file");
        menuOption(6, "Load from file");
        menuOption(7, "Reset to seed data");
        menuOption(8, "Pokemon info");
        menuOption(9, "Start battle");
        menuOption(10, "Show stats");
        menuOption(0, "Exit & Save");

    }

    public static void runAction(int choice, Scanner scanner, Pokedex pokedex, JsonHandler jsonHandler, BattleStatistics statistics) {
        try {
            switch (choice) {
                case 1 -> showAllPokemon(scanner, pokedex);
                case 2 -> createPokemon(scanner, pokedex);
                case 3 -> updatePokemon(scanner, pokedex);
                case 4 -> deletePokemon(scanner, pokedex);
                case 5, 0 -> saveToFile(pokedex, jsonHandler);
                case 6 -> loadFromFile(pokedex, jsonHandler, "pokemon.json");
                case 7 -> resetToSeedData(pokedex, jsonHandler);
                case 8 -> getPokemonInfo(scanner, pokedex);
                case 9 -> battleMenu(scanner, pokedex, jsonHandler, statistics);
                case 10 -> getPokemonsStats(pokedex, statistics);
                default -> System.out.println("  Invalid input");
            }
        } catch (QuitPokemonOperationException e) {
            System.out.println(e.getMessage());
        }
    }
}

