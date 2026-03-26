package com.example.mastermind;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Verwaltet das Speichern und Laden der Highscores.
 * Speichert Zeit in Sekunden + Datum/Uhrzeit in eine Textdatei.
 */
public class HighscoreManager {

    private static final String FILE_NAME = "highscores.txt";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    /** Speichert einen neuen Eintrag (nur bei Sieg). */
    public void saveScore(long seconds, int attempts) {
        String date = LocalDateTime.now().format(FORMATTER);
        String line = String.format("%s | Zeit: %ds | Versuche: %d", date, seconds, attempts);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern des Highscores: " + e.getMessage());
        }
    }

    /** Laedt alle Highscore-Eintraege aus der Datei. */
    public List<String> loadScores() {
        List<String> scores = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return scores;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) scores.add(line);
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Laden des Highscores: " + e.getMessage());
        }
        return scores;
    }
}