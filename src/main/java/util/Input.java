package util;

import java.util.Scanner;

public class Input {

    private static final Scanner scanner = new Scanner(System.in);

    private Input() {
        throw new UnsupportedOperationException("Impossible d'instancier une classe utilitaire!");
    }
    public static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    public static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Erreur : Veuillez entrer un nombre entier valide.");
            }
        }
    }
    public static long readLong(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Erreur : Veuillez entrer un nombre valide.");
            }
        }
    }
    public static double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Erreur : Veuillez entrer un nombre decimal valide.");
            }
        }
    }
    public static boolean readBoolean(String prompt) {
        while (true) {
            System.out.print(prompt + " (oui/non) : ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("oui") || input.equals("o") || input.equals("yes") || input.equals("y")) {
                return true;
            } else if (input.equals("non") || input.equals("n") || input.equals("no")) {
                return false;
            } else {
                System.out.println("Erreur : Veuillez repondre par oui ou non.");
            }
        }
    }
    public static void pressEnterToContinue() {
        System.out.print("\nAppuyez sur Entree pour continuer...");
        scanner.nextLine();
    }
    public static void closeScanner() {
        scanner.close();
    }
}