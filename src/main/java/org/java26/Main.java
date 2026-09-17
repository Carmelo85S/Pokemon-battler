package org.java26;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.java26.Menu.*;

public class Main {
    public static void main(String[] args) {

        Pokedex pokedex = new Pokedex();
        JsonHandler jsonHandler = new JsonHandler();
        try {
            System.out.println("checking");
            List<Pokemon> pokemon = jsonHandler.loadPokemon("pokemon.json");
            System.out.println("Empty: "+ pokemon.isEmpty());
            if (pokemon.isEmpty()) {
                pokemon = jsonHandler.loadPokemon("seed-pokemons.json");
                for (Pokemon p : pokemon) {
                    pokedex.addPokemon(p);
                }
            }
        } catch (PokemonLoadException e) {
            System.out.println(e.getMessage());
        }



    Scanner scanner = new Scanner(System.in);
    int choice = 0;

    printWelcome();
        do

    {
        showMenu();
        choice = InputHelper.readIntBetween(scanner, 1, 8, "Select an option:");
        runAction(choice, scanner, pokedex, jsonHandler);
    } while(choice !=8);
        scanner.close();
}
}
