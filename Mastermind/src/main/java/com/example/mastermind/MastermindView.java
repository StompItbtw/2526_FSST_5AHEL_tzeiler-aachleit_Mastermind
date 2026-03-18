package com.example.mastermind;

import java.util.Scanner;

/**
 * VIEW – verantwortlich für die gesamte Ein- und Ausgabe.
 * Kennt das Model NUR um Eingaben zu validieren.
 * Hat keine Spiellogik.
 */
public class MastermindView {

    // Scanner für Benutzereingaben (einmal erstellt, immer wiederverwendet)
    private final Scanner scanner;

    /**
     * Konstruktor: Initialisiert den Scanner.
     */
    public MastermindView() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Zeigt die Willkommensnachricht und Spielregeln an.
     */
    public void displayWelcome() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║          M A S T E R M I N D         ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println();
        System.out.println("Erlaubte Farben: R  G  B  Y  O  P");
        System.out.println("                 Rot Grün Blau Gelb Orange Pink");
        System.out.println("Versuche:        " + MastermindModel.MAX_ATTEMPTS);
        System.out.println("Code-Länge:      " + MastermindModel.CODE_LENGTH + " Farben (Wiederholungen erlaubt)");
        System.out.println("• = richtige Farbe, richtige Position");
        System.out.println("o = richtige Farbe, falsche Position");
        System.out.println("════════════════════════════════════════");
        System.out.println();
    }

    /**
     * Zeigt alle bisherigen Versuche mit ihrer Auswertung tabellarisch an.
     * Ähnlich wie im Original-Mastermind-Brettspiel.
     *
     * @param model Das Modell mit der History aller Versuche
     */
    public void displayBoard(MastermindModel model) {
        int attempts = model.getCurrentAttempt();
        char[][] guessHistory = model.getGuessHistory();
        String[] resultHistory = model.getResultHistory();

        System.out.println();
        System.out.println("  #  │ Versuch │ Auswertung");
        System.out.println("─────┼─────────┼───────────");

        for (int i = 0; i < attempts; i++) {
            // Versuchsnummer (1-basiert), linksbündig formatiert
            System.out.printf("  %-2d │ ", i + 1);

            // Die 4 Farbbuchstaben des Versuchs mit Leerzeichen
            for (char c : guessHistory[i]) {
                System.out.print(c + " ");
            }

            // Auswertung (• und o), oder "-" wenn keine Treffer
            String result = resultHistory[i];
            System.out.print("│ ");
            if (result == null || result.isEmpty()) {
                System.out.println("-");
            } else {
                System.out.println(result);
            }
        }

        System.out.println("─────┴─────────┴───────────");
        System.out.println();
    }

    /**
     * Zeigt die Anzahl der verbleibenden Versuche an.
     *
     * @param remaining Anzahl der verbleibenden Versuche
     */
    public void displayRemainingAttempts(int remaining) {
        System.out.println("Verbleibende Versuche: " + remaining);
    }

    /**
     * Liest eine gültige Farbeingabe vom Spieler ein.
     * Wiederholt die Aufforderung solange bis eine gültige Eingabe kommt.
     * Gültig = 4 Zeichen, alle aus {R, G, B, Y, O, P}.
     *
     * @param model Das Modell zur Validierung der Eingabe
     * @return Die gültige Eingabe als uppercase String (z.B. "RGBY")
     */
    public String readInput(MastermindModel model) {
        String input;

        while (true) {
            System.out.print("Dein Versuch (z.B. RGBY): ");
            input = scanner.nextLine().trim().toUpperCase();

            // Modell prüft ob die Eingabe gültig ist
            if (model.isValidInput(input)) {
                return input;  // gültige Eingabe → zurückgeben
            }

            // Ungültige Eingabe → Fehlermeldung und nochmal fragen
            System.out.println("  ✗ Ungültige Eingabe! Bitte genau 4 Buchstaben aus: R G B Y O P");
        }
    }

    /**
     * Zeigt eine Gewinnnachricht an.
     *
     * @param attempts Anzahl der benötigten Versuche
     */
    public void displayWin(int attempts) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   🎉  GEWONNEN! Herzlichen Glückwunsch!  ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println("Du hast den Code in " + attempts + " Versuch(en) erraten!");
        System.out.println();
    }

    /**
     * Zeigt eine Verlustnachricht mit dem aufgelösten Code an.
     *
     * @param secretCode Der geheime Code (wird am Ende aufgedeckt)
     */
    public void displayLoss(String secretCode) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   ✗  UNENTSCHIEDEN – Versuche aufgebraucht  ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println("Der geheime Code war: " + secretCode);
        System.out.println();
    }

    /**
     * Schließt den Scanner am Ende des Programms.
     * Sollte immer aufgerufen werden wenn das Spiel endet.
     */
    public void close() {
        scanner.close();
    }
}