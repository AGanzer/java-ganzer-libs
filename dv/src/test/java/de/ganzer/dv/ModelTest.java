package de.ganzer.dv;

import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {

    private static class MemoryModel extends Model {
        private String memoryBuffer;
        private boolean shouldThrowOnLoad = false;
        private boolean shouldThrowOnSave = false;

        public MemoryModel(String name, boolean readOnly, boolean newData) throws DVLoadException {
            super(name, readOnly, newData);
        }

        public MemoryModel(String name, boolean readOnly) throws DVLoadException {
            super(name, readOnly);
        }

        @Override
        protected void doCreateData() {
            this.memoryBuffer = "initial-new-data";
        }

        @Override
        protected void doLoadData() throws IOException {
            if (shouldThrowOnLoad) {
                throw new IOException("Simulated load failure");
            }
            this.memoryBuffer = "loaded-memory-data";
        }

        @Override
        protected void doSaveData() throws IOException {
            if (shouldThrowOnSave) {
                throw new IOException("Simulated save failure");
            }
        }

        public String getMemoryBuffer() {
            return memoryBuffer;
        }

        public void setMemoryBuffer(String memoryBuffer) {
            this.memoryBuffer = memoryBuffer;
            setModified(true);
        }

        public UndoManager exposedGetUndoManager() {
            return getUndoManager();
        }

        public void exposedSetUndoManager(UndoManager undoManager) {
            setUndoManager(undoManager);
        }

        public void exposedSetNewData(boolean newData) {
            setNewData(newData);
        }

        public void exposedResetReadOnly() {
            resetReadOnly();
        }

        public void exposedFireIndexedPropertyChange(String propertyName, int index, Object oldValue, Object newValue) {
            fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
        }
    }

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
    void testConstructors() throws Exception {
        var newModel = new MemoryModel("NewDoc", false, true);
        assertEquals("NewDoc", newModel.getName());
        assertFalse(newModel.isReadOnly());
        assertTrue(newModel.isNewData());
        assertFalse(newModel.isModified());
        assertEquals("initial-new-data", newModel.getMemoryBuffer());

        var loadedModel = new MemoryModel("LoadedDoc", true, false);
        assertEquals("LoadedDoc", loadedModel.getName());
        assertTrue(loadedModel.isReadOnly());
        assertFalse(loadedModel.isNewData());
        assertFalse(loadedModel.isModified());
        assertEquals("loaded-memory-data", loadedModel.getMemoryBuffer());

        var defaultConstructorModel = new MemoryModel("Doc2", false);
        assertEquals("Doc2", defaultConstructorModel.getName());
        assertFalse(defaultConstructorModel.isNewData());
    }

    @Test
    void testBlankNameThrowsExceptionOnNewData() {
        assertThrows(IllegalArgumentException.class, () -> new MemoryModel(null, false, true));
        assertThrows(IllegalArgumentException.class, () -> new MemoryModel("", false, true));
        assertThrows(IllegalArgumentException.class, () -> new MemoryModel("   ", false, true));
    }

    @Test
    void testNamePropertyChange() throws Exception {
        var model = new MemoryModel("InitialName", false, true);
        var events = new ArrayList<PropertyChangeEvent>();
        model.addPropertyChangeListener(Model.NAME_PROPERTY, events::add);

        model.setName("UpdatedName");
        assertEquals("UpdatedName", model.getName());
        assertEquals(1, events.size());
        assertEquals("InitialName", events.get(0).getOldValue());
        assertEquals("UpdatedName", events.get(0).getNewValue());
    }

    @Test
    void testModifiedAndReadOnly() throws Exception {
        var model = new MemoryModel("Doc", false, true);
        var events = new ArrayList<PropertyChangeEvent>();
        model.addPropertyChangeListener(Model.MODIFIED_PROPERTY, events::add);

        assertFalse(model.isModified());
        model.setModified(true);
        assertTrue(model.isModified());
        assertEquals(1, events.size());

        model.setModified(false);
        assertFalse(model.isModified());
        assertEquals(2, events.size());

        var readOnlyModel = new MemoryModel("ReadOnlyDoc", true, false);
        assertThrows(UnsupportedOperationException.class, () -> readOnlyModel.setModified(true));

        readOnlyModel.exposedResetReadOnly();
        assertFalse(readOnlyModel.isReadOnly());
        readOnlyModel.setModified(true);
        assertTrue(readOnlyModel.isModified());
    }

    @Test
    void testUndoRedoDelegation() throws Exception {
        var model = new MemoryModel("Doc", false, true);
        var events = new ArrayList<PropertyChangeEvent>();
        model.addPropertyChangeListener(events::add);

        var edit = new SimpleUndoable("Action 1");
        model.addUndoable(edit, true);

        assertEquals(1, edit.value);
        assertTrue(model.canUndo());
        assertFalse(model.canRedo());
        assertEquals("Action 1", model.getUndoTitle());
        assertEquals(List.of("Action 1"), model.getUndoTitles());

        model.undo();
        assertEquals(0, edit.value);
        assertFalse(model.canUndo());
        assertTrue(model.canRedo());
        assertEquals("Action 1", model.getRedoTitle());

        model.redo();
        assertEquals(1, edit.value);
        assertTrue(model.canUndo());
        assertFalse(model.canRedo());

        model.clearUndoableStack();
        assertFalse(model.canUndo());
        assertFalse(model.canRedo());
    }

    @Test
    void testSaveAndLoadData() throws Exception {
        var model = new MemoryModel("Doc", false, true);
        model.setMemoryBuffer("modified data");
        assertTrue(model.isModified());
        assertTrue(model.isNewData());

        model.saveData();
        assertFalse(model.isModified());
        assertFalse(model.isNewData());

        model.setMemoryBuffer("another change");
        assertTrue(model.isModified());
        model.loadData();
        assertEquals("loaded-memory-data", model.getMemoryBuffer());
        assertFalse(model.isModified());
    }

    @Test
    void testLoadDataThrowsDVLoadException() throws Exception {
        var model = new MemoryModel("Doc", false, true);
        model.shouldThrowOnLoad = true;

        assertThrows(DVLoadException.class, model::loadData);
    }

    @Test
    void testSaveDataThrowsDVSaveException() throws Exception {
        var model = new MemoryModel("Doc", false, true);
        model.shouldThrowOnSave = true;

        assertThrows(DVSaveException.class, model::saveData);
    }

    @Test
    void testCustomUndoManager() throws Exception {
        var model = new MemoryModel("Doc", false, true);
        var customManager = new UndoManager(5);

        model.exposedSetUndoManager(customManager);
        assertSame(customManager, model.exposedGetUndoManager());

        model.exposedSetUndoManager(null);
        assertNotNull(model.exposedGetUndoManager());
    }

    @Test
    void testListenersManagement() throws Exception {
        var model = new MemoryModel("Doc", false, true);
        var nameEvents = new ArrayList<PropertyChangeEvent>();
        var modifiedEvents = new ArrayList<PropertyChangeEvent>();

        java.beans.PropertyChangeListener nameListener = nameEvents::add;
        java.beans.PropertyChangeListener modifiedListener = modifiedEvents::add;

        model.addPropertyChangeListener(Model.NAME_PROPERTY, nameListener);
        model.addPropertyChangeListener(Model.MODIFIED_PROPERTY, modifiedListener);

        model.setName("NewName");
        assertEquals(1, nameEvents.size());
        assertEquals(0, modifiedEvents.size());

        model.setModified(true);
        assertEquals(1, nameEvents.size());
        assertEquals(1, modifiedEvents.size());

        model.removePropertyChangeListener(Model.NAME_PROPERTY, nameListener);
        model.removePropertyChangeListener(Model.MODIFIED_PROPERTY, modifiedListener);

        model.setName("AnotherName");
        model.setModified(false);
        assertEquals(1, nameEvents.size());
        assertEquals(1, modifiedEvents.size());
    }
}
