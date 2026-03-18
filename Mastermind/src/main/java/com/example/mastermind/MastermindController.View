package com.example.mastermind;

/**
 * CONTROLLER – steuert den gesamten Spielablauf.
 * Verbindet Model und View, enthält selbst keine Logik und keine Ausgaben.
 * Ruft Model-Methoden für Logik und View-Methoden für Ausgabe auf.
 */
public class MastermindController {

    // Referenz auf das Model (Spiellogik)
    private final MastermindModel model;

    // Referenz auf die View (Ein-/Ausgabe)
    private final MastermindView view;

    /**
     * Konstruktor: Erhält Model und View von außen (aus Main.java).
     * Das nennt sich "Dependency Injection" – Controller hängt von nichts ab außer
     * den zwei Objekten die er bekommt.
     *
     * @param model Das Spielmodell
     * @param view  Die Ausgabe-/Eingabe-Klasse
     */
    public MastermindController(MastermindModel model, MastermindView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Startet und steuert den kompletten Spielablauf.
     *
     * Ablauf:
     * 1. Willkommen anzeigen
     * 2. Solange das Spiel läuft:
     *    a) Spielfeld anzeigen
     *    b) Verbleibende Versuche anzeigen
     *    c) Gültige Eingabe vom Spieler holen
     *    d) Versuch im Model verarbeiten (Auswertung berechnen)
     *    e) Prüfen ob gewonnen oder unentschieden
     * 3. Endbedingung ausgeben
     */
    public void run() {
        // Willkommensnachricht und Regeln anzeigen
        view.displayWelcome();

        // Hauptspielschleife – läuft bis gewonnen oder alle Versuche verbraucht
        while (!model.isGameWon() && !model.isGameOver()) {

            // Aktuelles Spielfeld mit bisherigen Versuchen anzeigen
            if (model.getCurrentAttempt() > 0) {
                view.displayBoard(model);
            }

            // Verbleibende Versuche anzeigen
            view.displayRemainingAttempts(model.getRemainingAttempts());

            // Gültige Eingabe vom Spieler holen (View validiert intern mit Model)
            String input = view.readInput(model);

            // Versuch im Model verarbeiten → Auswertung wird berechnet und gespeichert
            model.processGuess(input);
        }

        // Spielfeld ein letztes Mal anzeigen (zeigt auch den letzten Versuch)
        view.displayBoard(model);

        // Spielende auswerten und entsprechende Nachricht anzeigen
        if (model.isGameWon()) {
            // Spieler hat gewonnen
            view.displayWin(model.getCurrentAttempt());
        } else {
            // Alle Versuche verbraucht ohne Lösung → Unentschieden
            view.displayLoss(model.getSecretCodeAsString());
        }

        // Scanner ordentlich schließen
        view.close();
    }
}