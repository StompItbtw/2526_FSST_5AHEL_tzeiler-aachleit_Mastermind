module com.example.mastermind {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.mastermind to javafx.fxml;
    exports com.example.mastermind;
}