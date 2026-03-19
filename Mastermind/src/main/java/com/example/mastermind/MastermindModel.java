package com.example.mastermind;

import java.util.Random;

/**
 * MODEL – enthält die gesamte Spiellogik.
 * Hat keine Abhängigkeit zu View oder Controller.
 */
public class MastermindModel {

    public static final char[] ALLOWED_COLORS = {'R', 'G', 'B', 'Y', 'O', 'P'};
    public static final int CODE_LENGTH  = 4;
    public static final int MAX_ATTEMPTS = 10;

    private final char[]   secretCode;
    private final char[][] guessHistory;
    private final String[] resultHistory;
    private int     currentAttempt;
    private boolean gameWon;

    public MastermindModel() {
        this.secretCode    = generateCode();
        this.guessHistory  = new char[MAX_ATTEMPTS][CODE_LENGTH];
        this.resultHistory = new String[MAX_ATTEMPTS];
        this.currentAttempt = 0;
        this.gameWon        = false;
    }

    /** Generiert einen zufälligen 4-stelligen Code (Wiederholungen erlaubt). */
    private char[] generateCode() {
        Random rng  = new Random();
        char[] code = new char[CODE_LENGTH];
        for (int i = 0; i < CODE_LENGTH; i++) {
            code[i] = ALLOWED_COLORS[rng.nextInt(ALLOWED_COLORS.length)];
        }
        return code;
    }

    /** Prüft ob eine Eingabe gültig ist (4 Zeichen, alle aus ALLOWED_COLORS). */
    public boolean isValidInput(String input) {
        if (input == null || input.length() != CODE_LENGTH) return false;
        for (char c : input.toUpperCase().toCharArray()) {
            boolean ok = false;
            for (char a : ALLOWED_COLORS) { if (c == a) { ok = true; break; } }
            if (!ok) return false;
        }
        return true;
    }

    /** Verarbeitet einen Rateversuch: speichert ihn und berechnet die Auswertung. */
    public void processGuess(String input) {
        char[] guess = input.toUpperCase().toCharArray();
        guessHistory[currentAttempt]  = guess;
        String result = evaluate(guess);
        resultHistory[currentAttempt] = result;
        if (result.equals("••••")) gameWon = true;
        currentAttempt++;
    }

    /**
     * Berechnet Auswertung:
     * • = richtige Farbe, richtige Position
     * o = richtige Farbe, falsche Position
     */
    private String evaluate(char[] guess) {
        boolean[] secretUsed = new boolean[CODE_LENGTH];
        boolean[] guessUsed  = new boolean[CODE_LENGTH];
        int exact = 0, color = 0;

        // 1. Exakte Treffer
        for (int i = 0; i < CODE_LENGTH; i++) {
            if (guess[i] == secretCode[i]) {
                exact++;
                secretUsed[i] = guessUsed[i] = true;
            }
        }
        // 2. Farbtreffer an falscher Position
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
        return "•".repeat(exact) + "o".repeat(color);
    }

    // Getter
    public boolean  isGameWon()            { return gameWon; }
    public boolean  isGameOver()           { return currentAttempt >= MAX_ATTEMPTS; }
    public int      getRemainingAttempts() { return MAX_ATTEMPTS - currentAttempt; }
    public int      getCurrentAttempt()   { return currentAttempt; }
    public char[][] getGuessHistory()     { return guessHistory; }
    public String[] getResultHistory()    { return resultHistory; }
    public String   getSecretCodeAsString() { return new String(secretCode); }
}