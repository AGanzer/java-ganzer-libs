package com.example.uitests.swing;

import java.awt.*;

public class SwingTestApp {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            var frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
