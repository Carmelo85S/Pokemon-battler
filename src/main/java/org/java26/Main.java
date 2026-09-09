package org.java26;

import java.util.Scanner;
import static org.java26.Menu.*;

public class Main {
    public static void main(String[] args) {

        Pokedex pokedex = new Pokedex();

        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        printWelcome();
        do {
            showMenu();
            choice = getChoice(scanner);
            runAction(choice, pokedex);
        } while (choice != 8);
        scanner.close();
    }
}
