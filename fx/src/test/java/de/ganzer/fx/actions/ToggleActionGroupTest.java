package de.ganzer.fx.actions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ToggleActionGroupTest {
    @Test
    void create() {
        ToggleActionGroup group = new ToggleActionGroup();

        assertNull(group.getSelectedAction());
        assertNull(group.selectedActionProperty().get());
    }

    @Test
    void setDisabled() {
        ToggleActionGroup group = new ToggleActionGroup().addAll(
                new Action().selected(true),
                new Action()
        );

        group.setDisabled(true);
        assertTrue(group.isDisabled());

        group.setDisabled(false);
        assertFalse(group.isDisabled());
    }

    @Test
    void setVisible() {
        ToggleActionGroup group = new ToggleActionGroup().addAll(
                new Action().selected(true),
                new Action()
        );

        group.setVisible(false);
        assertFalse(group.isVisible());

        group.setVisible(true);
        assertTrue(group.isVisible());
    }

    @Test
    void getSelectedAction() {
        Action a1;
        Action a2;

        ToggleActionGroup group = new ToggleActionGroup().addAll(
                a1 = new Action().selected(true),
                a2 = new Action()
        );

        assertEquals(a1, group.getSelectedAction(), "a1 selected");
        assertTrue(a1.isSelected(), "a1 selected");
        assertFalse(a2.isSelected(), "a2 not selected");

        a2.setSelected(true);

        assertEquals(a2, group.getSelectedAction(), "a2 selected");
        assertFalse(a1.isSelected(), "a1 not selected");
        assertTrue(a2.isSelected(), "a2 selected");
    }

    @Test
    void selectedActionProperty() {
        Action a1;
        Action a2;

        ToggleActionGroup group = new ToggleActionGroup().addAll(
                a1 = new Action().selected(true),
                a2 = new Action()
        );

        assertEquals(a1, group.selectedActionProperty().get(), "a1 selected");
        assertTrue(a1.isSelected(), "a1 selected");
        assertFalse(a2.isSelected(), "a2 not selected");

        a2.setSelected(true);

        assertEquals(a2, group.selectedActionProperty().get(), "a2 selected");
        assertFalse(a1.isSelected(), "a1 not selected");
        assertTrue(a2.isSelected(), "a2 selected");
    }

    @Test
    void iterator() {
        int counter = 0;
        ToggleActionGroup group = new ToggleActionGroup().addAll(
                new Action().selected(true),
                new Action(),
                new Action(),
                new Action()
        );

        for (var ignored: group)
            counter++;

        assertEquals(4, counter);
    }
}