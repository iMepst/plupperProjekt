module com.haw.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.junit.jupiter.api;


    opens com.haw.main to javafx.fxml;
    exports com.haw.main;
}