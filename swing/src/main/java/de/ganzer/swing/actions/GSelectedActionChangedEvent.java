package de.ganzer.swing.actions;

/**
 * This event ist used by {@link GToggleActionGroup} instances to notify that
 * the selected action has changed.
 */
public class GSelectedActionChangedEvent {
    private final GToggleActionGroup source;
    private final GAction selectedAction;

    /**
     * Creates a new event from the specified arguments.
     *
     * @param source The sending {@link GToggleActionGroup} of the event.
     * @param selectedAction The selected {@link GAction}. This may be
     *        {@code null}.
     */
    public GSelectedActionChangedEvent(GToggleActionGroup source, GAction selectedAction) {
        this.source = source;
        this.selectedAction = selectedAction;
    }

    /**
     * Gets the sending toggle group.
     *
     * @return The sending toggle group.
     */
    public GToggleActionGroup getSource() {
        return source;
    }

    /**
     * Gets the selected action.
     *
     * @return The selected action or {@code null} if no action is selected.
     */
    public GAction getSelectedAction() {
        return selectedAction;
    }
}
