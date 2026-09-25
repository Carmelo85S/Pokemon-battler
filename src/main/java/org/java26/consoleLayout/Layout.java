package org.java26.consoleLayout;

import java.util.Locale;

public class Layout {

    public static void title(String text) {
        System.out.println();
        String t = text.toUpperCase(Locale.ROOT);
        System.out.println("=== " + t + " ===");
        System.out.println("-".repeat(t.length() + 8));
    }

    public static void subTitle(String text) {
        System.out.println("--- " + text + " ---");
    }

    public static void separator() {
        System.out.println();
    }

    public static void menuOption(int number, String label){
        System.out.println(" - [" + number + "] - " + label);
    }

    public static void backOption(){
        System.out.println("--- Back to main menu by entering: 'B'---\n");
    }

}
