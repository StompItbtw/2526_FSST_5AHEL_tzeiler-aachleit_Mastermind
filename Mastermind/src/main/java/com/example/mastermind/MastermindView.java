package com.example.mastermind;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.function.Consumer;

/**
 * VIEW - JavaFX-Spieloberflaeche mit Timer und Spielername.
 */
public class MastermindView {

    private static final String BG_DARK   = "#1C1C2E";
    private static final String BG_ROW    = "#252540";
    private static final String BG_PANEL  = "#16213E";
    private static final String COLOR_TEXT= "#E0E0FF";
    private static final String ACCENT    = "#7B68EE";
    private static final String EMPTY_PEG = "#333355";

    private VBox   boardContainer;
    private HBox   currentGuessDisplay;
    private Label  remainingLabel;
    private Label  timerLabel;
    private Button submitButton;
    private Button deleteButton;

    private Consumer<Character> onColorSelected;
    private Runnable onSubmit;
    private Runnable onDelete;

    public Scene buildScene(String playerName, int maxAttempts) {
        VBox root = new VBox(0);
        root.setStyle("-fx-background-color: " + BG_DARK + ";");
        root.getChildren().add(buildTitleArea(playerName));

        boardContainer = new VBox(6);
        boardContainer.setPadding(new Insets(10, 20, 10, 20));
        boardContainer.setStyle("-fx-background-color: " + BG_DARK + ";");
        for (int i = 0; i < maxAttempts; i++) {
            boardContainer.getChildren().add(buildEmptyRow(i + 1));
        }

        ScrollPane scroll = new ScrollPane(boardContainer);
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(380);
        scroll.setStyle(
                "-fx-background: " + BG_DARK + "; -fx-background-color: " + BG_DARK + ";" +
                        "-fx-border-color: transparent;"
        );
        VBox.setVgrow(scroll, Priority.ALWAYS);

        root.getChildren().addAll(scroll, buildBottomPanel(maxAttempts));
        return new Scene(root, 480, 720);
    }

    private VBox buildTitleArea(String playerName) {
        VBox area = new VBox(4);
        area.setAlignment(Pos.CENTER);
        area.setPadding(new Insets(14, 16, 12, 16));
        area.setStyle("-fx-background-color: " + BG_PANEL + ";");

        Label title = new Label("MASTERMIND");
        title.setFont(Font.font("Monospace", FontWeight.BOLD, 24));
        title.setStyle("-fx-text-fill: " + ACCENT + ";");

        // Spielername links, Timer rechts
        HBox infoBar = new HBox();
        infoBar.setAlignment(Pos.CENTER_LEFT);
        infoBar.setPadding(new Insets(4, 0, 4, 0));

        Label nameLabel = new Label("Spieler: " + playerName);
        nameLabel.setFont(Font.font("Monospace", FontWeight.BOLD, 12));
        nameLabel.setStyle("-fx-text-fill: #AAAACC;");
        HBox.setHgrow(nameLabel, Priority.ALWAYS);

        timerLabel = new Label("Zeit: 00:00");
        timerLabel.setFont(Font.font("Monospace", FontWeight.BOLD, 12));
        timerLabel.setStyle("-fx-text-fill: #7BC67B;");

        infoBar.getChildren().addAll(nameLabel, timerLabel);

        // Legende mit echten Kreisen
        HBox legend = new HBox(20);
        legend.setAlignment(Pos.CENTER);
        legend.setPadding(new Insets(6, 0, 0, 0));

        Circle blackPeg = new Circle(7);
        blackPeg.setFill(Color.web("#111111"));
        blackPeg.setStroke(Color.web("#999999")); blackPeg.setStrokeWidth(1);
        Label exactLbl = new Label("= richtige Position");
        exactLbl.setFont(Font.font("Monospace", 11));
        exactLbl.setStyle("-fx-text-fill: #CCCCCC;");
        HBox exactBox = new HBox(6, blackPeg, exactLbl);
        exactBox.setAlignment(Pos.CENTER_LEFT);

        Circle whitePeg = new Circle(7);
        whitePeg.setFill(Color.web("#EEEEEE"));
        whitePeg.setStroke(Color.web("#AAAAAA")); whitePeg.setStrokeWidth(1);
        Label colorLbl = new Label("= falsche Position");
        colorLbl.setFont(Font.font("Monospace", 11));
        colorLbl.setStyle("-fx-text-fill: #CCCCCC;");
        HBox colorBox = new HBox(6, whitePeg, colorLbl);
        colorBox.setAlignment(Pos.CENTER_LEFT);

        legend.getChildren().addAll(exactBox, colorBox);
        area.getChildren().addAll(title, infoBar, legend);
        return area;
    }

    private HBox buildEmptyRow(int rowNumber) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(7, 14, 7, 14));
        row.setStyle("-fx-background-color: " + BG_ROW + "; -fx-background-radius: 8; -fx-opacity: 0.35;");

        Label num = new Label(String.format("%2d", rowNumber));
        num.setMinWidth(22); num.setFont(Font.font("Monospace", 12));
        num.setStyle("-fx-text-fill: #555577;");

        HBox pegs = new HBox(8);
        for (int i = 0; i < MastermindModel.CODE_LENGTH; i++) pegs.getChildren().add(makePeg(null));

        Rectangle sep = new Rectangle(2, 28);
        sep.setFill(Color.web("#333355"));

        row.getChildren().addAll(num, pegs, sep, makeEmptyFeedback());
        return row;
    }

    private HBox buildFilledRow(int rowNumber, char[] guess, String result) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(7, 14, 7, 14));
        row.setStyle("-fx-background-color: " + BG_ROW + "; -fx-background-radius: 8;");

        Label num = new Label(String.format("%2d", rowNumber));
        num.setMinWidth(22); num.setFont(Font.font("Monospace", 12));
        num.setStyle("-fx-text-fill: #8888AA;");

        HBox pegs = new HBox(8);
        for (char c : guess) pegs.getChildren().add(makePeg(c));

        Rectangle sep = new Rectangle(2, 28);
        sep.setFill(Color.web("#444466"));

        row.getChildren().addAll(num, pegs, sep, makeFilledFeedback(result));
        return row;
    }

    private VBox buildBottomPanel(int maxAttempts) {
        VBox panel = new VBox(12);
        panel.setPadding(new Insets(14, 20, 18, 20));
        panel.setStyle("-fx-background-color: " + BG_PANEL + "; -fx-border-color: #333355; -fx-border-width: 1 0 0 0;");

        Label guessTitle = new Label("Dein Versuch:");
        guessTitle.setFont(Font.font("Monospace", FontWeight.BOLD, 13));
        guessTitle.setStyle("-fx-text-fill: " + COLOR_TEXT + ";");

        currentGuessDisplay = new HBox(10);
        currentGuessDisplay.setAlignment(Pos.CENTER_LEFT);
        for (int i = 0; i < MastermindModel.CODE_LENGTH; i++) currentGuessDisplay.getChildren().add(makePeg(null));

        remainingLabel = new Label("Verbleibende Versuche: " + maxAttempts);
        remainingLabel.setFont(Font.font("Monospace", 12));
        remainingLabel.setStyle("-fx-text-fill: #8888AA;");

        Label colorTitle = new Label("Farbe waehlen:");
        colorTitle.setFont(Font.font("Monospace", FontWeight.BOLD, 13));
        colorTitle.setStyle("-fx-text-fill: " + COLOR_TEXT + ";");

        HBox colorBtns = new HBox(10);
        for (char c : MastermindModel.ALLOWED_COLORS) colorBtns.getChildren().add(makeColorButton(c));

        submitButton = new Button("Abschicken");
        submitButton.setFont(Font.font("Monospace", FontWeight.BOLD, 13));
        submitButton.setStyle("-fx-background-color: " + ACCENT + "; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 9 18 9 18; -fx-cursor: hand;");
        submitButton.setOnMouseEntered(e -> submitButton.setStyle("-fx-background-color: #9988FF; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 9 18 9 18; -fx-cursor: hand;"));
        submitButton.setOnMouseExited(e  -> submitButton.setStyle("-fx-background-color: " + ACCENT + "; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 9 18 9 18; -fx-cursor: hand;"));

        deleteButton = new Button("Loeschen");
        deleteButton.setFont(Font.font("Monospace", 13));
        deleteButton.setStyle("-fx-background-color: #3A3A5C; -fx-text-fill: " + COLOR_TEXT + "; -fx-background-radius: 8; -fx-padding: 9 18 9 18; -fx-cursor: hand;");

        submitButton.setOnAction(e -> { if (onSubmit != null) onSubmit.run(); });
        deleteButton.setOnAction(e -> { if (onDelete != null) onDelete.run(); });

        HBox ctrlBtns = new HBox(10, submitButton, deleteButton);
        panel.getChildren().addAll(guessTitle, currentGuessDisplay, remainingLabel, colorTitle, colorBtns, ctrlBtns);
        return panel;
    }

    // ── Peg / Feedback ──────────────────────────────────────────────────────

    private Circle makePeg(Character c) {
        Circle circle = new Circle(17);
        if (c == null) {
            circle.setFill(Color.web(EMPTY_PEG));
            circle.setStroke(Color.web("#444466")); circle.setStrokeWidth(1.5);
        } else {
            circle.setFill(colorFor(c));
            circle.setStroke(Color.web("#FFFFFF", 0.2)); circle.setStrokeWidth(1.5);
        }
        return circle;
    }

    private GridPane makeEmptyFeedback() {
        GridPane g = new GridPane();
        g.setHgap(4); g.setVgap(4); g.setPadding(new Insets(0, 0, 0, 8));
        for (int r = 0; r < 2; r++) for (int c = 0; c < 2; c++) {
            Circle ci = new Circle(6);
            ci.setFill(Color.web(EMPTY_PEG)); ci.setStroke(Color.web("#444466")); ci.setStrokeWidth(1);
            g.add(ci, c, r);
        }
        return g;
    }

    private GridPane makeFilledFeedback(String result) {
        GridPane g = new GridPane();
        g.setHgap(4); g.setVgap(4); g.setPadding(new Insets(0, 0, 0, 8));
        int idx = 0;
        for (int r = 0; r < 2; r++) {
            for (int c = 0; c < 2; c++) {
                Circle ci = new Circle(6);
                if (idx < result.length()) {
                    char fb = result.charAt(idx);
                    if (fb == MastermindModel.EXACT_CHAR) {
                        ci.setFill(Color.web("#111111")); ci.setStroke(Color.web("#999999")); ci.setStrokeWidth(1);
                    } else if (fb == MastermindModel.COLOR_CHAR) {
                        ci.setFill(Color.web("#EEEEEE")); ci.setStroke(Color.web("#AAAAAA")); ci.setStrokeWidth(1);
                    } else { ci.setFill(Color.web(EMPTY_PEG)); }
                } else { ci.setFill(Color.web(EMPTY_PEG)); ci.setStroke(Color.web("#444466")); ci.setStrokeWidth(1); }
                g.add(ci, c, r); idx++;
            }
        }
        return g;
    }

    private Button makeColorButton(char colorChar) {
        Circle circle = new Circle(19);
        circle.setFill(colorFor(colorChar));
        circle.setStroke(Color.web("#FFFFFF", 0.3)); circle.setStrokeWidth(2);
        Label lbl = new Label(String.valueOf(colorChar));
        lbl.setFont(Font.font("Monospace", FontWeight.BOLD, 12));
        lbl.setStyle("-fx-text-fill: white;"); lbl.setMouseTransparent(true);
        StackPane stack = new StackPane(circle, lbl);
        Button btn = new Button();
        btn.setGraphic(stack);
        btn.setStyle("-fx-background-color: transparent; -fx-padding: 4; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> circle.setOpacity(0.75));
        btn.setOnMouseExited(e  -> circle.setOpacity(1.0));
        btn.setOnAction(e -> { if (onColorSelected != null) onColorSelected.accept(colorChar); });
        btn.setTooltip(new Tooltip(colorName(colorChar)));
        return btn;
    }

    // ── Oeffentliche Update-Methoden ──────────────────────────────────────

    public void updateBoard(MastermindModel model) {
        int attempts = model.getCurrentAttempt();
        boardContainer.getChildren().clear();
        for (int i = 0; i < attempts; i++)
            boardContainer.getChildren().add(buildFilledRow(i + 1, model.getGuessHistory()[i], model.getResultHistory()[i]));
        for (int i = attempts; i < model.getMaxAttempts(); i++)
            boardContainer.getChildren().add(buildEmptyRow(i + 1));
        remainingLabel.setText("Verbleibende Versuche: " + model.getRemainingAttempts());
    }

    public void updateCurrentGuess(char[] currentGuess, int filledSlots) {
        currentGuessDisplay.getChildren().clear();
        for (int i = 0; i < MastermindModel.CODE_LENGTH; i++)
            currentGuessDisplay.getChildren().add(i < filledSlots ? makePeg(currentGuess[i]) : makePeg(null));
    }

    public void updateTimer(long seconds) {
        timerLabel.setText(String.format("Zeit: %02d:%02d", seconds / 60, seconds % 60));
        timerLabel.setStyle("-fx-text-fill: #7BC67B;");
    }

    public void stopTimer() {
        timerLabel.setStyle("-fx-text-fill: #8888AA;");
    }

    /**
     * Zeigt Gewinn-Dialog - mit Highscore-Hinweis falls neuer Rekord.
     */
    public void showWinAlert(String playerName, int attempts, long seconds, boolean isNewHighscore) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Gewonnen!");
        a.setHeaderText("Herzlichen Glueckwunsch, " + playerName + "!");
        String msg = "Code erraten in " + attempts + " Versuch(en)!\n" +
                "Benoetigt: " + String.format("%02d:%02d", seconds / 60, seconds % 60);
        if (isNewHighscore) msg += "\n\nNeuer Highscore! Du bist in den Top 10!";
        a.setContentText(msg);
        a.showAndWait();
    }

    public void showLossAlert(String playerName, String secretCode, long seconds) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Unentschieden");
        a.setHeaderText("Alle Versuche aufgebraucht, " + playerName + "!");
        a.setContentText("Der geheime Code war: " + secretCode + "\n" +
                "Gespielte Zeit: " + String.format("%02d:%02d", seconds / 60, seconds % 60));
        a.showAndWait();
    }

    public void disableInput() { submitButton.setDisable(true); deleteButton.setDisable(true); }

    private Color colorFor(char c) {
        switch (c) {
            case 'R': return Color.web("#E74C3C"); case 'G': return Color.web("#27AE60");
            case 'B': return Color.web("#2980B9"); case 'Y': return Color.web("#F1C40F");
            case 'O': return Color.web("#E67E22"); case 'P': return Color.web("#FF69B4");
            default:  return Color.web(EMPTY_PEG);
        }
    }

    private String colorName(char c) {
        switch (c) {
            case 'R': return "Rot";    case 'G': return "Gruen";
            case 'B': return "Blau";   case 'Y': return "Gelb";
            case 'O': return "Orange"; case 'P': return "Pink";
            default:  return "?";
        }
    }

    public void setOnColorSelected(Consumer<Character> h) { this.onColorSelected = h; }
    public void setOnSubmit(Runnable h)                   { this.onSubmit = h; }
    public void setOnDelete(Runnable h)                   { this.onDelete = h; }
}