package com.example.mastermind;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * HelloApplication – JavaFX-Einstiegspunkt.
 * Erstellt die drei MVC-Objekte und übergibt die Stage an den Controller.
 */
public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) {
        // 1. Model erstellen (generiert sofort den geheimen Code)
        MastermindModel model = new MastermindModel();

        // 2. View erstellen (baut die JavaFX-Oberfläche)
        MastermindView view = new MastermindView();

        // 3. Controller erstellen und Spiel starten
        MastermindController controller = new MastermindController(model, view, stage);
        controller.run();
    }

    public static void main(String[] args) {
        launch(args);
    }
}