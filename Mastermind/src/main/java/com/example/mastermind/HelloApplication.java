package com.example.mastermind;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * HelloApplication - JavaFX-Einstiegspunkt.
 * Verwaltet Navigation: Startbildschirm → Spiel → Highscores → Startbildschirm
 */
public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) {
        showStartScreen(stage);
    }

    /**
     * Zeigt den Startbildschirm an.
     * Wird auch nach dem Spiel wieder aufgerufen (Neustart).
     */
    private void showStartScreen(Stage stage) {
        StartView startView = new StartView();
        javafx.scene.Scene startScene = startView.buildScene();

        // "Spiel starten" gedrueckt
        startView.setOnStart(() -> {
            String playerName = startView.getPlayerName();
            int    attempts   = startView.getSelectedAttempts();

            MastermindModel      model      = new MastermindModel(attempts);
            MastermindView       view       = new MastermindView();
            MastermindController controller = new MastermindController(model, view, stage, playerName);
            controller.run();
        });

        // "Highscores anzeigen" gedrueckt
        startView.setOnShowHighscores(() -> {
            showHighscores(stage);
        });

        stage.setTitle("Mastermind");
        stage.setScene(startScene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Zeigt die Highscore-Tabelle an.
     * "Zurueck" kehrt zum Startbildschirm zurueck.
     */
    private void showHighscores(Stage stage) {
        HighscoreView hsView = new HighscoreView();
        javafx.scene.Scene hsScene = hsView.buildScene();

        // "Zurueck" gedrueckt → wieder Startbildschirm
        hsView.setOnBack(() -> showStartScreen(stage));

        stage.setTitle("Mastermind - Highscores");
        stage.setScene(hsScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}