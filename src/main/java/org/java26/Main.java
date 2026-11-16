package org.java26;

import org.bson.Document;
import org.java26.exceptions.QuitPokemonOperationException;
import org.java26.repository.JsonHandler;
import org.java26.models.Pokedex;
import org.java26.handlers.InputHelper;
import org.java26.repository.PokemonRepository;
import org.java26.repository.PokemonRepositoryImpl;
import org.java26.repository.battleLogRepository.BattleLogRepository;
import org.java26.repository.battleLogRepository.MongoBattleLogRepositoryImpl;
import org.java26.service.battle.BattleLogService;
import org.java26.service.battle.BattleStatistics;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static org.java26.service.Menu.*;
import static org.java26.repository.SaveLoadPokemons.loadInitialPokemonData;

public class Main {
    public static void main(String[] args) throws QuitPokemonOperationException, IOException {
        Pokedex pokedex = new Pokedex();
        JsonHandler jsonHandler = new JsonHandler();
        BattleStatistics statistics = jsonHandler.loadStatistics("statistic.json");
        PokemonRepositoryImpl impl = new PokemonRepositoryImpl();
        printWelcome();
        loadInitialPokemonData(pokedex, jsonHandler,impl);
        BattleLogRepository blr = new MongoBattleLogRepositoryImpl();
        BattleLogService bls = new BattleLogService(blr);
        List<Document> find = blr.findAll();

        System.out.println(find);


        try (Scanner scanner = new Scanner(System.in)) {
            int choice;
            do {
                showMenu();
                choice = InputHelper.readMenuChoice(
                        scanner,
                        0,
                        11,
                        "  Select an option %n  > "
                );
                runAction(choice, scanner, pokedex, jsonHandler, impl, statistics, bls);

            } while (choice != 0);
        } catch (NoSuchElementException e) {
            System.out.println("  Looks like you entered a special combination. App will close. Please be nice.");
        } catch (QuitPokemonOperationException e) {
            System.out.println(e.getMessage());
        }
    }
}

