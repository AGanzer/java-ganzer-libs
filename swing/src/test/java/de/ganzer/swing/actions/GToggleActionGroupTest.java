package de.ganzer.swing.actions;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class GToggleActionGroupTest {
    @Test
    void createDefault() {
        GToggleActionGroup group = new GToggleActionGroup();

        assertTrue(group.isForceSelection(), "isForceSelection");
        assertEquals(0, group.getSelectedActionChangedListeners().length);
    }
    @Test
    void createForceSelection() {
        GToggleActionGroup group = new GToggleActionGroup(false);

        assertFalse(group.isForceSelection(), "isForceSelection");
        assertEquals(0, group.getSelectedActionChangedListeners().length);
    }

    @Test
    void forceSelection() {
        GToggleActionGroup group = new GToggleActionGroup().forceSelection(false);
        assertFalse(group.isForceSelection(), "isForceSelection");
    }

    @Test
    void onSelectedActionChanged() {
        GAction a1;
        GAction a2;
        AtomicReference<GAction> selected = new AtomicReference<>();

        new GToggleActionGroup()
                .onSelectedActionChanged(e -> selected.set(e.getSelectedAction()))
                .addAll(
                a1 = new GAction().exclusivelySelectable(true).selected(true),
                a2 = new GAction().exclusivelySelectable(true)
        );

        a2.selected(true);

        assertEquals(a2, selected.get(), "selected");
        assertFalse(a1.isSelected(), "a1");
        assertTrue(a2.isSelected(), "a2");
    }

    @Test
    void onSelectedActionChangedNoSelection() {
        GAction a1;
        GAction a2;

        new GToggleActionGroup().addAll(
                a1 = new GAction().exclusivelySelectable(true).selected(true),
                a2 = new GAction().exclusivelySelectable(true)
        );

        a1.selected(false);

        assertFalse(a2.isSelected(), "a1");
        assertTrue(a1.isSelected(), "a2");
    }

    @Test
    void onSelectedActionChangedNoSelectionNoForce() {
        GAction a1;
        GAction a2;

        new GToggleActionGroup(false).addAll(
                a1 = new GAction().exclusivelySelectable(true).selected(true),
                a2 = new GAction().exclusivelySelectable(true)
        );

        a1.selected(false);

        assertFalse(a2.isSelected(), "a1");
        assertFalse(a1.isSelected(), "a2");
    }

    @Test
    void disabled() {
        GToggleActionGroup group = new GToggleActionGroup().addAll(
                new GAction().exclusivelySelectable(true).selected(true),
                new GAction().exclusivelySelectable(true)
        );

        assertFalse(group.isDisabled(), "isDisabled false");
        group.disabled(true);
        assertTrue(group.isDisabled(), "isDisabled true");
    }

    @Test
    void addSelectedActionChangedListener() {
        AtomicBoolean called = new AtomicBoolean(false);
        GAction a;

        GToggleActionGroup group = new GToggleActionGroup().addAll(
                new GAction().exclusivelySelectable(true).selected(true),
                a = new GAction().exclusivelySelectable(true)
        );

        group.addSelectedActionChangedListener(e -> called.set(true));
        a.selected(true);

        assertTrue(called.get());
    }

    @Test
    void getSelectedActionChangedListeners() {
        GSelectedActionChangedListener listener = e -> {};
        GToggleActionGroup group = new GToggleActionGroup().onSelectedActionChanged(listener);

        assertEquals(1, group.getSelectedActionChangedListeners().length, "actionListeners.length");
        assertEquals(listener, group.getSelectedActionChangedListeners()[0], "actionListeners[0]");
    }

    @Test
    void removeSelectedActionChangedListener() {
        AtomicBoolean called = new AtomicBoolean(false);
        GToggleActionGroup action = new GToggleActionGroup();
        GSelectedActionChangedListener listener = e -> called.set(true);
        GAction a;

        GToggleActionGroup group = new GToggleActionGroup().addAll(
                new GAction().exclusivelySelectable(true).selected(true),
                a = new GAction().exclusivelySelectable(true)
        );

        group.addSelectedActionChangedListener(listener);
        group.removeSelectedActionChangedListener(listener);
        a.selected(true);

        assertFalse(called.get());
    }

    @Test
    void addMenuItemsIntoMenu() {
        GToggleActionGroup group = new GToggleActionGroup().addAll(
                new GAction("0").exclusivelySelectable(true).selected(true),
                new GAction("1").exclusivelySelectable(true)
        );

        JMenu menu = new JMenu();
        group.addMenuItems(menu);

        assertEquals(2, menu.getMenuComponentCount(), "count");

        assertInstanceOf(JRadioButtonMenuItem.class, menu.getMenuComponent(0), "class 0");
        assertEquals("0", ((JMenuItem)menu.getMenuComponent(0)).getText(), "text");

        assertInstanceOf(JRadioButtonMenuItem.class, menu.getMenuComponent(1), "class 1");
        assertEquals("1", ((JMenuItem)menu.getMenuComponent(1)).getText(), "text");
    }

    @Test
    void addMenuItemsIntoPopupMenu() {
        GToggleActionGroup group = new GToggleActionGroup().addAll(
                new GAction("0").exclusivelySelectable(true).selected(true),
                new GAction("1").exclusivelySelectable(true)
        );

        JPopupMenu menu = new JPopupMenu();
        group.addMenuItems(menu);

        assertEquals(2, menu.getComponents().length, "count");

        assertInstanceOf(JRadioButtonMenuItem.class, menu.getSubElements()[0], "class 0");
        assertEquals("0", ((JMenuItem)menu.getSubElements()[0]).getText(), "text");

        assertInstanceOf(JRadioButtonMenuItem.class, menu.getSubElements()[1], "class 1");
        assertEquals("1", ((JMenuItem)menu.getSubElements()[1]).getText(), "text");
    }

    @Test
    void addButtons() {
        GToggleActionGroup group = new GToggleActionGroup().addAll(
                new GAction("0").exclusivelySelectable(true).selected(true),
                new GAction("1").exclusivelySelectable(true)
        );

        JToolBar toolBar = new JToolBar();
        group.addButtons(toolBar);

        assertEquals(2, toolBar.getComponents().length, "num items");

        assertInstanceOf(JToggleButton.class, toolBar.getComponent(0), "class 0");
        assertEquals("0", ((JToggleButton)toolBar.getComponent(0)).getText(), "text");

        assertInstanceOf(JToggleButton.class, toolBar.getComponent(1), "class 1");
        assertEquals("1", ((JToggleButton)toolBar.getComponent(1)).getText(), "text");
    }

    @Test
    void iterator() {
        int counter = 0;
        GToggleActionGroup group = new GToggleActionGroup().addAll(
                new GAction().exclusivelySelectable(true).selected(true),
                new GAction().exclusivelySelectable(true)
        );

        for (GAction action : group)
            counter++;

        assertEquals(2, counter);
    }
}
