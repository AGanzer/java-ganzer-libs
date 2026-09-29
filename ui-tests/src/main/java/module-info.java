open module com.example.uitests {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.ganzer.core;
    requires de.ganzer.fx;
    requires java.desktop;
    requires de.ganzer.swing;
    requires com.formdev.flatlaf;
    requires com.formdev.flatlaf.intellijthemes;
    requires com.kitfox.svg;
    requires batik.dom;
    requires batik.svggen;
    requires swingx.all;
    requires de.ganzer.dv;
    requires de.ganzer.swing.dlgfw;
    exports com.example.uitests.fx;
    exports com.example.uitests.fx.charts;
    exports com.example.uitests.swing;
}