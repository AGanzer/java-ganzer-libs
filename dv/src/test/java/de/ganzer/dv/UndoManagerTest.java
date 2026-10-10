package de.ganzer.dv;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UndoManagerTest {

    private static class SimpleUndoable implements Undoable {
        private final String title;
        private int value = 0;

        public SimpleUndoable(String title) {
            this.title = title;
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public void execute() {
            value++;
        }

        @Override
        public void undo() {
            value--;
        }
    }

    @Test
    void testConstructorsAndMaxCount() {
        var managerDefault = new UndoManager();
        assertEquals(99, managerDefault.getMaxUndoableCount());

        var managerCustom = new UndoManager(10);
        assertEquals(10, managerCustom.getMaxUndoableCount());

        managerCustom.setMaxUndoableCount(5);
        assertEquals(5, managerCustom.getMaxUndoableCount());
    }

    @Test
    void testAddWithoutExecute() {
        var manager = new UndoManager();
        var edit = new SimpleUndoable("Action 1");

        assertFalse(manager.canUndo());
        assertFalse(manager.canRedo());

        manager.add(edit);

        assertEquals(0, edit.value);
        assertTrue(manager.canUndo());
        assertFalse(manager.canRedo());
        assertEquals("Action 1", manager.getUndoTitle());
        assertNull(manager.getRedoTitle());
        assertEquals(List.of("Action 1"), manager.getUndoTitles());
        assertEquals(List.of(), manager.getRedoTitles());
    }

    @Test
    void testAddWithExecute() {
        var manager = new UndoManager();
        var edit = new SimpleUndoable("Action 1");

        manager.add(edit, true);

        assertEquals(1, edit.value);
        assertTrue(manager.canUndo());
        assertFalse(manager.canRedo());
    }

    @Test
    void testUndoAndRedo() {
        var manager = new UndoManager();
        var edit1 = new SimpleUndoable("Action 1");
        var edit2 = new SimpleUndoable("Action 2");

        manager.add(edit1, true);
        manager.add(edit2, true);

        assertEquals(1, edit1.value);
        assertEquals(1, edit2.value);
        assertEquals("Action 2", manager.getUndoTitle());
        assertEquals(List.of("Action 1", "Action 2"), manager.getUndoTitles());

        manager.undo();
        assertEquals(0, edit2.value);
        assertTrue(manager.canUndo());
        assertTrue(manager.canRedo());
        assertEquals("Action 1", manager.getUndoTitle());
        assertEquals("Action 2", manager.getRedoTitle());
        assertEquals(List.of("Action 1"), manager.getUndoTitles());
        assertEquals(List.of("Action 2"), manager.getRedoTitles());

        manager.undo();
        assertEquals(0, edit1.value);
        assertFalse(manager.canUndo());
        assertTrue(manager.canRedo());
        assertNull(manager.getUndoTitle());
        assertEquals("Action 1", manager.getRedoTitle());

        // Undo on empty undo stack does nothing
        manager.undo();
        assertFalse(manager.canUndo());

        manager.redo();
        assertEquals(1, edit1.value);
        assertTrue(manager.canUndo());
        assertTrue(manager.canRedo());
        assertEquals("Action 1", manager.getUndoTitle());
        assertEquals("Action 2", manager.getRedoTitle());

        manager.redo();
        assertEquals(1, edit2.value);
        assertTrue(manager.canUndo());
        assertFalse(manager.canRedo());

        // Redo on empty redo stack does nothing
        manager.redo();
        assertFalse(manager.canRedo());
    }

    @Test
    void testAddClearsRedoStack() {
        var manager = new UndoManager();
        var edit1 = new SimpleUndoable("Action 1");
        var edit2 = new SimpleUndoable("Action 2");
        var edit3 = new SimpleUndoable("Action 3");

        manager.add(edit1);
        manager.add(edit2);
        manager.undo();
        assertTrue(manager.canRedo());

        manager.add(edit3);
        assertFalse(manager.canRedo());
        assertEquals(List.of("Action 1", "Action 3"), manager.getUndoTitles());
    }

    @Test
    void testClear() {
        var manager = new UndoManager();
        var edit1 = new SimpleUndoable("Action 1");
        var edit2 = new SimpleUndoable("Action 2");

        manager.add(edit1);
        manager.add(edit2);
        manager.undo();

        assertTrue(manager.canUndo());
        assertTrue(manager.canRedo());

        manager.clear();
        assertFalse(manager.canUndo());
        assertFalse(manager.canRedo());
        assertNull(manager.getUndoTitle());
        assertNull(manager.getRedoTitle());
    }

    @Test
    void testMaxUndoableCountTruncatesStack() {
        var manager = new UndoManager(2);
        var edit1 = new SimpleUndoable("Action 1");
        var edit2 = new SimpleUndoable("Action 2");
        var edit3 = new SimpleUndoable("Action 3");

        manager.add(edit1);
        manager.add(edit2);
        assertEquals(List.of("Action 1", "Action 2"), manager.getUndoTitles());

        manager.add(edit3);
        assertEquals(List.of("Action 2", "Action 3"), manager.getUndoTitles());

        manager.setMaxUndoableCount(1);
        assertEquals(List.of("Action 3"), manager.getUndoTitles());
    }
}
