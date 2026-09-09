package org.java26;

import java.util.Locale;
import java.util.Scanner;

public class ActionMenu {

    public static int readIntBetween(Scanner scanner, int min, int max, String prompt) {
        while (true) {
            System.out.printf(prompt + "%n", min, max);
            String userInput = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(userInput);
                if (value < min || value > max) {
                    System.out.printf("Invalid input. Choose a value between %d and %d.%n", min, max);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Input is not a number. Please enter a valid input.");
            }
        }
    }

    public static String readString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String userInput = scanner.nextLine().trim();
            if (userInput.isBlank()) {
                System.out.println("Input cannot be empty.");
                continue;
            }
            return userInput;
        }
    }

    public static PokemonType readType(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String userInput = scanner.nextLine().toUpperCase(Locale.ROOT);
                if (userInput.isBlank()) {
                    System.out.println("Type cannot be empty.");
                    continue;
                }
                return PokemonType.valueOf(userInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input. Type not found in database...");
            }
        }
    }

    public static void showAllPokemon(Scanner scanner, Pokedex pokedex) {

        if (pokedex.getPokemons().isEmpty()) {
            System.out.println("Nothing to show");
            System.out.println("Do you want to create a Pokemon?");

            int choice = readIntBetween(
                    scanner,
                    1,
                    2,
                    "Make your choice:\n1. Yes\n2. No"
            );

            if (choice == 1) {
                insertNewPokemon(scanner, pokedex);
            } else {
                return;
            }
        }

        for (Pokemon p : pokedex.getPokemons()) {
            System.out.println(
                    "Name: " + p.name +
                            " type: " + p.type +
                            " max HP: " + p.maxHp +
                            " current HP: " + p.currentHp
            );

            for (Attack attack : p.attacks) {
                System.out.println("Attack: " + attack.name);
            }
        }
    }
    public static void insertNewPokemon(Scanner scanner, Pokedex pokemon) {
        System.out.println("Insert new pokemon.");
        //Pokemon obj
        String name = "";

        int maxHp;
        int currentHp;

        //Attack obj
        String attackName;
        int baseDamage;
        int accuracy;

        name = readString(scanner, "Enter new pokemon name");

        PokemonType type = readType(scanner, "Which type is your new pokemon? ");

        maxHp = readIntBetween(scanner, 0, 100, "Enter max HP between %d and %d.");

        currentHp = readIntBetween(scanner, 1, maxHp, "Enter current HP between %d and %d.");

        //Attack
        attackName = readString(scanner, "Enter attack name");

        baseDamage = readIntBetween(
                scanner,
                0,
                100,
                "How much damage should your attack have? Enter a value between %d and %d."
        );

        accuracy = readIntBetween(
                scanner,
                0,
                100,
                "Enter accuracy between %d and %d."
        );

        Pokemon p1 = new Pokemon(name, type, maxHp, currentHp);
        pokemon.addPokemon(p1);

        Attack a1 = new Attack(attackName, baseDamage, accuracy, type);
        p1.addAttack(a1);
    }


    public static void customizePokemon() {
        System.out.println("Customize your pokemon");
    }

    public static void deletePokemon() {
        System.out.println("Are you sure you want to delete your pokemon?");
    }

    public static void saveToFile() {
        System.out.println("Saving...");
    }

    public static void loadFromFile() {
        System.out.println("Loading...");
    }

    static void resetToSeedData(Pokedex pokemon) {
        Attack thunderbolt = new Attack("Thunderbolt", 50, 80, PokemonType.ELECTRIC);
        Attack quickAttack = new Attack("Quick Attack", 40, 90, PokemonType.NORMAL);

        Pokemon pikachu = new Pokemon("Pikachu", PokemonType.ELECTRIC, 100, 80);
        pikachu.addAttack(thunderbolt);
        pikachu.addAttack(quickAttack);


        Attack ember = new Attack("Ember", 40, 90, PokemonType.FIRE);
        Attack scratch = new Attack("Scratch", 35, 95, PokemonType.NORMAL);

        Pokemon charmander = new Pokemon("Charmander", PokemonType.FIRE, 100, 70);
        charmander.addAttack(ember);
        charmander.addAttack(scratch);


        Attack waterGun = new Attack("Water Gun", 40, 95, PokemonType.WATER);
        Attack tackle = new Attack("Tackle", 35, 95, PokemonType.NORMAL);

        Pokemon squirtle = new Pokemon("Squirtle", PokemonType.WATER, 100, 65);
        squirtle.addAttack(waterGun);
        squirtle.addAttack(tackle);


        Attack vineWhip = new Attack("Vine Whip", 45, 90, PokemonType.GRASS);
        Attack headbutt = new Attack("Headbutt", 50, 85, PokemonType.NORMAL);

        Pokemon bulbasaur = new Pokemon("Bulbasaur", PokemonType.GRASS, 100, 90);
        bulbasaur.addAttack(vineWhip);
        bulbasaur.addAttack(headbutt);


        Attack flamethrower = new Attack("Flamethrower", 70, 85, PokemonType.FIRE);
        Attack wingAttack = new Attack("Wing Attack", 60, 90, PokemonType.NORMAL);

        Pokemon charizard = new Pokemon("Charizard", PokemonType.FIRE, 100, 100);
        charizard.addAttack(flamethrower);
        charizard.addAttack(wingAttack);


        Attack thunderShock = new Attack("Thunder Shock", 40, 95, PokemonType.ELECTRIC);
        Attack spark = new Attack("Spark", 50, 90, PokemonType.ELECTRIC);

        Pokemon raichu = new Pokemon("Raichu", PokemonType.ELECTRIC, 100, 100);
        raichu.addAttack(thunderShock);
        raichu.addAttack(spark);

        Pokemon[] Pokemon = {
                pikachu,
                charmander,
                squirtle,
                bulbasaur,
                charizard,
                raichu
        };

        for (Pokemon p : Pokemon) {
            pokemon.addPokemon(p);
        }
    }
}

