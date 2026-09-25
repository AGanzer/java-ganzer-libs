package de.ganzer.core.dv;

import de.ganzer.core.util.Strings;

import java.util.List;

/**
 * A basic abstract model that implements the {@link Model} interface.
 *
 * @since 5.6.0
 */
public abstract class AbstractModel implements Model {
    private static final int IS_NEW = 0x01;
    private static final int IS_READONLY = 0x02;
    private static final int IS_MODIFIED = 0x04;

    private final ModelChangeSupport pcs = new ModelChangeSupport(this);

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
     */
    protected AbstractModel(String name, boolean readOnly) {
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
     */
    protected AbstractModel(String name, boolean readOnly, boolean newData) {
        if (newData && Strings.isNullOrBlank(name))
            throw new IllegalArgumentException("name is null or blank");

        this.name = name;

        if (newData)
            flags |= IS_NEW;

        if (readOnly)
            flags |= IS_READONLY;

        if (newData)
            doCreateData();
        else
            doLoadData();
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
    @Override
    public boolean isNewData() {
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
    @Override
    public boolean isReadOnly() {
        return (flags & IS_READONLY) != 0;
    }

    /**
     * The name of the model.
     * <p>
     * The meaning of the name is implementation defined.
     *
     * @return The name of the model or {@code null} if no name is available.
     */
    @Override
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
    @Override
    public void setName(String name) {
        var org = this.name;
        this.name = name;

        fireChange(NAME_PROPERTY, null, org, this.name);
    }

    /**
     * indicates whether the model is modified.
     *
     * @return {@code true} if the model's data is modified.
     */
    @Override
    public boolean isModified() {
        return (flags & IS_MODIFIED) != 0;
    }

    /**
     * Sets or unsets the modification flag of the model.
     * <p>
     * This fires a property change event with the property name set to
     * {@link #MODIFIED_PROPERTY}.
     *
     * @param modified {@code true} to indicate the model as modified.
     */
    @Override
    public void setModified(boolean modified) {
        var old = isModified();

        if (modified)
            flags |= IS_MODIFIED;
        else
            flags &= ~IS_MODIFIED;

        fireChange(MODIFIED_PROPERTY, null, old, modified);
    }

    /**
     * Indicates whether an action that can be undone is available.
     * <p>
     * If this is changed, a property change event is fired with the property
     * name set to {@link #CAN_UNDO_PROPERTY}.
     *
     * @return {@code true} if an action can be undone.
     */
    @Override
    public boolean canUndo() {
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
    @Override
    public boolean canRedo() {
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
     * @return The title of tue current undoable action or {@code null} if there
     *         is no undoable action.
     */
    @Override
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
     * @return The title of tue current redoable action or {@code null} if there
     *         is no redoable action.
     */
    @Override
    public String getRedoTitle() {
        return undoManager.getRedoTitle();
    }

    /**
     * Gets the titles of all available actions that can be undone.
     *
     * @return The titles of all available actions that can be undone.
     */
    @Override
    public List<String> getUndoTitles() {
        return undoManager.getUndoTitles();
    }

    /**
     * Gets the titles of all available actions that can be redone.
     *
     * @return The titles of all available actions that can be redone.
     */
    @Override
    public List<String> getRedoTitles() {
        return undoManager.getRedoTitles();
    }

    /**
     * Removes all undoable actions.
     */
    @Override
    public void clearUndoableStack() {
        var oldUndo = undoManager.canUndo();
        var oldUndoTitle = undoManager.getUndoTitle();
        var oldRedo = undoManager.canRedo();
        var oldRedoTitle = undoManager.getRedoTitle();

        undoManager.clear();

        fireChange(CAN_UNDO_PROPERTY, null, oldUndo, undoManager.canUndo());
        fireChange(UNDO_TITLE_PROPERTY, null, oldUndoTitle, null);
        fireChange(CAN_REDO_PROPERTY, null, oldRedo, undoManager.canRedo());
        fireChange(REDO_TITLE_PROPERTY, null, oldRedoTitle, null);
    }

    /**
     * Pushes an undoable action to the stack of undoable actions.
     *
     * @param undoable The action to push. This is not executed but only pushed.
     *
     * @see #addUndoable(Undoable, boolean)
     */
    @Override
    public void addUndoable(Undoable undoable) {
        addUndoable(undoable, false);
    }

    /**
     * Pushes an undoable action to the stack of undoable actions.
     *
     * @param undoable The action to push. This is not executed but only pushed.
     * @param execute If this is {@code true}, {@link Undoable#execute()} is
     *         invoked.
     */
    @Override
    public void addUndoable(Undoable undoable, boolean execute) {
        var oldUndo = undoManager.canUndo();
        var oldUndoTitle = undoManager.getUndoTitle();
        var oldRedo = undoManager.canRedo();
        var oldRedoTitle = undoManager.getRedoTitle();

        undoManager.add(undoable);
        setModified(true);

        fireChange(CAN_UNDO_PROPERTY, null, oldUndo, undoManager.canUndo());
        fireChange(UNDO_TITLE_PROPERTY, null, oldUndoTitle, undoManager.getUndoTitle());
        fireChange(CAN_REDO_PROPERTY, null, oldRedo, undoManager.canRedo());
        fireChange(REDO_TITLE_PROPERTY, null, oldRedoTitle, undoManager.getRedoTitle());
    }

    /**
     * Undoes the latest undoable action.
     */
    @Override
    public void undo() {
        var oldUndo = undoManager.canUndo();
        var oldUndoTitle = undoManager.getUndoTitle();
        var oldRedo = undoManager.canRedo();
        var oldRedoTitle = undoManager.getRedoTitle();

        undoManager.undo();
        setModified(true);

        fireChange(CAN_UNDO_PROPERTY, null, oldUndo, undoManager.canUndo());
        fireChange(UNDO_TITLE_PROPERTY, null, oldUndoTitle, undoManager.getUndoTitle());
        fireChange(CAN_REDO_PROPERTY, null, oldRedo, undoManager.canRedo());
        fireChange(REDO_TITLE_PROPERTY, null, oldRedoTitle, undoManager.getRedoTitle());
    }

    /**
     * Redoes the latest undoable action.
     */
    @Override
    public void redo() {
        var oldUndo = undoManager.canUndo();
        var oldUndoTitle = undoManager.getUndoTitle();
        var oldRedo = undoManager.canRedo();
        var oldRedoTitle = undoManager.getRedoTitle();

        undoManager.redo();
        setModified(true);

        fireChange(CAN_UNDO_PROPERTY, null, oldUndo, undoManager.canUndo());
        fireChange(UNDO_TITLE_PROPERTY, null, oldUndoTitle, undoManager.getUndoTitle());
        fireChange(CAN_REDO_PROPERTY, null, oldRedo, undoManager.canRedo());
        fireChange(REDO_TITLE_PROPERTY, null, oldRedoTitle, undoManager.getRedoTitle());
    }

    /**
     * Creates new data.
     * <p>
     * This resets the modification and the read only flags and sets the new
     * data flag.
     */
    @Override
    public void createData() {
        doCreateData();

        setModified(false);
        setNewData(true);
        resetReadOnly();
    }

    /**
     * Loads data from a file, a database or any other source.
     * <p>
     * This resets the modification and the new data flags.
     *
     * @throws RuntimeException on any error.
     */
    @Override
    public void loadData() throws RuntimeException {
        doLoadData();

        setModified(false);
        setNewData(false);
    }

    /**
     * Writes the data into a file, a database, or any other target.
     * <p>
     * This resets the modification, the read-only and the new data flags.
     *
     * @throws RuntimeException on any error.
     */
    @Override
    public void saveData() throws RuntimeException {
        doSaveData();

        setModified(false);
        setNewData(false);
        resetReadOnly();
    }

    /**
     * Adds a listener for all properties.
     *
     * @param listener The listener to install.
     * @param originator The originator that installs the event.
     *
     * @throws NullPointerException {@code originator} or {@code listener} is
     *          {@code null}.
     */
    @Override
    public void addChangeListener(ModelChangeListener listener, Object originator) {
        pcs.addChangeListener(null, listener, originator);
    }

    /**
     * Adds a listener for the specified property.
     *
     * @param propertyName The name of the property to add the listener fo or
     *         {@code null} or an empty string to listen to all properties.
     * @param listener The listener to install.
     * @param originator The originator that installs the event.
     *
     * @throws NullPointerException {@code originator} or {@code listener} is
     *          {@code null}.
     */
    @Override
    public void addChangeListener(String propertyName, ModelChangeListener listener, Object originator) {
        pcs.addChangeListener(propertyName, listener, originator);
    }

    /**
     * Removes an installed listener.
     *
     * @param listener The listener to install.
     * @param originator The originator that installs the event.
     *
     * @throws NullPointerException {@code originator} or {@code listener} is
     *          {@code null}.
     */
    @Override
    public void removeChangeListener(ModelChangeListener listener, Object originator) {
        pcs.removeChangeListener(null, listener, originator);
    }

    /**
     * Removes an installed listener for the specified property.
     *
     * @param propertyName The name of the property to add the listener fo or
     *         {@code null} or an empty string for listeners that are installed
     *         for all properties.
     * @param listener The listener to install.
     * @param originator The originator that installs the event.
     *
     * @throws NullPointerException {@code originator} or {@code listener} is
     *          {@code null}.
     */
    @Override
    public void removeChangeListener(String propertyName, ModelChangeListener listener, Object originator) {
        pcs.removeChangeListener(propertyName, listener, originator);
    }

    /**
     * Reports a bound property update to listeners
     * that have been registered to track updates of
     * all properties or a property with the specified name.
     * <p>
     * No event is fired if old and new values are equal and non-null.
     *
     * @param propertyName  the programmatic name of the property that was changed
     * @param originator The originator that causes the event. This is not
     *         notified of the change. This should be {@code null} to notify
     *         all listeners.
     * @param oldValue      the old value of the property
     * @param newValue      the new value of the property
     */
    protected void fireChange(String propertyName, Object originator, Object oldValue, Object newValue) {
        pcs.fireChange(propertyName, originator, oldValue, newValue);
    }

    /**
     * Reports a bound indexed property update to listeners
     * that have been registered to track updates of
     * all properties or a property with the specified name.
     * <p>
     * No event is fired if old and new values are equal and non-null.
     *
     * @param propertyName  the programmatic name of the property that was changed
     * @param originator The originator that causes the event. This is not
     *         notified of the change. This should be {@code null} to notify
     *         all listeners.
     * @param index         the index of the property element that was changed
     * @param oldValue      the old value of the property
     * @param newValue      the new value of the property
     */
    protected void fireIndexedChange(String propertyName, Object originator, int index, Object oldValue, Object newValue) {
        pcs.fireIndexedChange(propertyName, originator, index, oldValue, newValue);
    }

    /**
     * Resets the read only flag.
     * <p>
     * This fires a property change event with the property name set to
     * {@link #READ_ONLY_PROPERTY}.
     */
    protected void resetReadOnly() {
        var old = isReadOnly();
        flags &= ~IS_READONLY;

        fireChange(READ_ONLY_PROPERTY, null, old, false);
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

        fireChange(NEW_DATA_PROPERTY, null, old, newData);
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
     * Invoked by {@link #createData()} to create new data.
     */
    protected abstract void doCreateData();

    /**
     * Invoked by {@link #loadData()} to load data from a storage.
     *
     * @throws RuntimeException on any error.
     */
    protected abstract void doLoadData() throws RuntimeException;

    /**
     * Invoked by {@link #saveData()} to write the data into a storage.
     *
     * @throws RuntimeException on any error.
     */
    protected abstract void doSaveData()throws RuntimeException;
}
