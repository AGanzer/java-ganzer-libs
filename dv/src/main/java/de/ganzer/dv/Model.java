package de.ganzer.dv;

import de.ganzer.core.util.Strings;
import de.ganzer.dv.internals.DVMessages;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.List;

/**
 * A basic model that can be used to load save and modify data.
 * <p>
 * A model in the sense of this framework should only represent one or more
 * entities of data but should not act as an entity itself. The model does have
 * no internal logic ad is just a container of modifiable data.
 *
 * @see Document
 * @since 6.0.0
 */
public abstract class Model {
    /**
     * The name of the "newData" property used for {@link PropertyChangeEvent}'s.
     *
     * @see #addPropertyChangeListener(PropertyChangeListener)
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     */
    public static final String NEW_DATA_PROPERTY = "newData";

    /**
     * The name of the "readOnly" property used for {@link PropertyChangeEvent}'s.
     *
     * @see #addPropertyChangeListener(PropertyChangeListener)
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     */
    public static final String READ_ONLY_PROPERTY = "readOnly";

    /**
     * The name of the "canUndo" property used for {@link PropertyChangeEvent}'s.
     *
     * @see #addPropertyChangeListener(PropertyChangeListener)
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     */
    public static final String CAN_UNDO_PROPERTY = "canUndo";

    /**
     * The name of the "canRedo" property used for {@link PropertyChangeEvent}'s.
     *
     * @see #addPropertyChangeListener(PropertyChangeListener)
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     */
    public static final String CAN_REDO_PROPERTY = "canRedo";

    /**
     * The name of the "undoTitle" property used for {@link PropertyChangeEvent}'s.
     *
     * @see #addPropertyChangeListener(PropertyChangeListener)
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     */
    public static final String UNDO_TITLE_PROPERTY = "undoTitle";

    /**
     * The name of the "redoTitle" property used for {@link PropertyChangeEvent}'s.
     *
     * @see #addPropertyChangeListener(PropertyChangeListener)
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     */
    public static final String REDO_TITLE_PROPERTY = "redoTitle";

    /**
     * The name of the "name" property used for {@link PropertyChangeEvent}'s.
     *
     * @see #addPropertyChangeListener(PropertyChangeListener)
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     */
    public static final String NAME_PROPERTY = "name";

    /**
     * The name of the "modified" property used for {@link PropertyChangeEvent}'s.
     *
     * @see #addPropertyChangeListener(PropertyChangeListener)
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     */
    public static final String MODIFIED_PROPERTY = "modified";

    private static final int IS_NEW = 0x01;
    private static final int IS_READONLY = 0x02;
    private static final int IS_MODIFIED = 0x04;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private UndoManager undoManager = new UndoManager();

    private String name;
    private int flags;

    /**
     * Creates a new instance that loads existing data.
     * <p>
     * {@link #doLoadData()} is invoked by this constructor.
     *
     * @param name The name of the model.
     * @param readOnly Indicates whether the model is read-only.
     *
     * @throws IllegalArgumentException if {@code name} is {@code null} or empty
     *          or does contain only blanks.
     * @throws DVLoadException on any error loading data.
     */
    protected Model(String name, boolean readOnly) throws DVLoadException {
        this(name, readOnly, false);
    }

    /**
     * Creates a new instance.
     * <p>
     * {@link #doCreateData()} is invoked if {@code newData} is {@code true};
     * otherwise, {@link #doLoadData()} is invoked.
     *
     * @param name The name of the model.
     * @param readOnly Indicates whether the model is read-only.
     * @param newData Indicates whether the model shall create new data;
     *
     * @throws IllegalArgumentException if {@code newData} is {@code false} and
     *         {@code name} is {@code null} or empty or does contain only blanks.
     * @throws DVLoadException on any error loading data.
     */
    protected Model(String name, boolean readOnly, boolean newData) throws DVLoadException {
        if (newData && Strings.isNullOrBlank(name))
            throw new IllegalArgumentException("name is null or blank");

        this.name = name;

        if (readOnly)
            flags |= IS_READONLY;

        if (newData)
            createData();
        else
            loadData();
    }

    /**
     * The name of the model.
     * <p>
     * The meaning of the name is implementation defined.
     *
     * @return The name of the model or {@code null} if no name is available.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the model.
     * <p>
     * This fires a property change event with the property name set to
     * {@link #NAME_PROPERTY}.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        var org = this.name;
        this.name = name;

        firePropertyChange(NAME_PROPERTY, org, this.name);
    }

    /**
     * Indicates whether this data is read from a persistent memory or if it is
     * created newly.
     * <p>
     * If this is changed, a property change event is fired with the property
     * name set to {@link #NEW_DATA_PROPERTY}.
     *
     * @return {@code true} if the data is newly created.
     */
    public final boolean isNewData() {
        return (flags & IS_NEW) != 0;
    }

    /**
     * Indicates whether the data of the model can be modified.
     * <p>
     * Implementors should not allow any modification if this is {@code true}.
     * <p>
     * If this is changed, a property change event is fired with the property
     * name set to {@link #READ_ONLY_PROPERTY}.
     *
     * @return {@code false} if the data can be modified.
     */
    public final boolean isReadOnly() {
        return (flags & IS_READONLY) != 0;
    }

    /**
     * indicates whether the model is modified.
     *
     * @return {@code true} if the model's data is modified.
     *
     * @see #setModified(boolean)
     */
    public final boolean isModified() {
        return (flags & IS_MODIFIED) != 0;
    }

    /**
     * Sets or unsets the modification flag of the model.
     * <p>
     * This fires a property change event with the property name set to
     * {@link #MODIFIED_PROPERTY}.
     *
     * @param modified {@code true} to indicate the model as modified.
     *
     * @throws UnsupportedOperationException if the model is read-only.
     *
     * @see #isReadOnly()
     */
    public void setModified(boolean modified) {
        if (modified && isReadOnly())
            throw new UnsupportedOperationException("Model is read-only");

        var old = isModified();

        if (modified)
            flags |= IS_MODIFIED;
        else
            flags &= ~IS_MODIFIED;

        firePropertyChange(MODIFIED_PROPERTY, old, modified);
    }

    /**
     * Indicates whether an action that can be undone is available.
     * <p>
     * If this is changed, a property change event is fired with the property
     * name set to {@link #CAN_UNDO_PROPERTY}.
     *
     * @return {@code true} if an action can be undone.
     */
    public final boolean canUndo() {
        return undoManager.canUndo();
    }

    /**
     * Indicates whether an action that can be redone is available.
     * <p>
     * If this is changed, a property change event is fired with the property
     * name set to {@link #CAN_REDO_PROPERTY}.
     *
     * @return {@code true} if an action can be redone.
     */
    public final boolean canRedo() {
        return undoManager.canRedo();
    }

    /**
     * Gets the title of the current undoable action.
     * <p>
     * This default implementation is provided because not every model provides
     * undoable changes. This implementation does always return {@code null}.
     * <p>
     * If this is changed, implementors should fire a property change event with
     * the property name set to {@link #REDO_TITLE_PROPERTY}.
     *
     * @return The title of the current undoable action or {@code null} if there
     *         is no undoable action.
     */
    public String getUndoTitle() {
        return undoManager.getUndoTitle();
    }

    /**
     * Gets the title of the current redoable action.
     * <p>
     * This default implementation is provided because not every model provides
     * undoable changes. This implementation does always return {@code null}.
     * <p>
     * If this is changed, implementors should fire a property change event with
     * the property name set to {@link #UNDO_TITLE_PROPERTY}.
     *
     * @return The title of the current redoable action or {@code null} if there
     *         is no redoable action.
     */
    public String getRedoTitle() {
        return undoManager.getRedoTitle();
    }

    /**
     * Gets the titles of all available actions that can be undone.
     *
     * @return The titles of all available actions that can be undone.
     */
    public List<String> getUndoTitles() {
        return undoManager.getUndoTitles();
    }

    /**
     * Gets the titles of all available actions that can be redone.
     *
     * @return The titles of all available actions that can be redone.
     */
    public List<String> getRedoTitles() {
        return undoManager.getRedoTitles();
    }

    /**
     * Removes all undoable actions.
     */
    public void clearUndoableStack() {
        var oldUndo = undoManager.canUndo();
        var oldUndoTitle = undoManager.getUndoTitle();
        var oldRedo = undoManager.canRedo();
        var oldRedoTitle = undoManager.getRedoTitle();

        undoManager.clear();

        firePropertyChange(CAN_UNDO_PROPERTY, oldUndo, undoManager.canUndo());
        firePropertyChange(UNDO_TITLE_PROPERTY, oldUndoTitle, null);
        firePropertyChange(CAN_REDO_PROPERTY, oldRedo, undoManager.canRedo());
        firePropertyChange(REDO_TITLE_PROPERTY, oldRedoTitle, null);
    }

    /**
     * Pushes an undoable action to the stack of undoable actions.
     * <p>
     * This default implementation invokes {@link #addUndoable(Undoable, boolean)}
     * with the second argument set to {@code false}.
     * <p>
     * Implementors should set the modification flag if this is invoked.
     *
     * @param undoable The action to push. This is not executed but only pushed.
     *
     * @see #addUndoable(Undoable, boolean)
     */
    public final void addUndoable(Undoable undoable) {
        addUndoable(undoable, false);
    }

    /**
     * Pushes an undoable action to the stack of undoable actions.
     *
     * @param undoable The action to push. This is not executed but only pushed.
     * @param execute If this is {@code true}, {@link Undoable#execute()} is
     *         invoked.
     */
    public void addUndoable(Undoable undoable, boolean execute) {
        var oldUndo = undoManager.canUndo();
        var oldUndoTitle = undoManager.getUndoTitle();
        var oldRedo = undoManager.canRedo();
        var oldRedoTitle = undoManager.getRedoTitle();

        undoManager.add(undoable, execute);
        setModified(true);

        firePropertyChange(CAN_UNDO_PROPERTY, oldUndo, undoManager.canUndo());
        firePropertyChange(UNDO_TITLE_PROPERTY, oldUndoTitle, undoManager.getUndoTitle());
        firePropertyChange(CAN_REDO_PROPERTY, oldRedo, undoManager.canRedo());
        firePropertyChange(REDO_TITLE_PROPERTY, oldRedoTitle, undoManager.getRedoTitle());
    }

    /**
     * Undoes the latest undoable action.
     */
    public void undo() {
        var oldUndo = undoManager.canUndo();
        var oldUndoTitle = undoManager.getUndoTitle();
        var oldRedo = undoManager.canRedo();
        var oldRedoTitle = undoManager.getRedoTitle();

        undoManager.undo();
        setModified(true);

        firePropertyChange(CAN_UNDO_PROPERTY, oldUndo, undoManager.canUndo());
        firePropertyChange(UNDO_TITLE_PROPERTY, oldUndoTitle, undoManager.getUndoTitle());
        firePropertyChange(CAN_REDO_PROPERTY, oldRedo, undoManager.canRedo());
        firePropertyChange(REDO_TITLE_PROPERTY, oldRedoTitle, undoManager.getRedoTitle());
    }

    /**
     * Redoes the latest undoable action.
     */
    public void redo() {
        var oldUndo = undoManager.canUndo();
        var oldUndoTitle = undoManager.getUndoTitle();
        var oldRedo = undoManager.canRedo();
        var oldRedoTitle = undoManager.getRedoTitle();

        undoManager.redo();
        setModified(true);

        firePropertyChange(CAN_UNDO_PROPERTY, oldUndo, undoManager.canUndo());
        firePropertyChange(UNDO_TITLE_PROPERTY, oldUndoTitle, undoManager.getUndoTitle());
        firePropertyChange(CAN_REDO_PROPERTY, oldRedo, undoManager.canRedo());
        firePropertyChange(REDO_TITLE_PROPERTY, oldRedoTitle, undoManager.getRedoTitle());
    }

    /**
     * Creates new data.
     * <p>
     * This resets the modification flag and sets the new data flag and removes
     * all undoable actions.
     *
     * @see #doCreateData()
     */
    public void createData() {
        doCreateData();

        setModified(false);
        setNewData(true);

        undoManager.clear();
    }

    /**
     * Loads data from a file, a database, or any other source.
     * <p>
     * This resets the modification and the new data flags and removes all
     * undoable actions.
     *
     * @throws DVLoadException on any error.
     *
     * @see #doLoadData()
     */
    public void loadData() throws DVLoadException {
        try {
            doLoadData();
        } catch (Exception e) {
            String message = getLoadErrorMessage(getName(), e);
            throw new DVLoadException(
                    message != null ? message : DVMessages.get("dv.error.load", getName(), e.getLocalizedMessage()),
                    e);
        }

        setModified(false);
        setNewData(false);

        undoManager.clear();
    }

    /**
     * Writes the data into a file, a database, or any other target.
     * <p>
     * This resets the modification, the read-only, and the new data flags.
     *
     * @throws DVSaveException on any error.
     *
     * @see #doSaveData()
     * @see #getSaveErrorMessage(String, Throwable)
     */
    public void saveData() throws DVSaveException {
        try {
            doSaveData();
        } catch (Exception e) {
            String message = getSaveErrorMessage(getName(), e);
            throw new DVSaveException(
                    message != null ? message : DVMessages.get("dv.error.save", getName(), e.getLocalizedMessage()),
                    e);
        }

        setModified(false);
        setNewData(false);
        resetReadOnly();
    }

    /**
     * Adds a listener for all properties.
     *
     * @param listener The listener to install.
     *
     * @throws NullPointerException {@code originator} or {@code listener} is
     *          {@code null}.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(null, listener);
    }

    /**
     * Adds a listener for the specified property.
     *
     * @param propertyName The name of the property to add the listener fo or
     *         {@code null} or an empty string to listen to all properties.
     * @param listener The listener to install.
     *
     * @throws NullPointerException {@code originator} or {@code listener} is
     *          {@code null}.
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * Removes an installed listener.
     *
     * @param listener The listener to install.
     *
     * @throws NullPointerException {@code originator} or {@code listener} is
     *          {@code null}.
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(null, listener);
    }

    /**
     * Removes an installed listener for the specified property.
     *
     * @param propertyName The name of the property to add the listener fo or
     *         {@code null} or an empty string for listeners that are installed
     *         for all properties.
     * @param listener The listener to install.
     *
     * @throws NullPointerException {@code originator} or {@code listener} is
     *          {@code null}.
     */
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propertyName, listener);
    }

    /**
     * Reports a bound property update to listeners
     * that have been registered to track updates of
     * all properties or a property with the specified name.
     * <p>
     * No event is fired if old and new values are equal and non-null.
     *
     * @param propertyName  the programmatic name of the property that was changed
     * @param oldValue      the old value of the property
     * @param newValue      the new value of the property
     */
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Reports a bound indexed property update to listeners
     * that have been registered to track updates of
     * all properties or a property with the specified name.
     * <p>
     * No event is fired if old and new values are equal and non-null.
     *
     * @param propertyName  the programmatic name of the property that was changed
     * @param index         the index of the property element that was changed
     * @param oldValue      the old value of the property
     * @param newValue      the new value of the property
     */
    protected void fireIndexedPropertyChange(String propertyName, int index, Object oldValue, Object newValue) {
        pcs.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
    }

    /**
     * Resets the read-only flag.
     * <p>
     * This fires a property change event with the property name set to
     * {@link #READ_ONLY_PROPERTY}.
     */
    protected void resetReadOnly() {
        var old = isReadOnly();
        flags &= ~IS_READONLY;

        firePropertyChange(READ_ONLY_PROPERTY, old, false);
    }

    /**
     * Sets or unsets the new data flag.
     * <p>
     * This fires a property change event with the property name set to
     * {@link #NEW_DATA_PROPERTY}.
     *
     * @param newData {@code true} to set the model to new data.
     */
    protected void setNewData(boolean newData) {
        var old = isNewData();

        if (newData)
            flags |= IS_NEW;
        else
            flags &= ~IS_NEW;

        firePropertyChange(NEW_DATA_PROPERTY, old, newData);
    }

    /**
     * Gets the undo manager of this model.
     *
     * @return The undo manager.
     */
    protected UndoManager getUndoManager() {
        return undoManager;
    }

    /**
     * Sets the undo manager.
     *
     * @param undoManager The undo manager to set. If this is {@code null}, an
     *         instance of {@link UndoManager} is installed.
     */
    protected void setUndoManager(UndoManager undoManager) {
        this.undoManager = undoManager != null ? undoManager : new UndoManager();
    }

    /**
     * Invoked to get an alternative error message for the load operation.
     * <p>
     * Implementors should return {@code null} if they don't want to provide
     * an alternative error message.
     *
     * @param documentName The name of the document that cannot be loaded.
     * @param cause The causing exception.
     *
     * @return The alternative error message or {@code null} if no alternative
     *         error message is available. This implementation does always
     *         return {@code null}.
     */
    @SuppressWarnings("unused")
    protected String getLoadErrorMessage(String documentName, Throwable cause) {
        return null;
    }

    /**
     * Invoked to get an alternative error message for the save operation.
     * <p>
     * Implementors should return {@code null} if they don't want to provide
     * an alternative error message.
     *
     * @param documentName The name of the document that cannot be saved.
     * @param cause The causing exception.
     *
     * @return The alternative error message or {@code null} if no alternative
     *         error message is available. This implementation does always
     *         return {@code null}.
     */
    @SuppressWarnings("unused")
    protected String getSaveErrorMessage(String documentName, Throwable cause) {
        return null;
    }

    /**
     * Invoked by {@link #createData()} to create new data.
     */
    protected abstract void doCreateData();

    /**
     * Invoked by {@link #loadData()} to load data from a storage.
     *
     * @throws IOException on any error.
     */
    protected abstract void doLoadData() throws IOException;

    /**
     * Invoked by {@link #saveData()} to write the data into a storage.
     *
     * @throws IOException on any error.
     */
    protected abstract void doSaveData()throws IOException;
}
