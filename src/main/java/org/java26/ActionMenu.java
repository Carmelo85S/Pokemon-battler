package org.java26;

public class ActionMenu {
    public static void insertNewPokemon() {
        System.out.println("Insert new Pokemon");
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

    static void resetToSeedData() {
        System.out.println("Reset data...");
    }
}
