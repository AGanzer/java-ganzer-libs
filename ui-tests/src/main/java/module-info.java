module com.example.uitests {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.ganzer.core;
    requires de.ganzer.fx;
    opens com.example.uitests to javafx.fxml;
    opens com.example.uitests.charts to javafx.fxml;
    exports com.example.uitests;
    exports com.example.uitests.charts;
}