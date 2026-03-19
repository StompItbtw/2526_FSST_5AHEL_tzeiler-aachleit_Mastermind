package com.example.mastermind;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * CONTROLLER - steuert den Spielablauf.
 * Verwaltet auch den Timer (JavaFX Timeline).
 */
public class MastermindController {

    private final MastermindModel model;
    private final MastermindView  view;
    private final Stage           stage;
    private final String          playerName;

    private final char[] currentGuess = new char[MastermindModel.CODE_LENGTH];
    private int filledSlots = 0;

    // Timer - zaehlt Sekunden seit Spielstart
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
        // Szene aufbauen (mit Spielername und Versuchsanzahl)
        javafx.scene.Scene scene = view.buildScene(playerName, model.getMaxAttempts());

        // ── Timer starten ──────────────────────────────────────────────────
        // JavaFX Timeline: jede Sekunde wird elapsedSeconds erhoeht
        // und der Timer-Label in der View aktualisiert
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            elapsedSeconds++;
            view.updateTimer(elapsedSeconds);
        }));
        timer.setCycleCount(Timeline.INDEFINITE); // laeuft endlos bis gestoppt
        timer.play();

        // ── Farbe gewaehlt ─────────────────────────────────────────────────
        view.setOnColorSelected(colorChar -> {
            if (filledSlots < MastermindModel.CODE_LENGTH
                    && !model.isGameWon() && !model.isGameOver()) {
                currentGuess[filledSlots] = colorChar;
                filledSlots++;
                view.updateCurrentGuess(currentGuess, filledSlots);
            }
        });

        // ── Loeschen gedrueckt ─────────────────────────────────────────────
        view.setOnDelete(() -> {
            if (filledSlots > 0) {
                filledSlots--;
                currentGuess[filledSlots] = 0;
                view.updateCurrentGuess(currentGuess, filledSlots);
            }
        });

        // ── Abschicken gedrueckt ───────────────────────────────────────────
        view.setOnSubmit(() -> {
            if (filledSlots < MastermindModel.CODE_LENGTH) return;
            if (model.isGameWon() || model.isGameOver()) return;

            model.processGuess(new String(currentGuess));
            view.updateBoard(model);

            // Eingabe zuruecksetzen
            filledSlots = 0;
            for (int i = 0; i < MastermindModel.CODE_LENGTH; i++) currentGuess[i] = 0;
            view.updateCurrentGuess(currentGuess, filledSlots);

            // Spielende pruefen
            if (model.isGameWon()) {
                timer.stop();           // Timer anhalten
                view.stopTimer();       // Timer grau faerben
                view.disableInput();
                view.showWinAlert(playerName, model.getCurrentAttempt(), elapsedSeconds);
            } else if (model.isGameOver()) {
                timer.stop();
                view.stopTimer();
                view.disableInput();
                view.showLossAlert(playerName, model.getSecretCodeAsString(), elapsedSeconds);
            }
        });

        stage.setTitle("Mastermind – " + playerName);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}