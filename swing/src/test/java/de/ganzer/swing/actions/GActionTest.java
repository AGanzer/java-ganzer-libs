package de.ganzer.swing.actions;

import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class GActionTest {
    @Test
    void createDefault() {
        GAction action = new GAction();
        assertNull(action.getName(), "name");
        assertNull(action.getCommand(), "command");
        assertNull(action.getSmallIcon(), "smallIcon");
        assertNull(action.getLargeIcon(), "largeIcon");
        assertNull(action.getShortDescription(), "shortDescription");
        assertNull(action.getLongDescription(), "longDescription");
        assertNull(action.getAccelerator(), "accelerator");
        assertNull(action.getMnemonic(), "mnemonic");
        assertNull(action.getDisplayedMnemonicIndex(), "displayedMnemonicIndex");
        assertTrue(action.isEnabled(), "enabled");
        assertFalse(action.isSelected(), "selected");
        assertFalse(action.isSelectable(), "selectable");
        assertFalse(action.isExclusivelySelectable(), "exclusivelySelectable");
        assertEquals(0, action.getActionListeners().length, "actionListeners");
    }

    @Test
    void createName() {
        String expected = "test value";
        GAction action = new GAction(expected);
        assertEquals(expected, action.getName(), "name");
        assertNull(action.getCommand(), "command");
        assertNull(action.getSmallIcon(), "smallIcon");
        assertNull(action.getLargeIcon(), "largeIcon");
        assertNull(action.getShortDescription(), "shortDescription");
        assertNull(action.getLongDescription(), "longDescription");
        assertNull(action.getAccelerator(), "accelerator");
        assertNull(action.getMnemonic(), "mnemonic");
        assertNull(action.getDisplayedMnemonicIndex(), "displayedMnemonicIndex");
        assertTrue(action.isEnabled(), "enabled");
        assertFalse(action.isSelected(), "selected");
        assertFalse(action.isSelectable(), "selectable");
        assertFalse(action.isExclusivelySelectable(), "exclusivelySelectable");
        assertEquals(0, action.getActionListeners().length, "actionListeners");
    }

    @Test
    void createNameAndIcon() {
        String expectedName = "test value";
        Icon expectedIcon = new ImageIcon();

        GAction action = new GAction(expectedName, expectedIcon);
        assertEquals(expectedName, action.getName(), "name");
        assertNull(action.getCommand(), "command");
        assertEquals(expectedIcon, action.getSmallIcon(), "smallIcon");
        assertNull(action.getLargeIcon(), "largeIcon");
        assertNull(action.getShortDescription(), "shortDescription");
        assertNull(action.getLongDescription(), "longDescription");
        assertNull(action.getAccelerator(), "accelerator");
        assertNull(action.getMnemonic(), "mnemonic");
        assertNull(action.getDisplayedMnemonicIndex(), "displayedMnemonicIndex");
        assertTrue(action.isEnabled(), "enabled");
        assertFalse(action.isSelected(), "selected");
        assertFalse(action.isSelectable(), "selectable");
        assertFalse(action.isExclusivelySelectable(), "exclusivelySelectable");
        assertEquals(0, action.getActionListeners().length, "actionListeners");
    }

    @Test
    void name() {
        String expected = "test value";
        GAction action = new GAction().name(expected);
        assertEquals(expected, action.getValue(Action.NAME));
    }

    @Test
    void getName() {
        String expected = "test value";
        GAction action = new GAction();
        action.putValue(Action.NAME, expected);
        assertEquals(expected, action.getName());
    }

    @Test
    void command() {
        String expected = "test value";
        GAction action = new GAction().command(expected);
        assertEquals(expected, action.getValue(Action.ACTION_COMMAND_KEY));
    }

    @Test
    void getCommand() {
        String expected = "test value";
        GAction action = new GAction();
        action.putValue(Action.ACTION_COMMAND_KEY, expected);
        assertEquals(expected, action.getCommand());
    }

    @Test
    void smallIcon() {
        Icon expected = new ImageIcon();
        GAction action = new GAction().smallIcon(expected);
        assertEquals(expected, action.getValue(Action.SMALL_ICON));
    }

    @Test
    void getSmallIcon() {
        Icon expected = new ImageIcon();
        GAction action = new GAction();
        action.putValue(Action.SMALL_ICON, expected);
        assertEquals(expected, action.getSmallIcon());
    }

    @Test
    void largeIcon() {
        Icon expected = new ImageIcon();
        GAction action = new GAction().largeIcon(expected);
        assertEquals(expected, action.getValue(Action.LARGE_ICON_KEY));
    }

    @Test
    void getLargeIcon() {
        Icon expected = new ImageIcon();
        GAction action = new GAction();
        action.putValue(Action.LARGE_ICON_KEY, expected);
        assertEquals(expected, action.getLargeIcon());
    }

    @Test
    void accelerator() {
        KeyStroke expected = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK);
        GAction action = new GAction().accelerator(expected);
        assertEquals(expected, action.getValue(Action.ACCELERATOR_KEY));
    }

    @Test
    void getAccelerator() {
        KeyStroke expected = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK);
        GAction action = new GAction();
        action.putValue(Action.ACCELERATOR_KEY, expected);
        assertEquals(expected, action.getAccelerator());
    }

    @Test
    void mnemonic() {
        int expected = KeyEvent.VK_M;
        GAction action = new GAction().mnemonic(expected);
        assertEquals(expected, action.getValue(Action.MNEMONIC_KEY));
    }

    @Test
    void getMnemonic() {
        int expected = KeyEvent.VK_M;
        GAction action = new GAction();
        action.putValue(Action.MNEMONIC_KEY, expected);
        assertEquals(expected, action.getMnemonic());
    }

    @Test
    void displayedMnemonicIndex() {
        int expected = 1;
        GAction action = new GAction().displayedMnemonicIndex(expected);
        assertEquals(expected, action.getValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY));
    }

    @Test
    void getDisplayedMnemonicIndex() {
        int expected = 1;
        GAction action = new GAction();
        action.putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, expected);
        assertEquals(expected, action.getDisplayedMnemonicIndex());
    }

    @Test
    void shortDescription() {
        String expected = "test value";
        GAction action = new GAction().shortDescription(expected);
        assertEquals(expected, action.getValue(Action.SHORT_DESCRIPTION));
    }

    @Test
    void getShortDescription() {
        String expected = "test value";
        GAction action = new GAction();
        action.putValue(Action.SHORT_DESCRIPTION, expected);
        assertEquals(expected, action.getShortDescription());
    }

    @Test
    void longDescription() {
        String expected = "test value";
        GAction action = new GAction().longDescription(expected);
        assertEquals(expected, action.getValue(Action.LONG_DESCRIPTION));
    }

    @Test
    void getLongDescription() {
        String expected = "test value";
        GAction action = new GAction();
        action.putValue(Action.LONG_DESCRIPTION, expected);
        assertEquals(expected, action.getLongDescription());
    }

    @Test
    void selectableTrue() {
        boolean expected = true;
        GAction action = new GAction().selectable(expected);
        assertEquals(expected, action.isSelectable());
    }

    @Test
    void selectableFalse() {
        boolean expected = false;
        GAction action = new GAction().selectable(expected);
        assertEquals(expected, action.isSelectable());
    }

    @Test
    void exclusivelySelectableTrue() {
        boolean expected = true;
        GAction action = new GAction().exclusivelySelectable(expected);
        assertEquals(expected, action.isExclusivelySelectable());
    }

    @Test
    void exclusivelySelectableFalse() {
        boolean expected = false;
        GAction action = new GAction().exclusivelySelectable(expected);
        assertEquals(expected, action.isExclusivelySelectable());
    }

    @Test
    void selected() {
        boolean expected = true;
        GAction action = new GAction().selected(expected);
        assertEquals(expected, action.getValue(Action.SELECTED_KEY));
    }

    @Test
    void isSelected() {
        boolean expected = true;
        GAction action = new GAction();
        action.putValue(Action.SELECTED_KEY, expected);
        assertEquals(expected, action.isSelected());
    }

    @Test
    void enabled() {
        boolean expected = false;

        // Use Action as test object to ensure
        // that the superclass' method is called:
        //
        Action action = new GAction().enabled(expected);
        assertEquals(expected, action.isEnabled());
    }

    @Test
    void onAction() {
        AtomicBoolean called = new AtomicBoolean(false);
        GAction action = new GAction().onAction(e -> called.set(true));

        action.actionPerformed(new ActionEvent(this, 0, null));

        assertTrue(called.get());
    }

    @Test
    void addActionListener() {
        AtomicBoolean called = new AtomicBoolean(false);
        GAction action = new GAction();

        action.addActionListener(e -> called.set(true));
        action.actionPerformed(new ActionEvent(this, 0, null));

        assertTrue(called.get());
    }

    @Test
    void getActionListeners() {
        ActionListener listener = e -> {};
        GAction action = new GAction().onAction(listener);

        assertEquals(1, action.getActionListeners().length, "actionListeners.length");
        assertEquals(listener, action.getActionListeners()[0], "actionListeners[0]");
    }

    @Test
    void removeActionListener() {
        AtomicBoolean called = new AtomicBoolean(false);
        ActionListener listener = e -> called.set(true);
        GAction action = new GAction();

        action.addActionListener(listener);
        action.removeActionListener(listener);
        action.actionPerformed(new ActionEvent(this, 0, null));

        assertFalse(called.get());
    }

    @Test
    void createMenuItem() {
        GAction action = new GAction()
                .enabled(false)
                .selected(true)
                .selectable(false)
                .exclusivelySelectable(false);

        JMenuItem menuItem = action.createMenuItem();

        assertInstanceOf(JMenuItem.class, menuItem, "JMenuItem");
        assertFalse(menuItem.isEnabled(), "enabled");
        assertFalse(menuItem.isSelected(), "selected");
    }

    @Test
    void createCheckBoxMenuItem() {
        GAction action = new GAction()
                .enabled(false)
                .selected(true)
                .selectable(true)
                .exclusivelySelectable(false);

        JMenuItem menuItem = action.createMenuItem();

        assertInstanceOf(JCheckBoxMenuItem.class, menuItem, "JCheckBoxMenuItem");
        assertFalse(menuItem.isEnabled(), "enabled");
        assertTrue(menuItem.isSelected(), "selected");
    }

    @Test
    void createRadioButtonMenuItem() {
        GAction action = new GAction()
                .enabled(false)
                .selected(true)
                .selectable(false)
                .exclusivelySelectable(true);

        JMenuItem menuItem = action.createMenuItem();

        assertInstanceOf(JRadioButtonMenuItem.class, menuItem, "JRadioButtonMenuItem");
        assertFalse(menuItem.isEnabled(), "enabled");
        assertTrue(menuItem.isSelected(), "selected");
    }

    @Test
    void addMenuItemsToMenu() {
        String expectedText = "test value";
        GAction action = new GAction(expectedText);
        JMenu menu = new JMenu();
        action.addMenuItems(menu);

        assertEquals(1, menu.getMenuComponents().length, "num items");
        assertEquals(expectedText, ((JMenuItem)menu.getMenuComponents()[0]).getText(), "item text");
    }

    @Test
    void addMenuItemsToPopupMenu() {
        String expectedText = "test value";
        GAction action = new GAction(expectedText);
        JPopupMenu menu = new JPopupMenu();
        action.addMenuItems(menu);

        assertEquals(1, menu.getSubElements().length, "num items");
        assertEquals(expectedText, ((JMenuItem)menu.getSubElements()[0]).getText(), "item text");
    }

    @Test
    void createButton() {
        GAction action = new GAction()
                .enabled(false)
                .selected(true)
                .selectable(false)
                .exclusivelySelectable(false);

        AbstractButton button = action.createButton();

        assertInstanceOf(JButton.class, button, "JButton");
        assertFalse(button.isEnabled(), "enabled");
        assertFalse(button.isSelected(), "selected");
    }

    @Test
    void createToggleButton() {
        GAction action = new GAction()
                .enabled(false)
                .selected(true)
                .selectable(true)
                .exclusivelySelectable(false);

        AbstractButton button = action.createButton();

        assertInstanceOf(JToggleButton.class, button, "JToggleButton");
        assertFalse(button.isEnabled(), "enabled");
        assertTrue(button.isSelected(), "selected");
    }

    @Test
    void createExclusiveToggleButton() {
        GAction action = new GAction()
                .enabled(false)
                .selected(true)
                .selectable(false)
                .exclusivelySelectable(true);

        AbstractButton button = action.createButton();

        assertInstanceOf(JToggleButton.class, button, "JToggleButton");
        assertFalse(button.isEnabled(), "enabled");
        assertTrue(button.isSelected(), "selected");
    }

    @Test
    void addButtons() {
        String expectedText = "test value";
        GAction action = new GAction(expectedText);
        JToolBar toolBar = new JToolBar();
        action.addButtons(toolBar);

        assertEquals(1, toolBar.getComponents().length, "num items");
        assertEquals(expectedText, ((JButton)toolBar.getComponent(0)).getText(), "item text");
    }

    @Test
    void shouldHideText() {
        GAction emptyAction = new GAction();
        GAction smallIconAction = new GAction().smallIcon(new ImageIcon());
        GAction largeIconAction = new GAction().largeIcon(new ImageIcon());

        assertFalse(emptyAction.shouldHideText(CreateOptions.DEFAULT), "empty DEFAULT");
        assertTrue(smallIconAction.shouldHideText(CreateOptions.DEFAULT), "small DEFAULT");
        assertTrue(largeIconAction.shouldHideText(CreateOptions.DEFAULT), "large DEFAULT");
        assertFalse(smallIconAction.shouldHideText(CreateOptions.SHOW_TEXT), "small SHOW_TEXT");
        assertFalse(largeIconAction.shouldHideText(CreateOptions.SHOW_TEXT), "large SHOW_TEXT");
        assertFalse(smallIconAction.shouldHideText(CreateOptions.HIDE_IMAGE), "small HIDE_IMAGE");
        assertFalse(largeIconAction.shouldHideText(CreateOptions.HIDE_IMAGE), "large HIDE_IMAGE");
    }

    @Test
    void getVerticalTextPosition() {
        GAction action = new GAction();

        assertEquals(SwingConstants.BOTTOM, action.getVerticalTextPosition(CreateOptions.DEFAULT), "DEFAULT");
        assertEquals(SwingConstants.CENTER, action.getVerticalTextPosition(CreateOptions.IMAGE_LEADING), "IMAGE_LEADING");
    }

    @Test
    void getHorizontalTextPosition() {
        GAction action = new GAction();

        assertEquals(SwingConstants.CENTER, action.getHorizontalTextPosition(CreateOptions.DEFAULT), "DEFAULT");
        assertEquals(SwingConstants.TRAILING, action.getHorizontalTextPosition(CreateOptions.IMAGE_LEADING), "IMAGE_LEADING");
    }
}
