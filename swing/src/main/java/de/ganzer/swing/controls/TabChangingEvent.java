package de.ganzer.swing.controls;

/**
 * Event that is fired when the selected tab is about to be changed.
 * <p>
 * The change can be omitted by invoking {@code #setVetoed(true)}.
 *
 * @see VetoableChangeEvent
 */
public class TabChangingEvent extends VetoableChangeEvent{
    private final int oldIndex;
    private final int newIndex;

    /**
     * Constructs a new instance.
     *
     * @param source The object that is the source of the event (typically
     *         {@code this}).
     * @param oldIndex The currently selected index or -1 if no tab is currently
     *         selected.
     * @param newIndex The index of the tab that is about to be selected or -1
     *         if no tab is about to be selected.
     */
    public TabChangingEvent(Object source, int oldIndex, int newIndex) {
        super(source);
        this.oldIndex = oldIndex;
        this.newIndex = newIndex;
    }

    /**
     * Gets the currently selected index or -1 if no tab is currently selected.
     *
     * @return The currently selected index or -1 if no tab is currently selected.
     */
    public int getOldIndex() {
        return oldIndex;
    }

    /**
     * Gets the index of the tab that is about to be selected or -1 if no tab is
     * about to be selected.
     *
     * @return The index of the tab that is about to be selected or -1 if no tab
     *         is about to be selected.
     */
    public int getNewIndex() {
        return newIndex;
    }
}
