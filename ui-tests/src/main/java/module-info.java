module com.example.uitests {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.ganzer.core;
    requires de.ganzer.fx;
    requires java.desktop;
    requires de.ganzer.swing;
    requires com.formdev.flatlaf;
    requires com.formdev.flatlaf.intellijthemes;
    opens com.example.uitests.fx to javafx.fxml;
    opens com.example.uitests.fx.charts to javafx.fxml;
    exports com.example.uitests.fx;
    exports com.example.uitests.fx.charts;
    exports com.example.uitests.swing;
}