package org.java26.runner;

import org.bson.Document;
import org.java26.exceptions.QuitPokemonOperationException;
import org.java26.handlers.InputHelper;
import org.java26.models.Pokedex;
import org.java26.repository.JsonHandler;
import org.java26.repository.PokemonRepository;
import org.java26.repository.PokemonRepositoryImpl;
import org.java26.repository.battleLogRepository.BattleLogRepository;
import org.java26.service.battle.BattleLogService;
import org.java26.service.battle.BattleStatistics;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static org.java26.repository.SaveLoadPokemons.loadInitialPokemonData;
import static org.java26.service.Menu.*;

@Component
public class GameRunner implements CommandLineRunner {

    private final Pokedex pokedex;
    private final JsonHandler jsonHandler;
    private final PokemonRepositoryImpl pokemonRepositoryImpl;
    private final BattleLogRepository battleLogRepository;
    private final BattleLogService battleLogService;


    public GameRunner(
            Pokedex pokedex,
            JsonHandler jsonHandler,
            PokemonRepositoryImpl pokemonRepositoryImpl,
            BattleLogRepository battleLogRepository,
            BattleLogService battleLogService
    ) {
        this.pokedex = pokedex;
        this.jsonHandler = jsonHandler;
        this.pokemonRepositoryImpl = pokemonRepositoryImpl;
        this.battleLogRepository = battleLogRepository;
        this.battleLogService = battleLogService;
    }

    @Override
    public void run(String... args) throws IOException {
        BattleStatistics statistics = jsonHandler.loadStatistics("statistic.json");
        printWelcome();
        loadInitialPokemonData(pokedex, jsonHandler, pokemonRepositoryImpl);
        List<Document> find = battleLogRepository.findAll();

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
                runAction(choice, scanner, pokedex, jsonHandler, pokemonRepositoryImpl, statistics, battleLogService);

            } while (choice != 0);
        } catch (
                NoSuchElementException e) {
            System.out.println("  Looks like you entered a special combination. App will close. Please be nice.");
        } catch (
                QuitPokemonOperationException e) {
            System.out.println(e.getMessage());
        }
    }
}
