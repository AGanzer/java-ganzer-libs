package de.ganzer.core.dv;

import java.util.Collections;
import java.util.List;

/**
 * The basic interface to an object that represents a model.
 * <p>
 * A model in the sense of this framework should only represent one or more
 * entities of data but should not act as an entity itself.
 *
 * @since 5.6.0
 */
public interface Model {
    /**
     * The name of the "newData" property used for {@link ModelChangeEvent}'s.
     */
    String NEW_DATA_PROPERTY = "newData";
    /**
     * The name of the "readOnly" property used for {@link ModelChangeEvent}'s.
     */
    String READ_ONLY_PROPERTY = "readOnly";
    /**
     * The name of the "canUndo" property used for {@link ModelChangeEvent}'s.
     */
    String CAN_UNDO_PROPERTY = "canUndo";
    /**
     * The name of the "canRedo" property used for {@link ModelChangeEvent}'s.
     */
    String CAN_REDO_PROPERTY = "canRedo";
    /**
     * The name of the "undoTitle" property used for {@link ModelChangeEvent}'s.
     */
    String UNDO_TITLE_PROPERTY = "undoTitle";
    /**
     * The name of the "redoTitle" property used for {@link ModelChangeEvent}'s.
     */
    String REDO_TITLE_PROPERTY = "redoTitle";
    /**
     * The name of the "name" property used for {@link ModelChangeEvent}'s.
     */
    String NAME_PROPERTY = "name";
    /**
     * The name of the "modified" property used for {@link ModelChangeEvent}'s.
     */
    String MODIFIED_PROPERTY = "modified";

    /**
     * Indicates whether this data is read from a persistent memory or if it is
     * created newly.
     * <p>
     * If this is changed, implementors should fire a property change event with
     * the property name set to {@link #NEW_DATA_PROPERTY}.
     *
     * @return {@code true} if the data is newly created.
     */
    boolean isNewData();

    /**
     * Indicates whether the data of the model can be modified.
     * <p>
     * Implementors should not allow any modification if this is {@code true}.
     * <p>
     * If this is changed, implementors should fire a property change event with
     * the property name set to {@link #READ_ONLY_PROPERTY}.
     *
     * @return {@code false} if the data can be modified.
     */
    boolean isReadOnly();

    /**
     * The name of the model.
     * <p>
     * The meaning of the name is implementation defined, but every model should
     * have a name that can be displayed in the title of a view.
     *
     * @return The name of the model or {@code null} if no name is available.
     *
     * @see #setName(String)
     */
    String getName();

    /**
     * Sets the name of the model.
     * <p>
     * The meaning of the name is implementation defined, but every model should
     * have a name that can be displayed in the title of a view.
     * <p>
     * Changing the name should not affect the modification flag, because it
     * is usually not part of the data model or the encapsulated data entities
     * itself.
     * <p>
     * Implementors should fire a {@link ModelChangeEvent} with the property
     * name set to {@link #NAME_PROPERTY} if this is changed.
     *
     * @param name The name to set.
     */
    void setName(String name);

    /**
     * indicates whether the model is modified.
     *
     * @return {@code true} if the model's data is modified.
     */
    boolean isModified();

    /**
     * Sets or unsets the modification flag of the model.
     * <p>
     * Implementors should fire a {@link ModelChangeEvent} with the property
     * name set to {@link #MODIFIED_PROPERTY} if this is changed.
     *
     * @param modified {@code true} to indicate the model as modified.
     */
    void setModified(boolean modified);

    /**
     * Indicates whether an action that can be undone is available.
     * <p>
     * This default implementation is provided because not every model provides
     * undoable changes. This implementation does always return {@code false}.
     * <p>
     * If this is changed, implementors should fire a {@link ModelChangeEvent}
     * with the property name set to {@link #CAN_UNDO_PROPERTY}.
     *
     * @return {@code true} if an action can be undone.
     */
    default boolean canUndo() {
        return false;
    }

    /**
     * Indicates whether an action that can be redone is available.
     * <p>
     * This default implementation is provided because not every model provides
     * undoable changes. This implementation does always return {@code false}.
     * <p>
     * If this is changed, implementors should fire a property change event with
     * the property name set to {@link #CAN_REDO_PROPERTY}.
     *
     * @return {@code true} if an action can be redone.
     */
    default boolean canRedo() {
        return false;
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
     *          is no undoable action.
     */
    default String getUndoTitle() {
        return null;
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
     *          is no redoable action.
     */
    default String getRedoTitle() {
        return null;
    }

    /**
     * Gets the titles of all available actions that can be undone.
     * <p>
     * This default implementation is provided because not every model provides
     * undoable changes. This implementation does always return an empty
     * collection.
     *
     * @return The titles of all available actions that can be undone.
     */
    default List<String> getUndoTitles() {
        return Collections.emptyList();
    }

    /**
     * Gets the titles of all available actions that can be redone.
     * <p>
     * This default implementation is provided because not every model provides
     * undoable changes. This implementation does always return an empty
     * collection.
     *
     * @return The titles of all available actions that can be redone.
     */
    default List<String> getRedoTitles() {
        return Collections.emptyList();
    }

    /**
     * Removes all undoable actions.
     * <p>
     * This default implementation is provided because not every model provides
     * undoable changes. This implementation does nothing.
     */
    default void clearUndoableStack() {
    }

    /**
     * Pushes an undoable action to the stack of undoable actions.
     * <p>
     * This default implementation is provided because not every model provides
     * undoable changes. This implementation does nothing.
     * <p>
     * Implementors should set the modification flag if this is invoked.
     *
     * @param undoable The action to push. This is not executed but only pushed.
     *
     * @see #addUndoable(Undoable, boolean)
     */
    default void addUndoable(Undoable undoable) {
    }

    /**
     * Pushes an undoable action to the stack of undoable actions.
     * <p>
     * This default implementation is provided because not every model provides
     * undoable changes. This implementation does nothing.
     * <p>
     * Implementors should set the modification flag if this is invoked.
     *
     * @param undoable The action to push. This is not executed but only pushed.
     * @param execute If this is {@code true}, {@link Undoable#execute()} is
     *         invoked.
     */
    default void addUndoable(Undoable undoable, boolean execute) {
    }

    /**
     * Undoes the latest undoable action.
     * <p>
     * This default implementation is provided because not every model provides
     * undoable changes. This implementation does nothing.
     * <p>
     * Implementors should set the modification flag if this is invoked.
     */
    default void undo() {
    }

    /**
     * Redoes the latest undoable action.
     * <p>
     * This default implementation is provided because not every model provides
     * undoable changes. This implementation does nothing.
     * <p>
     * Implementors should set the modification flag if this is invoked.
     */
    default void redo() {
    }

    /**
     * Creates new data.
     * <p>
     * Implementors should reset the modification flag and return {@code true}
     * in {@link #isNewData()} and {@code false} in {@link #isReadOnly()} if
     * this is invoked.
     */
    void createData();

    /**
     * Loads data from a file, a database or any other source.
     * <p>
     * Implementors should reset the modification flag and return {@code false}
     * in {@link #isNewData()} if this is invoked.
     *
     * @throws RuntimeException on any error.
     */
    void loadData() throws RuntimeException;

    /**
     * Writes the data into a file, a database or any other target.
     * <p>
     * Implementors should reset the modification flag and return {@code false}
     * in {@link #isNewData()} and in {@link #isReadOnly()} if this is invoked.
     *
     * @throws RuntimeException on any error.
     */
    void saveData() throws RuntimeException;

    /**
     * Adds a listener for all properties.
     *
     * @param listener The listener to install.
     * @param originator The originator that installs the event.
     *
     * @throws NullPointerException {@code originator} or {@code listener} is
     *          {@code null}.
     */
    void addChangeListener(ModelChangeListener listener, Object originator);

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
    void addChangeListener(String propertyName, ModelChangeListener listener, Object originator) ;

    /**
     * Removes an installed listener.
     *
     * @param listener The listener to install.
     * @param originator The originator that has installed the event.
     *
     * @throws NullPointerException {@code originator} or {@code listener} is
     *          {@code null}.
     */
    void removeChangeListener(ModelChangeListener listener, Object originator);

    /**
     * Removes an installed listener for the specified property.
     *
     * @param propertyName The name of the property to add the listener fo or
     *         {@code null} or an empty string for listeners that are installed
     *         for all properties.
     * @param listener The listener to install.
     * @param originator The originator that has installed the event.
     *
     * @throws NullPointerException {@code originator} or {@code listener} is
     *          {@code null}.
     */
    void removeChangeListener(String propertyName, ModelChangeListener listener, Object originator);
}
