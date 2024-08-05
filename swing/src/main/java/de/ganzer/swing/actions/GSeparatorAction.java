package de.ganzer.swing.actions;

import javax.swing.*;
import java.util.Objects;

/**
 * Provides an action that creates as a separator.
 */
public class GSeparatorAction implements GActionItemBuilder {
    /**
     * This creates and inserts a single separator into the specified target.
     *
     * @param target The menu where to insert the separator.
     *
     * @throws NullPointerException {@code target} is {@code null}.
     */
    @Override
    public void addMenuItems(JMenu target) {
        Objects.requireNonNull(target, "target must not be null.");
        target.addSeparator();
    }

    /**
     * This creates and inserts a single separator into the specified target.
     *
     * @param target The toolbar where to insert the separator.
     * @param options This is ignored here.
     *
     * @throws NullPointerException {@code target} is {@code null}.
     */
    @Override
    public void addButtons(JToolBar target, int options) {
        Objects.requireNonNull(target, "target must not be null.");
        target.addSeparator();
    }
}
