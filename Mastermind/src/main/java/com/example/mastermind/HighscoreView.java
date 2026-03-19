package com.example.mastermind;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.List;

/**
 * HIGHSCORE-VIEW - zeigt die Top-10 Highscore-Tabelle an.
 * Wird als eigenes Fenster (Stage) oder eingebettet verwendet.
 */
public class HighscoreView {

    private static final String BG_DARK  = "#1C1C2E";
    private static final String BG_ROW   = "#252540";
    private static final String BG_PANEL = "#16213E";
    private static final String ACCENT   = "#7B68EE";
    private static final String TEXT     = "#E0E0FF";
    private static final String GOLD     = "#FFD700";
    private static final String SILVER   = "#C0C0C0";
    private static final String BRONZE   = "#CD7F32";

    private Runnable onBack;

    /**
     * Baut die Highscore-Szene und gibt sie zurueck.
     */
    public Scene buildScene() {
        VBox root = new VBox(0);
        root.setStyle("-fx-background-color: " + BG_DARK + ";");

        // Titel
        VBox titleArea = new VBox(4);
        titleArea.setAlignment(Pos.CENTER);
        titleArea.setPadding(new Insets(20, 0, 16, 0));
        titleArea.setStyle("-fx-background-color: " + BG_PANEL + ";");

        Label title = new Label("HIGHSCORES");
        title.setFont(Font.font("Monospace", FontWeight.BOLD, 26));
        title.setStyle("-fx-text-fill: " + GOLD + ";");

        Label sub = new Label("Top 10 - Beste Ergebnisse");
        sub.setFont(Font.font("Monospace", 12));
        sub.setStyle("-fx-text-fill: #8888AA;");

        titleArea.getChildren().addAll(title, sub);

        // Tabellen-Kopfzeile
        HBox header = buildHeaderRow();

        // Tabellen-Inhalt
        VBox tableContent = new VBox(4);
        tableContent.setPadding(new Insets(10, 20, 10, 20));

        List<HighscoreManager.Entry> scores = HighscoreManager.loadScores();

        if (scores.isEmpty()) {
            // Noch keine Highscores vorhanden
            Label empty = new Label("Noch keine Highscores vorhanden.\nGewinne ein Spiel um dich einzutragen!");
            empty.setFont(Font.font("Monospace", 13));
            empty.setStyle("-fx-text-fill: #666688;");
            empty.setAlignment(Pos.CENTER);
            empty.setPadding(new Insets(30, 0, 30, 0));
            tableContent.setAlignment(Pos.CENTER);
            tableContent.getChildren().add(empty);
        } else {
            // Eintraege anzeigen
            for (int i = 0; i < scores.size(); i++) {
                tableContent.getChildren().add(buildScoreRow(i + 1, scores.get(i)));
            }
        }

        // Zurueck-Button
        Button backBtn = new Button("Zurueck");
        backBtn.setFont(Font.font("Monospace", FontWeight.BOLD, 13));
        backBtn.setStyle(
                "-fx-background-color: " + ACCENT + "; -fx-text-fill: white;" +
                        "-fx-background-radius: 8; -fx-padding: 10 24 10 24; -fx-cursor: hand;"
        );
        backBtn.setOnAction(e -> { if (onBack != null) onBack.run(); });

        HBox btnBox = new HBox(backBtn);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setPadding(new Insets(16, 0, 20, 0));

        root.getChildren().addAll(titleArea, header, tableContent, btnBox);
        return new Scene(root, 480, 520);
    }

    /** Baut die Kopfzeile der Tabelle. */
    private HBox buildHeaderRow() {
        HBox row = new HBox();
        row.setPadding(new Insets(8, 20, 8, 20));
        row.setStyle(
                "-fx-background-color: #1A1A30;" +
                        "-fx-border-color: #333355; -fx-border-width: 1 0 1 0;"
        );

        Label rankLbl    = makeHeaderLabel("#",       50);
        Label nameLbl    = makeHeaderLabel("Name",   140);
        Label attLbl     = makeHeaderLabel("Versuche", 90);
        Label timeLbl    = makeHeaderLabel("Zeit",     90);
        Label maxAttLbl  = makeHeaderLabel("Max",      70);

        row.getChildren().addAll(rankLbl, nameLbl, attLbl, timeLbl, maxAttLbl);
        return row;
    }

    private Label makeHeaderLabel(String text, double width) {
        Label lbl = new Label(text);
        lbl.setMinWidth(width);
        lbl.setFont(Font.font("Monospace", FontWeight.BOLD, 12));
        lbl.setStyle("-fx-text-fill: #8888AA;");
        return lbl;
    }

    /** Baut eine einzelne Score-Zeile. */
    private HBox buildScoreRow(int rank, HighscoreManager.Entry entry) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(8, 6, 8, 6));
        row.setStyle(
                "-fx-background-color: " + BG_ROW + ";" +
                        "-fx-background-radius: 8;"
        );

        // Rang-Farbe: Gold/Silber/Bronze fuer Top 3
        String rankColor;
        String rankText;
        switch (rank) {
            case 1: rankColor = GOLD;   rankText = "1."; break;
            case 2: rankColor = SILVER; rankText = "2."; break;
            case 3: rankColor = BRONZE; rankText = "3."; break;
            default: rankColor = "#666688"; rankText = rank + "."; break;
        }

        Label rankLbl = new Label(rankText);
        rankLbl.setMinWidth(50);
        rankLbl.setFont(Font.font("Monospace", FontWeight.BOLD, 13));
        rankLbl.setStyle("-fx-text-fill: " + rankColor + ";");

        Label nameLbl = new Label(entry.name);
        nameLbl.setMinWidth(140);
        nameLbl.setFont(Font.font("Monospace", FontWeight.BOLD, 13));
        nameLbl.setStyle("-fx-text-fill: " + TEXT + ";");

        // Versuche als Balken-Visualisierung
        Label attLbl = new Label(entry.attempts + " Versuche");
        attLbl.setMinWidth(90);
        attLbl.setFont(Font.font("Monospace", 12));
        attLbl.setStyle("-fx-text-fill: #AAAACC;");

        Label timeLbl = new Label(entry.getTimeFormatted());
        timeLbl.setMinWidth(90);
        timeLbl.setFont(Font.font("Monospace", 12));
        timeLbl.setStyle("-fx-text-fill: #7BC67B;");

        Label maxLbl = new Label("/" + entry.maxAttempts);
        maxLbl.setMinWidth(70);
        maxLbl.setFont(Font.font("Monospace", 11));
        maxLbl.setStyle("-fx-text-fill: #666688;");

        row.getChildren().addAll(rankLbl, nameLbl, attLbl, timeLbl, maxLbl);
        return row;
    }

    public void setOnBack(Runnable h) { this.onBack = h; }
}