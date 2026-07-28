package de.ganzer.swing.controls;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GComboBoxTest {
    @Test
    void getSelectedItem() {
        GComboBox<String> cb = new GComboBox<>();
        cb.addItem("a");
        cb.setSelectedItem("a");

        // This test is simple to check whether no compie error occurs:
        String item = cb.getSelectedItem();
        assertEquals("a", item);
    }
}