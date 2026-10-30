package de.ganzer.swing.controls;

import javax.swing.event.ChangeEvent;

/**
 * This class represents a vetoable change event.
 *
 * @see #setVetoed(boolean)
 */
public class VetoableChangeEvent extends ChangeEvent {
    private boolean vetoed;

    /**
     * Constructs a new instance.
     *
     * @param source The object that is the source of the event (typically
     *        {@code this}).
     */
    public VetoableChangeEvent(Object source) {
        super(source);
    }

    /**
     * Indicates whether the change should be dismissed.
     *
     * @return {@code true} if the change should be dismissed, {@code false}
     *         otherwise.
     */
    public boolean isVetoed() {
        return vetoed;
    }

    /**
     * Sets whether the change should be dismissed.
     *
     * @param vetoed {@code true} if the change should be dismissed,
     *        {@code false} otherwise.
     */
    public void setVetoed(boolean vetoed) {
        this.vetoed = vetoed;
    }
}
