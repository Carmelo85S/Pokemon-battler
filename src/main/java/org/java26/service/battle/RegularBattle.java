package org.java26.service.battle;

import org.java26.exceptions.InvalidPokemonException;
import org.java26.models.Pokedex;
import org.java26.models.Pokemon;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static org.java26.consoleLayout.Layout.*;
import static org.java26.inputHelpers.InputHelper.readIntBetween;
import static org.java26.inputHelpers.InputHelper.readString;
import static org.java26.service.PokemonCreator.createPokemon;
import static org.java26.service.PokemonViewer.showPokemons;

public class RegularBattle {
    public static void startBattle(
            Scanner scanner,
            Pokedex pokedex,
            List<Pokemon> wildPokemons) {

        if (wildPokemons.isEmpty()) {
            title("No wild pokemons available");
        }

        if (pokedex.getPokemons().isEmpty()) {
            title("No pokemons available");
            subTitle("Do you wanna create pokemons?");
            menuOption(1, "Yes");
            menuOption(2, "No");
            separator();
            try {
                int choice = readIntBetween(scanner, 1, 2, "Select one option: ");
                if (choice == 1) {
                    createPokemon(scanner, pokedex);
                    return;
                }
                ;
            } catch (InvalidPokemonException e) {
                System.out.println(e.getMessage());
            }
        }

        title("Choose your pokemon");
        showPokemons(pokedex);
        while (true) {
            try {
                Pokemon myPokemon = pokedex.getPokemon(readString(scanner, "  Choose your Pokemon for the battle: ", "  You choose "));
                System.out.println("MY POKEMON: " + myPokemon);
                break;
            } catch (InvalidPokemonException e) {
                System.out.println(e.getMessage());
            }
        }

        Random random = new Random();
        int randomOpponent = random.nextInt(wildPokemons.size());
        Pokemon opponent = wildPokemons.get(randomOpponent);

        System.out.println("  Your opponent for this battle is '" + opponent.getName() + "'");

        title("---Random Start---");
        boolean playerTurn = random.nextBoolean();
        System.out.println(playerTurn);
        if (playerTurn) {
            subTitle("Player Starts");
        } else {
            subTitle("Opponent Starts");
        }


    }
}