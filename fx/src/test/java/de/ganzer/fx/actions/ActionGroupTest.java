package de.ganzer.fx.actions;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActionGroupTest {
    @Test
    void createDefault() {
        ActionGroup group = new ActionGroup();

        assertNull(group.getCommandText());
        assertNull(group.getTooltipText());
        assertNull(group.getAccelerator());
        assertNull(group.getMenuImage());
        assertNull(group.getSmallButtonImage());
        assertNull(group.getMediumButtonImage());
        assertNull(group.getLargeButtonImage());
        assertNull(group.getOnAction());
        assertNull(group.getTag());
        assertTrue(group.isVisible());
        assertFalse(group.isDisabled());
        assertFalse(group.isSelected());
        assertFalse(group.isSelectable());
        assertFalse(group.isExclusiveSelectable());
        assertEquals(0, group.getNotBindMenu());
        assertEquals(0, group.getNotBindButton());
    }

    @Test
    void createCommandText() {
        String expected = "test value";
        ActionGroup group = new ActionGroup(expected);

        assertEquals(expected, group.getCommandText());
        assertNull(group.getTooltipText());
        assertNull(group.getAccelerator());
        assertNull(group.getMenuImage());
        assertNull(group.getSmallButtonImage());
        assertNull(group.getMediumButtonImage());
        assertNull(group.getLargeButtonImage());
        assertNull(group.getOnAction());
        assertNull(group.getTag());
        assertTrue(group.isVisible());
        assertFalse(group.isDisabled());
        assertFalse(group.isSelected());
        assertFalse(group.isSelectable());
        assertFalse(group.isExclusiveSelectable());
        assertEquals(0, group.getNotBindMenu());
        assertEquals(0, group.getNotBindButton());
    }

    @Test
    void commandText() {
        String expected = "test value";
        ActionGroup group = new ActionGroup().commandText(expected);

        assertEquals(expected, group.getCommandText(), "accessor");
        assertEquals(expected, group.commandTextProperty().get(), "property");
    }

    @Test
    void disabled() {
        ActionGroup group = new ActionGroup().disabled(true);
        assertTrue(group.isDisabled(), "accessor");
        assertTrue(group.disabledProperty().get(), "property");

        group.disabled(false);
        assertFalse(group.isDisabled(), "accessor");
        assertFalse(group.disabledProperty().get(), "property");
    }

    @Test
    void visible() {
        ActionGroup group = new ActionGroup().visible(false);
        assertFalse(group.isVisible(), "accessor");
        assertFalse(group.visibleProperty().get(), "property");

        group.visible(true);
        assertTrue(group.isVisible(), "accessor");
        assertTrue(group.visibleProperty().get(), "property");
    }

    @Test
    void smallButtonImage() {
        Node expected = new ImageView();
        ActionGroup group = new ActionGroup().smallButtonImage(expected);

        assertEquals(expected, group.getSmallButtonImage(), "accessor");
        assertEquals(expected, group.smallButtonImageProperty().get(), "property");
    }

    @Test
    void mediumButtonImage() {
        Node expected = new ImageView();
        ActionGroup group = new ActionGroup().mediumButtonImage(expected);

        assertEquals(expected, group.getMediumButtonImage(), "accessor");
        assertEquals(expected, group.mediumButtonImageProperty().get(), "property");
    }

    @Test
    void largeButtonImage() {
        Node expected = new ImageView();
        ActionGroup group = new ActionGroup().largeButtonImage(expected);

        assertEquals(expected, group.getLargeButtonImage(), "accessor");
        assertEquals(expected, group.largeButtonImageProperty().get(), "property");
    }

    @Test
    void onAction() {
        EventHandler<ActionEvent> expected = e -> {};
        ActionGroup group = new ActionGroup().onAction(expected);

        assertEquals(expected, group.getOnAction(), "accessor");
        assertEquals(expected, group.onActionProperty().get(), "property");
    }

    @Test
    void notBindMenu() {
        int expected = BindNot.IMAGE | BindNot.SELECTED;
        ActionGroup group = new ActionGroup().notBindMenu(expected);

        assertEquals(expected, group.getNotBindMenu());
    }

    @Test
    void notBindButton() {
        int expected = BindNot.IMAGE | BindNot.SELECTED;
        ActionGroup group = new ActionGroup().notBindButton(expected);

        assertEquals(expected, group.getNotBindButton());
    }

    @Test
    void iterator() {
        int counter = 0;
        ActionGroup group = new ActionGroup().addAll(
               new Action(),
               new Action(),
               new SeparatorAction(),
               new ActionGroup()
        );

        for (var a: group)
            counter++;

        assertEquals(4, counter);
    }
}
