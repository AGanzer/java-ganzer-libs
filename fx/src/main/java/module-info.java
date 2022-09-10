module de.ganzer.fx {
    requires javafx.controls;
    requires javafx.fxml;
    requires ganzer.core.main;
    opens de.ganzer.fx to javafx.fxml;
    exports de.ganzer.fx;
    exports de.ganzer.fx.validation;
}