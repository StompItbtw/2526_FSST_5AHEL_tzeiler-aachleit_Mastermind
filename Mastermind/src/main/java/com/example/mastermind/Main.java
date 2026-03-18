package com.example.mastermind;

/**
 * EINSTIEGSPUNKT – startet das Programm.
 *
 * Aufgabe: Genau ein Mal alle drei MVC-Objekte erstellen und den Controller starten.
 * Hier steht KEINE Spiellogik und KEINE Ausgabe – das ist Aufgabe von Model/View/Controller.
 *
 * Hinweis: Falls du ein JavaFX-Projekt hast und Launcher.java existiert,
 * rufe dort einfach Main.main(args) auf, oder ersetze den Inhalt
 * von Launcher.java durch diesen Code.
 */
public class Main {

    public static void main(String[] args) {

        // 1. Model erstellen – generiert sofort einen zufälligen geheimen Code
        MastermindModel model = new MastermindModel();

        // 2. View erstellen – initialisiert den Scanner für Benutzereingaben
        MastermindView view = new MastermindView();

        // 3. Controller erstellen – bekommt Model und View übergeben
        MastermindController controller = new MastermindController(model, view);

        // 4. Spiel starten – der Controller übernimmt ab hier alles
        controller.run();
    }
}