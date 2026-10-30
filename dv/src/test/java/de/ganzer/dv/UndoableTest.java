package de.ganzer.dv;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UndoableTest {

    private static class SimpleUndoable implements Undoable {
        private final String title;
        private int executionCount = 0;
        private int undoCount = 0;

        public SimpleUndoable(String title) {
            this.title = title;
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public void execute() {
            executionCount++;
        }

        @Override
        public void undo() {
            undoCount++;
        }
    }

    @Test
    void testGetTitle() {
        var undoable = new SimpleUndoable("Action 1");
        assertEquals("Action 1", undoable.getTitle());
    }

    @Test
    void testExecuteAndUndo() {
        var undoable = new SimpleUndoable("Action 1");
        assertEquals(0, undoable.executionCount);
        assertEquals(0, undoable.undoCount);

        undoable.execute();
        assertEquals(1, undoable.executionCount);
        assertEquals(0, undoable.undoCount);

        undoable.undo();
        assertEquals(1, undoable.executionCount);
        assertEquals(1, undoable.undoCount);
    }

    @Test
    void testDefaultRedoCallsExecute() {
        var undoable = new SimpleUndoable("Action 1");
        undoable.redo();
        assertEquals(1, undoable.executionCount);

        undoable.redo();
        assertEquals(2, undoable.executionCount);
    }
}
