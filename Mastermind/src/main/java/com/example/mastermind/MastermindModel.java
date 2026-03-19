package com.example.mastermind;

import java.util.Random;

/**
 * MODEL - enthaelt die gesamte Spiellogik.
 * Hat keine Abhaengigkeit zu View oder Controller.
 */
public class MastermindModel {

    public static final char[] ALLOWED_COLORS = {'R', 'G', 'B', 'Y', 'O', 'P'};
    public static final int CODE_LENGTH = 4;

    // Feedback-Zeichen als Konstanten (ASCII - kein Encoding-Problem)
    public static final char EXACT_CHAR = '*'; // richtige Farbe, richtige Position
    public static final char COLOR_CHAR = 'o'; // richtige Farbe, falsche Position

    private final int      maxAttempts;   // konfigurierbar beim Spielstart
    private final char[]   secretCode;
    private final char[][] guessHistory;
    private final String[] resultHistory;
    private int     currentAttempt;
    private boolean gameWon;

    /**
     * Konstruktor mit konfigurierbarer Versuchsanzahl.
     *
     * @param maxAttempts Anzahl der erlaubten Versuche (z.B. 5, 10, 15)
     */
    public MastermindModel(int maxAttempts) {
        this.maxAttempts    = maxAttempts;
        this.secretCode     = generateCode();
        this.guessHistory   = new char[maxAttempts][CODE_LENGTH];
        this.resultHistory  = new String[maxAttempts];
        this.currentAttempt = 0;
        this.gameWon        = false;
    }

    /**
     * Generiert einen zufaelligen 4-stelligen Code.
     * Farben duerfen sich wiederholen.
     */
    private char[] generateCode() {
        Random rng  = new Random();
        char[] code = new char[CODE_LENGTH];
        for (int i = 0; i < CODE_LENGTH; i++) {
            code[i] = ALLOWED_COLORS[rng.nextInt(ALLOWED_COLORS.length)];
        }
        return code;
    }

    /**
     * Prueft ob eine Eingabe gueltig ist.
     * Gueltig = genau 4 Zeichen, alle aus {R, G, B, Y, O, P}.
     */
    public boolean isValidInput(String input) {
        if (input == null || input.length() != CODE_LENGTH) return false;
        for (char c : input.toUpperCase().toCharArray()) {
            boolean ok = false;
            for (char a : ALLOWED_COLORS) {
                if (c == a) { ok = true; break; }
            }
            if (!ok) return false;
        }
        return true;
    }

    /**
     * Verarbeitet einen Rateversuch:
     * Speichert den Versuch und berechnet die Auswertung.
     */
    public void processGuess(String input) {
        char[] guess = input.toUpperCase().toCharArray();
        guessHistory[currentAttempt] = guess;

        String result = evaluate(guess);
        resultHistory[currentAttempt] = result;

        // Gewonnen wenn alle 4 Positionen korrekt (z.B. "****")
        String winString = String.valueOf(EXACT_CHAR).repeat(CODE_LENGTH);
        if (result.equals(winString)) gameWon = true;

        currentAttempt++;
    }

    /**
     * Berechnet die Auswertung eines Versuchs.
     *
     * * = richtige Farbe, richtige Position
     * o = richtige Farbe, falsche Position
     *
     * Zwei-Durchgang-Algorithmus damit keine Farbe doppelt gezaehlt wird.
     */
    private String evaluate(char[] guess) {
        boolean[] secretUsed = new boolean[CODE_LENGTH];
        boolean[] guessUsed  = new boolean[CODE_LENGTH];
        int exact = 0, color = 0;

        // 1. Durchgang: exakte Treffer zaehlen
        for (int i = 0; i < CODE_LENGTH; i++) {
            if (guess[i] == secretCode[i]) {
                exact++;
                secretUsed[i] = guessUsed[i] = true;
            }
        }

        // 2. Durchgang: Farbtreffer an falscher Position zaehlen
        for (int i = 0; i < CODE_LENGTH; i++) {
            if (guessUsed[i]) continue;
            for (int j = 0; j < CODE_LENGTH; j++) {
                if (secretUsed[j]) continue;
                if (guess[i] == secretCode[j]) {
                    color++;
                    secretUsed[j] = true;
                    break;
                }
            }
        }

        // Ergebnis-String aufbauen (nur ASCII)
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < exact; i++) sb.append(EXACT_CHAR);
        for (int i = 0; i < color; i++) sb.append(COLOR_CHAR);
        return sb.toString();
    }

    // ── Getter ────────────────────────────────────────────────────────────

    public boolean  isGameWon()              { return gameWon; }
    public boolean  isGameOver()             { return currentAttempt >= maxAttempts; }
    public int      getRemainingAttempts()   { return maxAttempts - currentAttempt; }
    public int      getMaxAttempts()         { return maxAttempts; }
    public int      getCurrentAttempt()     { return currentAttempt; }
    public char[][] getGuessHistory()       { return guessHistory; }
    public String[] getResultHistory()      { return resultHistory; }
    public String   getSecretCodeAsString() { return new String(secretCode); }
}