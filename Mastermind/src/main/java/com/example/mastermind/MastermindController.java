package com.example.mastermind;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * CONTROLLER - steuert den Spielablauf.
 * Startet Timer bei Spielbeginn, speichert Highscore bei Gewinn.
 */
public class MastermindController {

    private final MastermindModel model;
    private final MastermindView  view;
    private final Stage           stage;
    private final String          playerName;

    private final char[] currentGuess = new char[MastermindModel.CODE_LENGTH];
    private int  filledSlots   = 0;

    // Timer
    private Timeline timer;
    private long     elapsedSeconds = 0;

    public MastermindController(MastermindModel model, MastermindView view,
                                Stage stage, String playerName) {
        this.model      = model;
        this.view       = view;
        this.stage      = stage;
        this.playerName = playerName;
    }

    public void run() {
        javafx.scene.Scene scene = view.buildScene(playerName, model.getMaxAttempts());

        // ── Timer starten (laeuft ab dem ersten Anzeigen der Szene) ────────
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            elapsedSeconds++;
            view.updateTimer(elapsedSeconds);
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play(); // Timer startet sofort wenn Spiel beginnt

        // ── Farbe gewaehlt ─────────────────────────────────────────────────
        view.setOnColorSelected(colorChar -> {
            if (filledSlots < MastermindModel.CODE_LENGTH
                    && !model.isGameWon() && !model.isGameOver()) {
                currentGuess[filledSlots] = colorChar;
                filledSlots++;
                view.updateCurrentGuess(currentGuess, filledSlots);
            }
        });

        // ── Loeschen ───────────────────────────────────────────────────────
        view.setOnDelete(() -> {
            if (filledSlots > 0) {
                filledSlots--;
                currentGuess[filledSlots] = 0;
                view.updateCurrentGuess(currentGuess, filledSlots);
            }
        });

        // ── Abschicken ─────────────────────────────────────────────────────
        view.setOnSubmit(() -> {
            if (filledSlots < MastermindModel.CODE_LENGTH) return;
            if (model.isGameWon() || model.isGameOver()) return;

            model.processGuess(new String(currentGuess));
            view.updateBoard(model);

            filledSlots = 0;
            for (int i = 0; i < MastermindModel.CODE_LENGTH; i++) currentGuess[i] = 0;
            view.updateCurrentGuess(currentGuess, filledSlots);

            if (model.isGameWon()) {
                timer.stop();
                view.stopTimer();
                view.disableInput();

                // ── Highscore speichern (nur bei Gewinn) ───────────────────
                boolean isNewHighscore = HighscoreManager.isHighscore(
                        model.getCurrentAttempt(), elapsedSeconds
                );
                HighscoreManager.saveScore(
                        playerName,
                        model.getCurrentAttempt(),
                        elapsedSeconds,
                        model.getMaxAttempts()
                );

                view.showWinAlert(playerName, model.getCurrentAttempt(),
                        elapsedSeconds, isNewHighscore);

            } else if (model.isGameOver()) {
                timer.stop();
                view.stopTimer();
                view.disableInput();
                // Bei Verlust kein Highscore-Eintrag
                view.showLossAlert(playerName, model.getSecretCodeAsString(), elapsedSeconds);
            }
        });

        stage.setTitle("Mastermind - " + playerName);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}