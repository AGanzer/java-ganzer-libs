module com.example.uitests {
    requires javafx.controls;
    requires javafx.fxml;
    requires ganzer.core.main;
    requires de.ganzer.fx;
    opens com.example.uitests to javafx.fxml;
    exports com.example.uitests;
}