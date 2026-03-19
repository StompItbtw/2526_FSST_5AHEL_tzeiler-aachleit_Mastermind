package com.example.mastermind;

import javafx.stage.Stage;

/**
 * CONTROLLER – steuert den Spielablauf.
 * Verbindet Model und View: hört auf View-Events, aktualisiert Model, aktualisiert View.
 * Enthält selbst keine Logik und keine Ausgaben.
 */
public class MastermindController {

    private final MastermindModel model;
    private final MastermindView view;
    private final Stage stage;

    // Der aktuell eingegebene Versuch (wird Slot für Slot befüllt)
    private final char[] currentGuess = new char[MastermindModel.CODE_LENGTH];

    // Wie viele Slots bereits befüllt sind (0–4)
    private int filledSlots = 0;

    /**
     * Konstruktor – erhält alle drei Abhängigkeiten von HelloApplication.
     */
    public MastermindController(MastermindModel model, MastermindView view, Stage stage) {
        this.model = model;
        this.view  = view;
        this.stage = stage;
    }

    /**
     * Startet das Spiel:
     * 1. Szene aus der View holen
     * 2. Event-Handler auf View-Buttons setzen
     * 3. Stage anzeigen
     */
    public void run() {
        // Szene bauen
        javafx.scene.Scene scene = view.buildScene();

        // ── Callback: Spieler wählt eine Farbe ────────────────────────────
        view.setOnColorSelected(colorChar -> {
            // Nur wenn noch Slots frei und Spiel noch läuft
            if (filledSlots < MastermindModel.CODE_LENGTH && !model.isGameWon() && !model.isGameOver()) {
                currentGuess[filledSlots] = colorChar;
                filledSlots++;
                // View aktualisieren: zeige aktuelle Farbauswahl in den 4 Slots
                view.updateCurrentGuess(currentGuess, filledSlots);
            }
        });

        // ── Callback: Spieler drückt "Löschen" ────────────────────────────
        view.setOnDelete(() -> {
            if (filledSlots > 0) {
                filledSlots--;
                currentGuess[filledSlots] = 0; // Slot leeren
                view.updateCurrentGuess(currentGuess, filledSlots);
            }
        });

        // ── Callback: Spieler drückt "Abschicken" ─────────────────────────
        view.setOnSubmit(() -> {
            // Nur abschicken wenn alle 4 Slots gefüllt und Spiel noch läuft
            if (filledSlots < MastermindModel.CODE_LENGTH) return;
            if (model.isGameWon() || model.isGameOver()) return;

            // Versuch als String zusammenbauen und im Model verarbeiten
            String guess = new String(currentGuess);
            model.processGuess(guess);

            // Spielfeld in der View aktualisieren
            view.updateBoard(model);

            // Aktuelle Eingabe zurücksetzen
            filledSlots = 0;
            for (int i = 0; i < MastermindModel.CODE_LENGTH; i++) currentGuess[i] = 0;
            view.updateCurrentGuess(currentGuess, filledSlots);

            // Spielende prüfen
            if (model.isGameWon()) {
                view.disableInput();
                view.showWinAlert(model.getCurrentAttempt());
            } else if (model.isGameOver()) {
                view.disableInput();
                view.showLossAlert(model.getSecretCodeAsString());
            }
        });

        // Stage konfigurieren und anzeigen
        stage.setTitle("Mastermind");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
