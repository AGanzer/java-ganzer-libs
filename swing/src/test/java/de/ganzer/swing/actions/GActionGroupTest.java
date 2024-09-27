package de.ganzer.swing.actions;

import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class GActionGroupTest {
    @Test
    void createDefault() {
        GActionGroup action = new GActionGroup();
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
        GActionGroup action = new GActionGroup(expected);
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
    void addAll() {
        GActionGroup group = new GActionGroup().addAll(
                new GAction(),
                new GAction(),
                new GActionGroup()
        );

        int count = 0;
        for (GActionItemBuilder ignored: group)
            count++;

        assertEquals(3, count);
    }

    @Test
    void name() {
        String expected = "test value";
        GActionGroup group = new GActionGroup().name(expected);
        assertEquals(expected, group.getValue(Action.NAME));
    }

    @Test
    void command() {
        String expected = "test value";
        GActionGroup group = new GActionGroup().command(expected);
        assertEquals(expected, group.getValue(Action.ACTION_COMMAND_KEY));
    }

    @Test
    void largeIcon() {
        Icon expected = new ImageIcon();
        GActionGroup group = new GActionGroup().largeIcon(expected);
        assertEquals(expected, group.getValue(Action.LARGE_ICON_KEY));
    }

    @Test
    void mnemonic() {
        int expected = KeyEvent.VK_M;
        GActionGroup group = new GActionGroup().mnemonic(expected);
        assertEquals(expected, group.getValue(Action.MNEMONIC_KEY));
    }

    @Test
    void displayedMnemonicIndex() {
        int expected = 1;
        GActionGroup group = new GActionGroup().displayedMnemonicIndex(expected);
        assertEquals(expected, group.getValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY));
    }

    @Test
    void shortDescription() {
        String expected = "test value";
        GActionGroup group = new GActionGroup().shortDescription(expected);
        assertEquals(expected, group.getValue(Action.SHORT_DESCRIPTION));
    }

    @Test
    void longDescription() {
        String expected = "test value";
        GActionGroup group = new GActionGroup().longDescription(expected);
        assertEquals(expected, group.getValue(Action.LONG_DESCRIPTION));
    }

    @Test
    void enabled() {
        boolean expected = false;

        // Use Action as test object to ensure
        // that the superclass' method is called:
        //
        Action group = new GActionGroup().enabled(expected);
        assertEquals(expected, group.isEnabled());
    }

    @Test
    void onAction() {
        AtomicBoolean called = new AtomicBoolean(false);
        GActionGroup group = new GActionGroup().onAction(e -> called.set(true));

        group.actionPerformed(new ActionEvent(this, 0, null));

        assertTrue(called.get());
    }

    @Test
    void createMenu() {
        String expectedText = "test value";
        GActionGroup group = new GActionGroup(expectedText).addAll(
                new GAction("0"),
                new GAction("1"),
                new GSeparatorAction(),
                new GActionGroup("3").addAll(
                        new GAction("4")
                )
        );

        JMenu menu = group.createMenu();

        assertEquals(expectedText, menu.getText(), "text");
        assertEquals(4, menu.getMenuComponentCount(), "count");

        assertInstanceOf(JMenuItem.class, menu.getMenuComponent(0), "class 0");
        assertEquals("0", ((JMenuItem)menu.getMenuComponent(0)).getText(), "text");

        assertInstanceOf(JMenuItem.class, menu.getMenuComponent(1), "class 1");
        assertEquals("1", ((JMenuItem)menu.getMenuComponent(1)).getText(), "text");

        assertInstanceOf(JSeparator.class, menu.getMenuComponent(2));

        assertInstanceOf(JMenu.class, menu.getMenuComponent(3));
        assertEquals("3", ((JMenu)menu.getMenuComponent(3)).getText(), "text");

        assertInstanceOf(JMenuItem.class, ((JMenu)menu.getMenuComponent(3)).getMenuComponent(0), "class 4");
        assertEquals("4", ((JMenuItem)((JMenu)menu.getMenuComponent(3)).getMenuComponent(0)).getText(), "text");
    }

    @Test
    void addMenus() {
        GActionGroup group = new GActionGroup().addAll(
                new GActionGroup("0").addAll(
                        new GAction("00")
                ),
                new GActionGroup("1").addAll(
                        new GAction("01")
                )
        );

        JMenuBar menuBar = new JMenuBar();
        group.addMenus(menuBar);

        assertEquals(2, menuBar.getMenuCount(), "count");

        assertInstanceOf(JMenu.class, menuBar.getMenu(0), "class 0");
        assertEquals("0", menuBar.getMenu(0).getText(), "text");

        assertInstanceOf(JMenuItem.class, menuBar.getMenu(0).getMenuComponent(0), "class 00");
        assertEquals("00", ((JMenuItem)menuBar.getMenu(0).getMenuComponent(0)).getText(), "text");

        assertInstanceOf(JMenu.class, menuBar.getMenu(1), "class 1");
        assertEquals("1", menuBar.getMenu(1).getText(), "text");

        assertInstanceOf(JMenuItem.class, menuBar.getMenu(1).getMenuComponent(0), "class 01");
        assertEquals("01", ((JMenuItem)menuBar.getMenu(1).getMenuComponent(0)).getText(), "text");
    }

    @Test
    void addMenuItemsIntoMenu() {
        GActionGroup group = new GActionGroup().addAll(
                new GAction("0"),
                new GAction("1"),
                new GSeparatorAction(),
                new GActionGroup("3").addAll(
                        new GAction("4")
                )
        );

        JMenu menu = new JMenu();
        group.addMenuItems(menu);

        assertEquals(4, menu.getMenuComponentCount(), "count");

        assertInstanceOf(JMenuItem.class, menu.getMenuComponent(0), "class 0");
        assertEquals("0", ((JMenuItem)menu.getMenuComponent(0)).getText(), "text");

        assertInstanceOf(JMenuItem.class, menu.getMenuComponent(1), "class 1");
        assertEquals("1", ((JMenuItem)menu.getMenuComponent(1)).getText(), "text");

        assertInstanceOf(JSeparator.class, menu.getMenuComponent(2));

        assertInstanceOf(JMenu.class, menu.getMenuComponent(3));
        assertEquals("3", ((JMenu)menu.getMenuComponent(3)).getText(), "text");

        assertInstanceOf(JMenuItem.class, ((JMenu)menu.getMenuComponent(3)).getMenuComponent(0), "class 4");
        assertEquals("4", ((JMenuItem)((JMenu)menu.getMenuComponent(3)).getMenuComponent(0)).getText(), "text");
    }

    @Test
    void addMenuItemsIntoPopupMenu() {
        GActionGroup group = new GActionGroup().addAll(
                new GAction("0"),
                new GAction("1"),
                new GSeparatorAction(),
                new GActionGroup("3").addAll(
                        new GAction("4")
                )
        );

        JPopupMenu menu = new JPopupMenu();
        group.addMenuItems(menu);

        assertEquals(4, menu.getComponents().length, "count");

        assertInstanceOf(JMenuItem.class, menu.getSubElements()[0], "class 0");
        assertEquals("0", ((JMenuItem)menu.getSubElements()[0]).getText(), "text");

        assertInstanceOf(JMenuItem.class, menu.getSubElements()[1], "class 1");
        assertEquals("1", ((JMenuItem)menu.getSubElements()[1]).getText(), "text");

        assertInstanceOf(JSeparator.class, menu.getComponents()[2]);

        assertInstanceOf(JMenu.class, menu.getSubElements()[2]);
        assertEquals("3", ((JMenu)menu.getSubElements()[2]).getText(), "text");

        assertInstanceOf(JMenuItem.class, ((JMenu)menu.getSubElements()[2]).getMenuComponent(0), "class 4");
        assertEquals("4", ((JMenuItem)((JMenu)menu.getSubElements()[2]).getMenuComponent(0)).getText(), "text");
    }

    @Test
    void createButton() {
        GActionGroup group = new GActionGroup()
                .enabled(false)
                .addAll(
                new GAction("0"),
                new GAction("1"),
                new GSeparatorAction(),
                new GActionGroup("3").addAll(
                        new GAction("4")
                )
        );

        AbstractButton button = group.createButton();

        assertInstanceOf(JButton.class, button, "JButton");
        assertFalse(button.isEnabled(), "enabled");
    }

    @Test
    void addButtons() {
        GActionGroup group = new GActionGroup().addAll(
                new GAction("0"),
                new GAction("1"),
                new GSeparatorAction(),
                new GActionGroup("3").addAll(
                        new GAction("4")
                )
        );

        JToolBar toolBar = new JToolBar();
        group.addButtons(toolBar);

        assertEquals(4, toolBar.getComponents().length, "num items");

        assertInstanceOf(JButton.class, toolBar.getComponent(0), "class 0");
        assertEquals("0", ((JButton)toolBar.getComponent(0)).getText(), "text");

        assertInstanceOf(JButton.class, toolBar.getComponent(1), "class 1");
        assertEquals("1", ((JButton)toolBar.getComponent(1)).getText(), "text");

        assertInstanceOf(JToolBar.Separator.class, toolBar.getComponent(2));

        assertInstanceOf(JButton.class, toolBar.getComponent(3), "class 3");
        assertEquals("3", ((JButton)toolBar.getComponent(3)).getText(), "text");
    }
}