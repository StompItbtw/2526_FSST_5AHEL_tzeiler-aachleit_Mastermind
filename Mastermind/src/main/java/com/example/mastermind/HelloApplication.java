package com.example.mastermind;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * HelloApplication - JavaFX-Einstiegspunkt.
 * Zeigt zuerst den Startbildschirm, dann das Spiel.
 */
public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) {
        // ── 1. Startbildschirm anzeigen ────────────────────────────────────
        StartView startView = new StartView();
        javafx.scene.Scene startScene = startView.buildScene();

        // ── 2. Wenn "Spiel starten" gedrueckt wird ─────────────────────────
        startView.setOnStart(() -> {
            // Name und Versuchsanzahl aus dem Startbildschirm holen
            String playerName = startView.getPlayerName();
            int    attempts   = startView.getSelectedAttempts();

            // Model mit gewaehlter Versuchsanzahl erstellen
            MastermindModel model = new MastermindModel(attempts);

            // View und Controller erstellen
            MastermindView       view       = new MastermindView();
            MastermindController controller = new MastermindController(
                    model, view, stage, playerName
            );

            // Spiel starten (wechselt die Szene auf der Stage)
            controller.run();
        });

        // Startbildschirm anzeigen
        stage.setTitle("Mastermind");
        stage.setScene(startScene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}