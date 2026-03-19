package com.example.mastermind;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * HIGHSCORE-MANAGER - liest und schreibt Highscores in eine Datei.
 * Gehoert zum Model-Bereich (Datenverwaltung, keine UI).
 *
 * Format der Datei (CSV):
 *   Name;Versuche;Sekunden;MaxVersuche
 *   z.B.: Thomas;7;143;10
 *
 * Sortierung: Weniger Versuche = besser. Bei gleichen Versuchen: schnellere Zeit.
 */
public class HighscoreManager {

    // Datei liegt im Benutzerverzeichnis neben dem Projekt
    private static final String HIGHSCORE_FILE =
            System.getProperty("user.home") + "/mastermind_highscores.txt";
    private static final int    MAX_ENTRIES    = 10; // Nur Top 10 speichern

    /**
     * Repraesentiert einen einzelnen Highscore-Eintrag.
     */
    public static class Entry {
        public final String name;
        public final int    attempts;   // Anzahl benoetigter Versuche
        public final long   seconds;    // Benoetigte Zeit in Sekunden
        public final int    maxAttempts;// Maximale Versuche (Schwierigkeit)

        public Entry(String name, int attempts, long seconds, int maxAttempts) {
            this.name        = name;
            this.attempts    = attempts;
            this.seconds     = seconds;
            this.maxAttempts = maxAttempts;
        }

        /** Gibt die Zeit als MM:SS String zurueck. */
        public String getTimeFormatted() {
            return String.format("%02d:%02d", seconds / 60, seconds % 60);
        }
    }

    /**
     * Speichert einen neuen Highscore-Eintrag in die Datei.
     * Nur Gewinner werden gespeichert.
     *
     * @param name        Spielername
     * @param attempts    Anzahl benoetigter Versuche
     * @param seconds     Benoetigte Zeit in Sekunden
     * @param maxAttempts Maximale Versuche (zeigt Schwierigkeit)
     */
    public static void saveScore(String name, int attempts, long seconds, int maxAttempts) {
        try {
            // Bestehende Eintraege laden
            List<Entry> entries = loadScores();

            // Neuen Eintrag hinzufuegen
            entries.add(new Entry(name, attempts, seconds, maxAttempts));

            // Sortieren: weniger Versuche = besser, bei Gleichstand schnellere Zeit
            entries.sort(Comparator
                    .comparingInt((Entry e) -> e.attempts)
                    .thenComparingLong(e -> e.seconds)
            );

            // Nur Top 10 behalten
            if (entries.size() > MAX_ENTRIES) {
                entries = entries.subList(0, MAX_ENTRIES);
            }

            // In Datei schreiben
            Path path = Path.of(HIGHSCORE_FILE);
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                for (Entry e : entries) {
                    writer.write(e.name + ";" + e.attempts + ";" + e.seconds + ";" + e.maxAttempts);
                    writer.newLine();
                }
            }

        } catch (IOException ex) {
            // Fehler beim Speichern - leise ignorieren (Spiel laeuft trotzdem)
            System.err.println("Highscore konnte nicht gespeichert werden: " + ex.getMessage());
        }
    }

    /**
     * Laedt alle Highscore-Eintraege aus der Datei.
     *
     * @return Liste aller Eintraege, bereits sortiert. Leer wenn keine Datei vorhanden.
     */
    public static List<Entry> loadScores() {
        List<Entry> entries = new ArrayList<>();
        Path path = Path.of(HIGHSCORE_FILE);

        if (!Files.exists(path)) return entries; // Noch keine Datei = leere Liste

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(";");
                if (parts.length < 4) continue; // Ungueltige Zeile ueberspringen

                String name        = parts[0];
                int    attempts    = Integer.parseInt(parts[1]);
                long   seconds     = Long.parseLong(parts[2]);
                int    maxAttempts = Integer.parseInt(parts[3]);

                entries.add(new Entry(name, attempts, seconds, maxAttempts));
            }
        } catch (IOException | NumberFormatException ex) {
            System.err.println("Highscore konnte nicht geladen werden: " + ex.getMessage());
        }

        return entries;
    }

    /**
     * Prueft ob ein Ergebnis in die Top 10 kommen wuerde.
     * Nuetzlich um dem Spieler anzuzeigen ob er einen neuen Highscore hat.
     */
    public static boolean isHighscore(int attempts, long seconds) {
        List<Entry> entries = loadScores();
        if (entries.size() < MAX_ENTRIES) return true; // Noch Platz frei

        // Mit dem schlechtesten Eintrag vergleichen
        Entry worst = entries.get(entries.size() - 1);
        if (attempts < worst.attempts) return true;
        if (attempts == worst.attempts && seconds < worst.seconds) return true;
        return false;
    }
}