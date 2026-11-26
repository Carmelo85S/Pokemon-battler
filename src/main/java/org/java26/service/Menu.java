package org.java26.service;

import org.java26.exceptions.QuitPokemonOperationException;
import org.java26.repository.JsonHandler;
import org.java26.models.Pokedex;
import org.java26.repository.PokemonRepositoryImpl;
import org.java26.repository.battleLogRepository.BattleLogRepository;
import org.java26.service.battle.BattleLogService;
import org.java26.service.battle.BattleStatistics;

import java.util.Scanner;

import static org.java26.UI.Layout.*;
import static org.java26.repository.SaveLoadPokemons.*;
import static org.java26.service.BattleMenu.battleMenu;
import static org.java26.service.PokemonCreator.createPokemon;
import static org.java26.service.PokemonEditor.deletePokemon;
import static org.java26.service.PokemonEditor.updatePokemon;
import static org.java26.service.PokemonViewer.getPokemonInfo;
import static org.java26.service.PokemonViewer.showAllPokemon;

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
        menuOption(5, "Reset to default data");
        menuOption(6, "View Pokemon details");
        menuOption(7, "Enter Battle Arena");
        menuOption(8, "View Battle Statistics");
        menuOption(9, "Reset Battle Statistics");
        menuOption(0, "Exit");
    }

    public static void runAction(
            int choice,
            Scanner scanner,
            Pokedex pokedex,
            JsonHandler jsonHandler,
            PokemonRepositoryImpl impl,
            BattleStatistics statistics,
            BattleLogRepository blr,
            BattleLogService bls
    ) {
        try {
            switch (choice) {
                case 1 -> showAllPokemon(scanner, pokedex, impl);
                case 2 -> createPokemon(scanner, pokedex, impl);
                case 3 -> updatePokemon(scanner, pokedex, impl);
                case 4 -> deletePokemon(scanner, pokedex, impl);
                case 5 -> resetToSeedData(pokedex, impl, blr, jsonHandler);
                case 6 -> getPokemonInfo(scanner, pokedex);
                case 7 -> battleMenu(scanner, pokedex, jsonHandler, statistics, impl, bls);
                case 8 -> bls.printAllBattles();
                case 9 -> bls.resetStatistics();
                case 0 -> System.out.println("  Goodbye!");
                default -> System.out.println("  Invalid input");
            }
        } catch (QuitPokemonOperationException e) {
            System.out.println(e.getMessage());
        }
    }}

