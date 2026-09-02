package org.java26;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        printWelcome();

        int choice = 0;

        do{
           showMenu();

            System.out.println("Choose your option.");
            System.out.print("Your choice is: ");

            String input = scanner.nextLine().trim();

            try{
                choice = Integer.parseInt(input);
                    if(choice < 1 || choice > 8){
                        System.out.println("Please insert a number between 1 and 8.");
                        continue;
                    }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter only one number.");
                continue;
            }

            //Switch Operations, replace cases with methods calls later
            switch(choice){
                case 1 -> showAllPokemon();
                case 2 -> insertNewPokemon();
                case 3 -> customizePokemon();
                case 4 -> deletePokemon();
                case 5 -> saveToFile();
                case 6 -> loadFromFile();
                case 7 -> resetToSeedData();
                case 8 -> System.out.println("Goodbye, Trainer!");
                default -> System.out.println("Invalid input");

            }
        }while(choice != 8);

        scanner.close();
    }

    public static void showAllPokemon(){
        System.out.println("Pikatchu");
        System.out.println("Bulbasaut");
        System.out.println("Charizard");
    }

    public static void printWelcome(){
        System.out.println("+---------------------------+ ");
        System.out.println("|     WELCOME TRAINER       |");
        System.out.println("|            by             |");
        System.out.println("|      Carmelo Salis        |");
        System.out.println("+---------------------------+ ");
        System.out.println();
    }

    public static void showMenu(){
        System.out.println("1 - Show all Pokemon.");
        System.out.println("2 - Insert a new Pokemon");
        System.out.println("3 - Customize your Pokemon");
        System.out.println("4 - Delete your Pokemon");
        System.out.println("5 - Save to file.");
        System.out.println("6 - Load from file.");
        System.out.println("7 - Reset to seed data");
        System.out.println("8 - Exit.");
    }



    public static void insertNewPokemon(){
        System.out.println("Insert new Pokemon");
    }

    public static void customizePokemon(){
        System.out.println("Customize your pokemon");
    }

    public static void deletePokemon(){
        System.out.println("Are you sure you want to delete your pokemon?");
    }

    public static void saveToFile(){
        System.out.println("Saving...");
    }

    public static void loadFromFile(){
        System.out.println("Loading...");
    }

    static void resetToSeedData(){
        System.out.println("Reset data...");
    }
}
