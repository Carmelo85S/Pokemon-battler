package org.java26.service.battle;

import org.java26.exceptions.InvalidPokemonException;
import org.java26.models.Attack;
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
        try {
            if (wildPokemons.isEmpty()) {
                title("No wild pokemons available");
                return;
            }
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
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

        Pokemon myPokemon = choosePokemon(scanner, pokedex);

        Random random = new Random();
        int randomOpponent = random.nextInt(wildPokemons.size());
        Pokemon opponent = wildPokemons.get(randomOpponent);

        System.out.println("  Your opponent for this battle is '" + opponent.getName() + "'");

        title("---Random Start---");
        boolean playerTurn = random.nextBoolean();
        System.out.println(playerTurn);

        while (myPokemon.getCurrentHp() > 0 && opponent.getCurrentHp() > 0) {
            //PLAYER TURN
            if (playerTurn) {
                subTitle(myPokemon.getName() + " Starts");

                System.out.println();
                System.out.println("  ATTACKS");
                System.out.println("+----------------+----------------+----------------+----------------+----------------+");
                System.out.printf(
                        "| %-14s | %-14s | %-14s | %-14s | %-14s |%n",
                        "Choice", "Name", "Damage", "Accuracy", "Type"
                );
                System.out.println("+----------------+----------------+----------------+----------------+----------------+");

                for (int i = 0; i < myPokemon.getAttacks().size(); i++) {

                    Attack attack = myPokemon.getAttacks().get(i);

                    System.out.printf(
                            "| %-14d | %-14s | %-14d | %-14d | %-14s |%n",
                            i + 1,
                            attack.getName(),
                            attack.getBaseDamage(),
                            attack.getAccuracy(),
                            attack.getType()
                    );
                }

                System.out.println("+----------------+----------------+----------------+----------------+----------------+");

                int choice;
                while (true) {
                    try {
                        choice = readIntBetween(
                                scanner,
                                1,
                                myPokemon.getAttacks().size(),
                                "Choose attack: "
                        );
                        break;
                    } catch (InvalidPokemonException e) {
                        System.out.println(e.getMessage());
                    }
                }
                Attack selectedAttack = myPokemon.getAttacks().get(choice - 1);
                int damage = selectedAttack.getBaseDamage();

                int newHp = Math.max(
                        0,
                        opponent.getCurrentHp() - damage
                );

                opponent.setCurrentHp(newHp);
                System.out.println(
                        myPokemon.getName() +
                                " used " +
                                selectedAttack.getName()
                );

                System.out.println(
                        opponent.getName() +
                                " HP: " +
                                opponent.getCurrentHp() +
                                "/" +
                                opponent.getMaxHp()
                );

                playerTurn = !playerTurn;
            } else{
                int randomAttack = random.nextInt(opponent.getAttacks().size());
                Attack selectedAttack = opponent.getAttacks().get(randomAttack);

                int damagePlayer = selectedAttack.getBaseDamage();

                int newHp = Math.max(
                        0,
                        myPokemon.getCurrentHp() - damagePlayer
                );

                myPokemon.setCurrentHp(newHp);
                System.out.println(
                        opponent.getName() +
                                " used " +
                                selectedAttack.getName()
                );

                System.out.println(
                        myPokemon.getName() +
                                " HP: " +
                                myPokemon.getCurrentHp() +
                                "/" +
                                myPokemon.getMaxHp()
                );
                playerTurn = !playerTurn;

            }
        }
    }

    private static Pokemon choosePokemon(Scanner scanner, Pokedex pokedex) {

        while (true) {
            try {
                String choice = readString(
                        scanner,
                        "  Choose your Pokemon for the battle: ",
                        "  You choose "
                );

                return pokedex.getPokemon(choice);

            } catch (InvalidPokemonException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}

