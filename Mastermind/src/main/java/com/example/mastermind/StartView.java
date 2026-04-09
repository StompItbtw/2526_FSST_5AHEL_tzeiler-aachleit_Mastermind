package com.example.mastermind;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * STARTVIEW - Startbildschirm.
 * Name eingeben, Versuchsanzahl waehlen, Highscores anzeigen.
 */
public class StartView {

    private static final String BG_DARK  = "#1C1C2E";
    private static final String BG_PANEL = "#16213E";
    private static final String ACCENT   = "#7B68EE";
    private static final String TEXT     = "#E0E0FF";
    private static final String GOLD     = "#FFD700";

    private TextField      nameField;
    private Spinner<Integer> attemptsSpinner;
    private Button         startButton;
    private Button         highscoreButton;

    private Runnable onStart;
    private Runnable onShowHighscores;

    public Scene buildScene() {
        VBox root = new VBox(0);
        root.setStyle("-fx-background-color: " + BG_DARK + ";");

        // Titel
        VBox titleArea = new VBox(6);
        titleArea.setAlignment(Pos.CENTER);
        titleArea.setPadding(new Insets(28, 0, 20, 0));
        titleArea.setStyle("-fx-background-color: " + BG_PANEL + ";");

        Label title = new Label("MASTERMIND");
        title.setFont(Font.font("Monospace", FontWeight.BOLD, 30));
        title.setStyle("-fx-text-fill: " + ACCENT + ";");

        Label sub = new Label("Konfiguriere dein Spiel");
        sub.setFont(Font.font("Monospace", 13));
        sub.setStyle("-fx-text-fill: #8888AA;");

        HBox colorDots = new HBox(10);
        colorDots.setAlignment(Pos.CENTER);
        colorDots.setPadding(new Insets(10, 0, 0, 0));
        for (char c : MastermindModel.ALLOWED_COLORS) {
            Circle dot = new Circle(10);
            dot.setFill(colorFor(c));
            dot.setStroke(Color.web("#FFFFFF", 0.2));
            dot.setStrokeWidth(1.5);
            colorDots.getChildren().add(dot);
        }

        titleArea.getChildren().addAll(title, sub, colorDots);

        // Formular
        VBox form = new VBox(16);
        form.setPadding(new Insets(24, 40, 24, 40));

        // Name
        Label nameLabel = new Label("Dein Name:");
        nameLabel.setFont(Font.font("Monospace", FontWeight.BOLD, 13));
        nameLabel.setStyle("-fx-text-fill: " + TEXT + ";");

        nameField = new TextField();
        nameField.setPromptText("Name eingeben...");
        nameField.setFont(Font.font("Monospace", 13));
        nameField.setStyle(
                "-fx-background-color: #252540; -fx-text-fill: " + TEXT + ";" +
                        "-fx-prompt-text-fill: #666688; -fx-background-radius: 8;" +
                        "-fx-padding: 10 14 10 14; -fx-border-color: #444466;" +
                        "-fx-border-radius: 8; -fx-border-width: 1;"
        );
        nameField.setOnAction(e -> { if (onStart != null) onStart.run(); });

        // Spielervorwahl
        HBox Vorwahl = new HBox(8);
        Vorwahl.setAlignment(Pos.CENTER_LEFT);
        Label VorwahlLbl = new Label("Vorwahl: ");
        VorwahlLbl.setFont(Font.font("Monospace", 11));
        VorwahlLbl.setStyle("-fx-text-fill: #8888AA;");
        Vorwahl.getChildren().add(VorwahlLbl);
        for (String preset : new String[]{}) {
            Button pBtn = new Button(preset);
            pBtn.setFont(Font.font("Monospace", 11));
            pBtn.setStyle(
                    "-fx-background-color: #2A2A4A; -fx-text-fill: #AAAACC;" +
                            "-fx-background-radius: 6; -fx-padding: 4 10 4 10; -fx-cursor: hand;"
            );
            pBtn.setOnAction(ev -> nameField.setText(preset));
            Vorwahl.getChildren().add(pBtn);
        }


        // Versuche
        Label attLabel = new Label("Anzahl Versuche:");
        attLabel.setFont(Font.font("Monospace", FontWeight.BOLD, 13));
        attLabel.setStyle("-fx-text-fill: " + TEXT + ";");

        attemptsSpinner = new Spinner<>(1, 20, 10, 1);
        attemptsSpinner.setEditable(true);
        attemptsSpinner.setPrefWidth(120);
        attemptsSpinner.getEditor().setFont(Font.font("Monospace", 13));
        attemptsSpinner.getEditor().setStyle(
                "-fx-background-color: #252540; -fx-text-fill: " + TEXT + "; -fx-padding: 8 10 8 10;"
        );

        // Schnellwahl
        HBox quick = new HBox(8);
        quick.setAlignment(Pos.CENTER_LEFT);
        Label quickLbl = new Label("Schnellwahl: ");
        quickLbl.setFont(Font.font("Monospace", 11));
        quickLbl.setStyle("-fx-text-fill: #8888AA;");
        quick.getChildren().add(quickLbl);
        for (int val : new int[]{5, 8, 10, 15}) {
            Button qBtn = new Button(String.valueOf(val));
            qBtn.setFont(Font.font("Monospace", 11));
            qBtn.setStyle(
                    "-fx-background-color: #2A2A4A; -fx-text-fill: #AAAACC;" +
                            "-fx-background-radius: 6; -fx-padding: 4 10 4 10; -fx-cursor: hand;"
            );
            int v = val;
            qBtn.setOnAction(e -> attemptsSpinner.getValueFactory().setValue(v));
            quick.getChildren().add(qBtn);
        }



        // Trennlinie
        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #333355;");

        // Start-Button
        startButton = new Button("Spiel starten");
        startButton.setFont(Font.font("Monospace", FontWeight.BOLD, 14));
        startButton.setMaxWidth(Double.MAX_VALUE);
        startButton.setStyle(
                "-fx-background-color: " + ACCENT + "; -fx-text-fill: white;" +
                        "-fx-background-radius: 10; -fx-padding: 12 0 12 0; -fx-cursor: hand;"
        );
        startButton.setOnMouseEntered(e -> startButton.setStyle(
                "-fx-background-color: #9988FF; -fx-text-fill: white;" +
                        "-fx-background-radius: 10; -fx-padding: 12 0 12 0; -fx-cursor: hand;"));
        startButton.setOnMouseExited(e -> startButton.setStyle(
                "-fx-background-color: " + ACCENT + "; -fx-text-fill: white;" +
                        "-fx-background-radius: 10; -fx-padding: 12 0 12 0; -fx-cursor: hand;"));
        startButton.setOnAction(e -> { if (onStart != null) onStart.run(); });

        // Highscore-Button
        highscoreButton = new Button("Highscores anzeigen");
        highscoreButton.setFont(Font.font("Monospace", 13));
        highscoreButton.setMaxWidth(Double.MAX_VALUE);
        highscoreButton.setStyle(
                "-fx-background-color: #2A2A4A; -fx-text-fill: " + GOLD + ";" +
                        "-fx-background-radius: 10; -fx-padding: 10 0 10 0; -fx-cursor: hand;" +
                        "-fx-border-color: " + GOLD + "; -fx-border-radius: 10; -fx-border-width: 1;"
        );
        highscoreButton.setOnAction(e -> { if (onShowHighscores != null) onShowHighscores.run(); });

        form.getChildren().addAll(
                nameLabel, nameField, Vorwahl,
                attLabel, attemptsSpinner, quick,
                sep,
                startButton, highscoreButton
        );

        root.getChildren().addAll(titleArea, form);
        return new Scene(root, 420, 520);
    }

    public String  getPlayerName()       { String n = nameField.getText().trim(); return n.isEmpty() ? "Spieler" : n; }
    public int     getSelectedAttempts() { return attemptsSpinner.getValue(); }

    public void setOnStart(Runnable h)           { this.onStart = h; }
    public void setOnShowHighscores(Runnable h)  { this.onShowHighscores = h; }

    private Color colorFor(char c) {
        switch (c) {
            case 'R': return Color.web("#E74C3C"); case 'G': return Color.web("#27AE60");
            case 'B': return Color.web("#2980B9"); case 'Y': return Color.web("#F1C40F");
            case 'O': return Color.web("#E67E22"); case 'P': return Color.web("#FF69B4");
            default:  return Color.web("#333355");
        }
    }
}