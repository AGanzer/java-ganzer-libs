package de.ganzer.fx.actions;

import de.ganzer.fx.input.LocalizedKeyCombination;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class ActionTest {
    @Test
    void createDefault() {
        Action action = new Action();

        assertNull(action.getCommandText());
        assertNull(action.getTooltipText());
        assertNull(action.getAccelerator());
        assertNull(action.getMenuImage());
        assertNull(action.getSmallButtonImage());
        assertNull(action.getMediumButtonImage());
        assertNull(action.getLargeButtonImage());
        assertNull(action.getOnAction());
        assertNull(action.getTag());
        assertTrue(action.isVisible());
        assertFalse(action.isDisabled());
        assertFalse(action.isSelected());
        assertFalse(action.isSelectable());
        assertFalse(action.isExclusiveSelectable());
        assertEquals(0, action.getNotBindMenu());
        assertEquals(0, action.getNotBindButton());
    }

    @Test
    void createCommandText() {
        String expected = "test value";
        Action action = new Action(expected);

        assertEquals(expected, action.getCommandText());
        assertNull(action.getTooltipText());
        assertNull(action.getAccelerator());
        assertNull(action.getMenuImage());
        assertNull(action.getSmallButtonImage());
        assertNull(action.getMediumButtonImage());
        assertNull(action.getLargeButtonImage());
        assertNull(action.getOnAction());
        assertNull(action.getTag());
        assertTrue(action.isVisible());
        assertFalse(action.isDisabled());
        assertFalse(action.isSelected());
        assertFalse(action.isSelectable());
        assertFalse(action.isExclusiveSelectable());
        assertEquals(0, action.getNotBindMenu());
        assertEquals(0, action.getNotBindButton());
    }

    @Test
    void setShowAcceleratorsInTooltips() {
        Action.setShowAcceleratorsInTooltips(true);
        assertTrue(Action.isShowAcceleratorsInTooltips());

        Action.setShowAcceleratorsInTooltips(false);
        assertFalse(Action.isShowAcceleratorsInTooltips());
    }

    @Test
    void setShowAcceleratorsInButtonTexts() {
        Action.setShowAcceleratorsInButtonTexts(true);
        assertTrue(Action.isShowAcceleratorsInButtonTexts());

        Action.setShowAcceleratorsInButtonTexts(false);
        assertFalse(Action.isShowAcceleratorsInButtonTexts());
    }

    @Test
    void commandText() {
        String expected = "test value";
        Action action = new Action().commandText(expected);

        assertEquals(expected, action.getCommandText(), "accessor");
        assertEquals(expected, action.commandTextProperty().get(), "property");
    }

    @Test
    void accelerator() {
        KeyCombination expected = LocalizedKeyCombination.valueOf("Ctrl+S");
        Action action = new Action().accelerator(expected);

        assertEquals(expected, action.getAccelerator(), "accessor");
        assertEquals(expected, action.acceleratorProperty().get(), "property");
    }

    @Test
    void menuImage() {
        Node expected = new ImageView();
        Action action = new Action().menuImage(expected);

        assertEquals(expected, action.getMenuImage(), "accessor");
        assertEquals(expected, action.menuImageProperty().get(), "property");
    }

    @Test
    void smallButtonImage() {
        Node expected = new ImageView();
        Action action = new Action().smallButtonImage(expected);

        assertEquals(expected, action.getSmallButtonImage(), "accessor");
        assertEquals(expected, action.smallButtonImageProperty().get(), "property");
    }

    @Test
    void mediumButtonImage() {
        Node expected = new ImageView();
        Action action = new Action().mediumButtonImage(expected);

        assertEquals(expected, action.getMediumButtonImage(), "accessor");
        assertEquals(expected, action.mediumButtonImageProperty().get(), "property");
    }

    @Test
    void largeButtonImage() {
        Node expected = new ImageView();
        Action action = new Action().largeButtonImage(expected);

        assertEquals(expected, action.getLargeButtonImage(), "accessor");
        assertEquals(expected, action.largeButtonImageProperty().get(), "property");
    }

    @Test
    void disabled() {
        Action action = new Action().disabled(true);
        assertTrue(action.isDisabled(), "accessor");
        assertTrue(action.disabledProperty().get(), "property");

        action.disabled(false);
        assertFalse(action.isDisabled(), "accessor");
        assertFalse(action.disabledProperty().get(), "property");
    }

    @Test
    void visible() {
        Action action = new Action().visible(false);
        assertFalse(action.isVisible(), "accessor");
        assertFalse(action.visibleProperty().get(), "property");

        action.visible(true);
        assertTrue(action.isVisible(), "accessor");
        assertTrue(action.visibleProperty().get(), "property");
    }

    @Test
    void selected() {
        Action action = new Action().selected(true);
        assertTrue(action.isSelected(), "accessor");
        assertTrue(action.selectedProperty().get(), "property");

        action.selected(false);
        assertFalse(action.isSelected(), "accessor");
        assertFalse(action.selectedProperty().get(), "property");
    }

    @Test
    void selectable() {
        Action action = new Action().selectable(true);
        assertTrue(action.isSelectable());

        action.selectable(false);
        assertFalse(action.isSelectable());
    }

    @Test
    void exclusiveSelectable() {
        Action action = new Action().exclusiveSelectable(true);
        assertTrue(action.isExclusiveSelectable());

        action.exclusiveSelectable(false);
        assertFalse(action.isExclusiveSelectable());
    }

    @Test
    void onAction() {
        EventHandler<ActionEvent> expected = e -> {};
        Action action = new Action().onAction(expected);

        assertEquals(expected, action.getOnAction(), "accessor");
        assertEquals(expected, action.onActionProperty().get(), "property");
    }

    @Test
    void notBindMenu() {
        int expected = BindNot.IMAGE | BindNot.SELECTED;
        Action action = new Action().notBindMenu(expected);

        assertEquals(expected, action.getNotBindMenu());
    }

    @Test
    void notBindButton() {
        int expected = BindNot.IMAGE | BindNot.SELECTED;
        Action action = new Action().notBindButton(expected);

        assertEquals(expected, action.getNotBindButton());
    }

    @Test
    void cloneImage() {
        Function<Node, Node> expected = n -> new ImageView();
        Action action = new Action().cloneImage(expected);

        assertEquals(expected, action.getCloneImage());
    }

    @Test
    void tag() {
        Object expected = new Object();
        Action action = new Action().tag(expected);

        assertEquals(expected, action.getTag());
    }

    @Test
    void setCommandText() {
        String expected = "test value";
        Action action = new Action();
        action.setCommandText(expected);

        assertEquals(expected, action.getCommandText());
    }

    @Test
    void setMenuImage() {
        Node expected = new ImageView();
        Action action = new Action();
        action.setMenuImage(expected);

        assertEquals(expected, action.getMenuImage());
    }

    @Test
    void setSmallButtonImage() {
        Node expected = new ImageView();
        Action action = new Action();
        action.setSmallButtonImage(expected);

        assertEquals(expected, action.getSmallButtonImage());
    }

    @Test
    void setMediumButtonImage() {
        Node expected = new ImageView();
        Action action = new Action();
        action.setMediumButtonImage(expected);

        assertEquals(expected, action.getMediumButtonImage());
    }

    @Test
    void setLargeButtonImage() {
        Node expected = new ImageView();
        Action action = new Action();
        action.setLargeButtonImage(expected);

        assertEquals(expected, action.getLargeButtonImage());
    }

    @Test
    void setSelected() {
        Action action = new Action();

        action.setSelected(true);
        assertTrue(action.isSelected());

        action.setSelected(false);
        assertFalse(action.isSelected());
    }

    @Test
    void setDisabled() {
        Action action = new Action();

        action.setDisabled(true);
        assertTrue(action.isDisabled());

        action.setDisabled(false);
        assertFalse(action.isDisabled());
    }

    @Test
    void setVisible() {
        Action action = new Action();

        action.setVisible(false);
        assertFalse(action.isVisible());

        action.setVisible(true);
        assertTrue(action.isVisible());
    }

    @Test
    void setAccelerator() {
        KeyCombination expected = LocalizedKeyCombination.valueOf("Ctrl+S");
        Action action = new Action();
        action.setAccelerator(expected);

        assertEquals(expected, action.getAccelerator());
    }

    @Test
    void setOnAction() {
        EventHandler<ActionEvent> expected = e -> {};
        Action action = new Action();
        action.setOnAction(expected);

        assertEquals(expected, action.getOnAction());
    }

    @Test
    void setSelectable() {
        Action action = new Action();

        action.setSelectable(true);
        assertTrue(action.isSelectable());

        action.setSelectable(false);
        assertFalse(action.isSelectable());
    }

    @Test
    void setExclusiveSelectable() {
        Action action = new Action();

        action.setExclusiveSelectable(true);
        assertTrue(action.isExclusiveSelectable());

        action.setExclusiveSelectable(false);
        assertFalse(action.isExclusiveSelectable());
    }

    @Test
    void setNotBindMenu() {
        int expected = BindNot.IMAGE | BindNot.SELECTED;
        Action action = new Action();
        action.setNotBindMenu(expected);

        assertEquals(expected, action.getNotBindMenu());
    }

    @Test
    void setNotBindButton() {
        int expected = BindNot.IMAGE | BindNot.SELECTED;
        Action action = new Action();
        action.setNotBindButton(expected);

        assertEquals(expected, action.getNotBindButton());
    }

    @Test
    void setCloneImage() {
        Function<Node, Node> expected = n -> new ImageView();
        Action action = new Action();
        action.setCloneImage(expected);

        assertEquals(expected, action.getCloneImage());
    }

    @Test
    void setTag() {
        Object expected = new Object();
        Action action = new Action();
        action.setTag(expected);

        assertEquals(expected, action.getTag());
    }

    @Test
    void commandTextProperty() {
    }

    @Test
    void acceleratorProperty() {
    }

    @Test
    void menuImageProperty() {
    }

    @Test
    void smallButtonImageProperty() {
    }

    @Test
    void mediumButtonImageProperty() {
    }

    @Test
    void largeButtonImageProperty() {
    }

    @Test
    void disabledProperty() {
    }

    @Test
    void selectedProperty() {
    }

    @Test
    void visibleProperty() {
    }

    @Test
    void onActionProperty() {
    }

    @Test
    void createMenuItems() {
    }

    @Test
    void createButtons() {
    }
}