package com.example.mastermind;

import java.util.Random;

/**
 * MODEL – enthält die gesamte Spiellogik.
 * Hat keine Abhängigkeit zu View oder Controller.
 */
public class MastermindModel {

    // Erlaubte Farben (R=Rot, G=Grün, B=Blau, Y=Gelb, O=Orange, P=Pink)
    public static final char[] ALLOWED_COLORS = {'R', 'G', 'B', 'Y', 'O', 'P'};

    // Länge des geheimen Codes
    public static final int CODE_LENGTH = 4;

    // Maximale Anzahl an Versuchen
    public static final int MAX_ATTEMPTS = 10;

    // Der geheime Code als char-Array
    private final char[] secretCode;

    // Speichert alle bisherigen Versuche des Spielers
    private final char[][] guessHistory;

    // Speichert die Auswertung (•/o) zu jedem Versuch
    private final String[] resultHistory;

    // Zählt die bisherigen Versuche
    private int currentAttempt;

    // Gibt an ob der Spieler gewonnen hat
    private boolean gameWon;

    /**
     * Konstruktor: Initialisiert das Modell und generiert einen neuen geheimen Code.
     */
    public MastermindModel() {
        this.secretCode = generateCode();
        this.guessHistory = new char[MAX_ATTEMPTS][CODE_LENGTH];
        this.resultHistory = new String[MAX_ATTEMPTS];
        this.currentAttempt = 0;
        this.gameWon = false;
    }

    /**
     * Generiert einen zufälligen 4-stelligen Code aus den erlaubten Farben.
     * Farben dürfen sich wiederholen.
     *
     * @return char-Array mit dem geheimen Code
     */
    private char[] generateCode() {
        Random random = new Random();
        char[] code = new char[CODE_LENGTH];
        for (int i = 0; i < CODE_LENGTH; i++) {
            // Zufälligen Index aus ALLOWED_COLORS wählen
            code[i] = ALLOWED_COLORS[random.nextInt(ALLOWED_COLORS.length)];
        }
        return code;
    }

    /**
     * Überprüft ob eine Eingabe gültig ist.
     * Gültig = genau 4 Zeichen, alle aus den erlaubten Farben.
     *
     * @param input Die Eingabe des Spielers als String (z.B. "RGBY")
     * @return true wenn gültig, false sonst
     */
    public boolean isValidInput(String input) {
        // Null oder falsche Länge abfangen
        if (input == null || input.length() != CODE_LENGTH) {
            return false;
        }

        // Jeden Buchstaben auf Gültigkeit prüfen
        for (char c : input.toUpperCase().toCharArray()) {
            boolean found = false;
            for (char allowed : ALLOWED_COLORS) {
                if (c == allowed) {
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        return true;
    }

    /**
     * Verarbeitet einen Rateversuch des Spielers:
     * - Speichert den Versuch in der History
     * - Berechnet die Auswertung (• und o)
     * - Erhöht den Versuchszähler
     *
     * @param input Die gültige Eingabe des Spielers (z.B. "RGBY")
     */
    public void processGuess(String input) {
        char[] guess = input.toUpperCase().toCharArray();

        // Versuch in der History speichern
        guessHistory[currentAttempt] = guess;

        // Auswertung berechnen und speichern
        String result = evaluate(guess);
        resultHistory[currentAttempt] = result;

        // Prüfen ob der Spieler gewonnen hat (alle 4 Positionen korrekt)
        if (result.equals("••••")) {
            gameWon = true;
        }

        currentAttempt++;
    }

    /**
     * Berechnet die Auswertung eines Rateversuchs.
     * • = richtige Farbe an richtiger Position
     * o = richtige Farbe an falscher Position
     *
     * Wichtig: Zuerst alle • zählen, dann alle o – damit keine Farbe doppelt gezählt wird.
     *
     * @param guess Der Rateversuch als char-Array
     * @return String aus • und o (z.B. "••o")
     */
    private String evaluate(char[] guess) {
        // Hilfsvariablen um bereits gezählte Positionen zu markieren
        boolean[] secretUsed = new boolean[CODE_LENGTH];
        boolean[] guessUsed = new boolean[CODE_LENGTH];

        int exactMatches = 0;  // richtige Farbe, richtige Position
        int colorMatches = 0;  // richtige Farbe, falsche Position

        // 1. Durchgang: Exakte Treffer zählen (•)
        for (int i = 0; i < CODE_LENGTH; i++) {
            if (guess[i] == secretCode[i]) {
                exactMatches++;
                secretUsed[i] = true;  // diese Position ist bereits verbraucht
                guessUsed[i] = true;
            }
        }

        // 2. Durchgang: Farbtreffer an falscher Position zählen (o)
        for (int i = 0; i < CODE_LENGTH; i++) {
            if (guessUsed[i]) continue;  // bereits als • gezählt

            for (int j = 0; j < CODE_LENGTH; j++) {
                if (secretUsed[j]) continue;  // bereits verbraucht

                if (guess[i] == secretCode[j]) {
                    colorMatches++;
                    secretUsed[j] = true;  // damit dieselbe Geheimcode-Position nicht 2× zählt
                    break;
                }
            }
        }

        // Ergebnis-String aufbauen (• vor o)
        return "•".repeat(exactMatches) + "o".repeat(colorMatches);
    }

    // ── Getter für Controller und View ──────────────────────────────────────

    /** Gibt zurück ob das Spiel gewonnen wurde. */
    public boolean isGameWon() {
        return gameWon;
    }

    /** Gibt zurück ob alle Versuche verbraucht sind. */
    public boolean isGameOver() {
        return currentAttempt >= MAX_ATTEMPTS;
    }

    /** Gibt die Anzahl der verbleibenden Versuche zurück. */
    public int getRemainingAttempts() {
        return MAX_ATTEMPTS - currentAttempt;
    }

    /** Gibt den aktuellen Versuchszähler zurück (0-basiert). */
    public int getCurrentAttempt() {
        return currentAttempt;
    }

    /** Gibt die bisherigen Rateversuche zurück. */
    public char[][] getGuessHistory() {
        return guessHistory;
    }

    /** Gibt die bisherigen Auswertungen zurück. */
    public String[] getResultHistory() {
        return resultHistory;
    }

    /** Gibt den geheimen Code als String zurück (für Spielende). */
    public String getSecretCodeAsString() {
        return new String(secretCode);
    }
}