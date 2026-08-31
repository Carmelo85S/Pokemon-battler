package org.java26;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("+---------------------------+ ");
        System.out.println("|     WELCOME TRAINER       |");
        System.out.println("|            by             |");
        System.out.println("|      Carmelo Salis        |");
        System.out.println("+---------------------------+ ");
        System.out.println();


        /*
        * Menu should appear at least once, create a Menu with 5 options
        * use do-while loop
        * Create, Show, Update, Delete, Exit
        * */

        int choice = 0;

        do{
            System.out.println("1 - Create your Pokemon");
            System.out.println("2 - Show Pokemons");
            System.out.println("3 - Customize your Pokemon");
            System.out.println("4 - Delete your Pokemon");
            System.out.println("5 - Exit");

            System.out.println("Choose your option.");
            System.out.print("Your choice is: ");

            //Check if input from user is valid
            while(!scanner.hasNextInt()){
                System.out.print("Invalid input! Please enter a number between 1 and 5.");
                scanner.nextLine();
            }

            choice=scanner.nextInt();

            //Switch Operations, we should change the case value with methods
            String operations = switch(choice){
                case 1 -> "Create Pokemon";
                case 2 -> "Show Pokemon";
                case 3 -> "Customize your Pokemon";
                case 4 -> "Delete Pokemon";
                default -> "Invalid choice";
            };

            if(choice < 1 || choice > 5){
                System.out.println("Invalid choice, please enter a number between 1 and 5.");
            } else if(choice == 5){
                System.out.println("Good bye trainer.");
            } else {
                System.out.println("Welcome Trainer!");
            }


        }while(choice!=5);



        scanner.close();
    }
}
