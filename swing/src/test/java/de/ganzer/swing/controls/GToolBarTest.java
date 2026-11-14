package de.ganzer.swing.controls;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

class GToolBarTest {
    @ParameterizedTest
    @ValueSource(ints = {JToolBar.HORIZONTAL, JToolBar.VERTICAL})
    void overflowsWholeItemsAndRestoresThemAfterResizing(int orientation) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            GToolBar toolbar = toolbar(orientation);
            JButton first = item();
            JButton second = item();
            JButton third = item();
            toolbar.add(first);
            toolbar.add(second);
            toolbar.add(third);
            Dimension preferred = toolbar.getPreferredSize();
            JButton overflow = (JButton) toolbar.getComponent(3);
            int available = 80 + length(toolbar, overflow.getPreferredSize());
            size(toolbar, available);
            toolbar.doLayout();
            assertTrue(overflow.isVisible());
            assertNotNull(overflow.getIcon());
            assertEquals(40, length(toolbar, first.getSize()));
            assertEquals(40, length(toolbar, second.getSize()));
            assertEquals(new Rectangle(), third.getBounds());
            assertEquals(preferred, toolbar.getPreferredSize());
            assertEquals(available, orientation == JToolBar.HORIZONTAL
                    ? overflow.getX() + overflow.getWidth() : overflow.getY() + overflow.getHeight());

            size(toolbar, 120);
            toolbar.doLayout();
            assertFalse(overflow.isVisible());
            assertEquals(40, length(toolbar, third.getSize()));
            assertSame(third, toolbar.getComponent(2));
        });
    }

    @Test
    void ignoresHiddenItemsAndHandlesEmptyAndVerySmallToolbars() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            GToolBar toolbar = toolbar(JToolBar.HORIZONTAL);
            size(toolbar, 0);
            toolbar.doLayout();
            assertFalse(toolbar.getComponent(0).isVisible());
            JButton hidden = item();
            hidden.setVisible(false);
            toolbar.add(hidden);
            JButton visible = item();
            toolbar.add(visible);
            size(toolbar, 40);
            toolbar.doLayout();
            assertFalse(toolbar.getComponent(2).isVisible());
            assertEquals(40, visible.getWidth());
            size(toolbar, 1);
            toolbar.doLayout();
            assertTrue(toolbar.getComponent(2).isVisible());
            assertEquals(0, visible.getWidth());
            assertFalse(hidden.isVisible());
            toolbar.removeAll();
            toolbar.doLayout();
            assertEquals(1, toolbar.getComponentCount());
            assertFalse(toolbar.getComponent(0).isVisible());
        });
    }

    @Test
    void recalculatesAfterOrientationAndVisibilityChanges() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            GToolBar toolbar = toolbar(JToolBar.HORIZONTAL);
            JButton first = item();
            JButton second = item();
            toolbar.add(first);
            toolbar.add(second);
            size(toolbar, 60);
            toolbar.doLayout();
            assertEquals(0, second.getWidth());
            first.setVisible(false);
            toolbar.doLayout();
            assertEquals(40, second.getWidth());
            first.setVisible(true);
            toolbar.setOrientation(JToolBar.VERTICAL);
            size(toolbar, 80);
            toolbar.doLayout();
            assertEquals(40, second.getHeight());
            assertEquals(40, second.getY());
            assertFalse(toolbar.getComponent(2).isVisible());
        });
    }

    @Test
    void popupUsesOriginalControlsAndRestoresOrderAndListeners() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless());
        SwingUtilities.invokeAndWait(() -> {
            JFrame frame = new JFrame();

            try {
                GToolBar toolbar = toolbar(JToolBar.HORIZONTAL);
                JButton first = item();
                JToggleButton toggle = new JToggleButton();
                toggle.setPreferredSize(new Dimension(40, 40));
                JTextField field = new JTextField("value");
                field.setPreferredSize(new Dimension(40, 40));
                AtomicInteger calls = new AtomicInteger();
                toggle.addActionListener(event -> calls.incrementAndGet());
                toolbar.add(first);
                toolbar.add(toggle);
                toolbar.add(field);
                frame.setFocusableWindowState(false);
                toolbar.setPreferredSize(new Dimension(60, 40));
                frame.setContentPane(toolbar);
                frame.pack();
                frame.setVisible(true);
                size(toolbar, 60);
                toolbar.doLayout();
                JButton overflow = (JButton) toolbar.getComponent(3);
                overflow.doClick();
                assertInstanceOf(JPopupMenu.class, toggle.getParent());
                JPopupMenu popup = (JPopupMenu) toggle.getParent();
                assertSame(popup, field.getParent());
                toolbar.doLayout();
                assertTrue(popup.isVisible());
                toggle.doClick();
                SecondaryLoop loop = Toolkit.getDefaultToolkit().getSystemEventQueue().createSecondaryLoop();
                SwingUtilities.invokeLater(loop::exit);
                loop.enter();
                assertTrue(toggle.isSelected());
                assertEquals(1, calls.get());
                assertFalse(popup.isVisible());
                assertSame(toggle, toolbar.getComponent(1));
                assertSame(field, toolbar.getComponent(2));
                assertEquals(1, toggle.getActionListeners().length);
                assertEquals("value", field.getText());
                overflow.doClick();
                popup.setVisible(false);
                assertSame(field, toolbar.getComponent(2));
                overflow.doClick();
                toolbar.remove(field);
                assertNull(field.getParent());
                assertSame(toggle, toolbar.getComponent(1));
            } finally {
                frame.dispose();
            }
        });
    }

    @ParameterizedTest
    @CsvSource({"0, false", "0, true", "1, false", "1, true"})
    void overflowButtonCanOpenAnotherPopup(int orientation, boolean cancel) throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless());
        JFrame[] frame = new JFrame[1];
        GToolBar[] toolbar = new GToolBar[1];
        JButton[] button = new JButton[1];
        JPopupMenu[] menu = new JPopupMenu[1];
        AtomicInteger calls = new AtomicInteger();

        try {
            SwingUtilities.invokeAndWait(() -> {
                frame[0] = new JFrame();
                frame[0].setFocusableWindowState(false);
                toolbar[0] = toolbar(orientation);
                JButton first = item();
                first.setPreferredSize(new Dimension(200, 200));
                toolbar[0].add(first);
                button[0] = item();
                menu[0] = new JPopupMenu();
                JMenuItem command = new JMenuItem("Command");
                command.addActionListener(event -> calls.incrementAndGet());
                menu[0].add(command);
                button[0].addActionListener(event -> {
                    assertInstanceOf(JPopupMenu.class, button[0].getParent());
                    assertTrue(button[0].getHeight() > 0);
                    menu[0].show(button[0], 0, button[0].getHeight());
                });
                toolbar[0].add(button[0]);
                toolbar[0].setPreferredSize(orientation == JToolBar.HORIZONTAL
                        ? new Dimension(60, 40) : new Dimension(40, 60));
                frame[0].setContentPane(toolbar[0]);
                frame[0].pack();
                frame[0].setVisible(true);
                toolbar[0].doLayout();
                ((JButton) toolbar[0].getComponent(2)).doClick();
            });
            SwingUtilities.invokeAndWait(() -> {
                assertInstanceOf(JPopupMenu.class, button[0].getParent());
                button[0].doClick();
            });
            SwingUtilities.invokeAndWait(() -> {
                assertTrue(menu[0].isVisible());
                assertTrue(button[0].isShowing());
                assertSame(button[0], menu[0].getInvoker());
                if (cancel) {
                    MenuSelectionManager.defaultManager().clearSelectedPath();
                } else {
                    // The menu UI clears selection before invoking a command.
                    MenuSelectionManager.defaultManager().clearSelectedPath();
                    ((JMenuItem) menu[0].getComponent(0)).doClick();
                    assertEquals(1, calls.get());
                }
            });
            SwingUtilities.invokeAndWait(() -> {
                assertFalse(menu[0].isVisible());
                assertSame(toolbar[0], button[0].getParent());
                assertSame(button[0], toolbar[0].getComponent(1));
                assertEquals(1, button[0].getActionListeners().length);
            });
        } finally {
            SwingUtilities.invokeAndWait(() -> {
                menu[0].setVisible(false);
                frame[0].dispose();
            });
        }
    }

    private static GToolBar toolbar(int orientation) {
        GToolBar toolbar = new GToolBar(orientation);
        toolbar.setFloatable(false);
        toolbar.setBorder(BorderFactory.createEmptyBorder());
        return toolbar;
    }

    private static JButton item() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(40, 40));
        return button;
    }

    private static void size(GToolBar toolbar, int length) {
        toolbar.setSize(toolbar.getOrientation() == JToolBar.HORIZONTAL
                ? new Dimension(length, 40) : new Dimension(40, length));
    }

    private static int length(GToolBar toolbar, Dimension size) {
        return toolbar.getOrientation() == JToolBar.HORIZONTAL ? size.width : size.height;
    }
}
