package ui;

import util.DBConnection;

public class AppMain {
    public static void main(String[] args) {
        if (!DBConnection.testConnection()) {
            System.out.println("Impossible de se connecter à la base de données.");
            System.out.println("Vérifiez votre fichier db.properties et PostgreSQL.");
            return;
        }
        AppMenu menu = new AppMenu();
        menu.start();
        DBConnection.closeConnection();
    }
}