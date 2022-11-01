module com.example.tetris {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.datatransfer;
    requires java.desktop;
    requires org.junit.jupiter.api;


    opens com.example.lifeGame to javafx.fxml;
    exports com.example.lifeGame;
}