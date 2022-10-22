module com.example.tetris {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.lifeGame to javafx.fxml;
    exports com.example.lifeGame;
}