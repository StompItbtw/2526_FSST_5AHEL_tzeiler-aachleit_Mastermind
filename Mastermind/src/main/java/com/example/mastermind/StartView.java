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
 * STARTVIEW - Startbildschirm vor dem Spiel.
 * Spieler gibt hier Name und Versuchsanzahl ein.
 */
public class StartView {

    private static final String BG_DARK  = "#1C1C2E";
    private static final String BG_PANEL = "#16213E";
    private static final String ACCENT   = "#7B68EE";
    private static final String TEXT     = "#E0E0FF";

    private TextField nameField;
    private Spinner<Integer> attemptsSpinner;
    private Button startButton;

    private Runnable onStart;

    /**
     * Baut die Start-Szene und gibt sie zurueck.
     */
    public Scene buildScene() {
        VBox root = new VBox(0);
        root.setStyle("-fx-background-color: " + BG_DARK + ";");
        root.setAlignment(Pos.CENTER);

        // Titel
        VBox titleArea = new VBox(6);
        titleArea.setAlignment(Pos.CENTER);
        titleArea.setPadding(new Insets(30, 0, 24, 0));
        titleArea.setStyle("-fx-background-color: " + BG_PANEL + ";");

        Label title = new Label("MASTERMIND");
        title.setFont(Font.font("Monospace", FontWeight.BOLD, 30));
        title.setStyle("-fx-text-fill: " + ACCENT + ";");

        Label sub = new Label("Konfiguriere dein Spiel");
        sub.setFont(Font.font("Monospace", 13));
        sub.setStyle("-fx-text-fill: #8888AA;");

        // 6 Farb-Kreise zur Dekoration
        HBox colorDots = new HBox(10);
        colorDots.setAlignment(Pos.CENTER);
        colorDots.setPadding(new Insets(12, 0, 0, 0));
        for (char c : MastermindModel.ALLOWED_COLORS) {
            Circle dot = new Circle(10);
            dot.setFill(colorFor(c));
            dot.setStroke(Color.web("#FFFFFF", 0.2));
            dot.setStrokeWidth(1.5);
            colorDots.getChildren().add(dot);
        }

        titleArea.getChildren().addAll(title, sub, colorDots);

        // Formular
        VBox form = new VBox(20);
        form.setPadding(new Insets(30, 40, 30, 40));
        form.setAlignment(Pos.CENTER_LEFT);

        // Name-Feld
        Label nameLabel = new Label("Dein Name:");
        nameLabel.setFont(Font.font("Monospace", FontWeight.BOLD, 13));
        nameLabel.setStyle("-fx-text-fill: " + TEXT + ";");

        nameField = new TextField();
        nameField.setPromptText("Name eingeben...");
        nameField.setFont(Font.font("Monospace", 13));
        nameField.setStyle(
                "-fx-background-color: #252540;" +
                        "-fx-text-fill: " + TEXT + ";" +
                        "-fx-prompt-text-fill: #666688;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10 14 10 14;" +
                        "-fx-border-color: #444466;" +
                        "-fx-border-radius: 8;" +
                        "-fx-border-width: 1;"
        );
        nameField.setPrefWidth(300);

        // Versuche-Spinner
        Label attemptsLabel = new Label("Anzahl Versuche:");
        attemptsLabel.setFont(Font.font("Monospace", FontWeight.BOLD, 13));
        attemptsLabel.setStyle("-fx-text-fill: " + TEXT + ";");

        // Spinner: min=1, max=20, default=10, Schritte von 1
        attemptsSpinner = new Spinner<>(1, 20, 10, 1);
        attemptsSpinner.setEditable(true);
        attemptsSpinner.setPrefWidth(120);
        attemptsSpinner.setStyle(
                "-fx-background-color: #252540;" +
                        "-fx-border-color: #444466;" +
                        "-fx-border-radius: 8;"
        );
        attemptsSpinner.getEditor().setFont(Font.font("Monospace", 13));
        attemptsSpinner.getEditor().setStyle(
                "-fx-background-color: #252540;" +
                        "-fx-text-fill: " + TEXT + ";" +
                        "-fx-padding: 8 10 8 10;"
        );

        // Schnellwahl-Buttons fuer haeufige Werte
        HBox quickSelect = new HBox(8);
        quickSelect.setAlignment(Pos.CENTER_LEFT);
        Label quickLabel = new Label("Schnellwahl: ");
        quickLabel.setFont(Font.font("Monospace", 11));
        quickLabel.setStyle("-fx-text-fill: #8888AA;");
        quickSelect.getChildren().add(quickLabel);

        for (int val : new int[]{5, 8, 10, 15}) {
            Button qBtn = new Button(String.valueOf(val));
            qBtn.setFont(Font.font("Monospace", 11));
            qBtn.setStyle(
                    "-fx-background-color: #2A2A4A;" +
                            "-fx-text-fill: #AAAACC;" +
                            "-fx-background-radius: 6;" +
                            "-fx-padding: 4 10 4 10;" +
                            "-fx-cursor: hand;"
            );
            int finalVal = val;
            qBtn.setOnAction(e -> attemptsSpinner.getValueFactory().setValue(finalVal));
            quickSelect.getChildren().add(qBtn);
        }

        // Start-Button
        startButton = new Button("Spiel starten");
        startButton.setFont(Font.font("Monospace", FontWeight.BOLD, 14));
        startButton.setPrefWidth(300);
        startButton.setStyle(
                "-fx-background-color: " + ACCENT + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 12 0 12 0;" +
                        "-fx-cursor: hand;"
        );
        startButton.setOnMouseEntered(e -> startButton.setStyle(
                "-fx-background-color: #9988FF; -fx-text-fill: white;" +
                        "-fx-background-radius: 10; -fx-padding: 12 0 12 0; -fx-cursor: hand;"));
        startButton.setOnMouseExited(e -> startButton.setStyle(
                "-fx-background-color: " + ACCENT + "; -fx-text-fill: white;" +
                        "-fx-background-radius: 10; -fx-padding: 12 0 12 0; -fx-cursor: hand;"));

        startButton.setOnAction(e -> { if (onStart != null) onStart.run(); });

        // Enter-Taste startet auch das Spiel
        nameField.setOnAction(e -> { if (onStart != null) onStart.run(); });

        form.getChildren().addAll(
                nameLabel, nameField,
                attemptsLabel, attemptsSpinner, quickSelect,
                new Separator(),
                startButton
        );

        // Separator stylen
        form.getChildren().stream()
                .filter(n -> n instanceof Separator)
                .forEach(n -> ((Separator) n).setStyle("-fx-background-color: #333355;"));

        root.getChildren().addAll(titleArea, form);
        return new Scene(root, 420, 480);
    }

    /** Gibt den eingegebenen Namen zurueck. Leer = "Spieler". */
    public String getPlayerName() {
        String name = nameField.getText().trim();
        return name.isEmpty() ? "Spieler" : name;
    }

    /** Gibt die gewaehlte Versuchsanzahl zurueck. */
    public int getSelectedAttempts() {
        return attemptsSpinner.getValue();
    }

    public void setOnStart(Runnable h) { this.onStart = h; }

    private Color colorFor(char c) {
        switch (c) {
            case 'R': return Color.web("#E74C3C");
            case 'G': return Color.web("#27AE60");
            case 'B': return Color.web("#2980B9");
            case 'Y': return Color.web("#F1C40F");
            case 'O': return Color.web("#E67E22");
            case 'P': return Color.web("#FF69B4");
            default:  return Color.web("#333355");
        }
    }
}