package com.example.mastermind;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * HelloApplication – JavaFX-Einstiegspunkt.
 * Da Mastermind eine Konsolenanwendung ist, wird JavaFX
 * nur als Starter verwendet und sofort an Main weitergegeben.
 */
public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) {
        // JavaFX-Fenster wird nicht benötigt – Konsolenspiel starten
        Main.main(new String[]{});
        // Programm nach Spielende beenden
        stage.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}