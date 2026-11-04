package de.ganzer.swing.controls;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * A default implementation of {@link AbstractWizardModel} that selects the next
 * page in the order they were added.
 *
 * @see #getNextPage()
 *
 * @since 6.3.0
 */
public class DefaultWizardModel extends AbstractWizardModel {
    private final List<Object> wizardPages = new ArrayList<>();

    /**
     * Adds a page to the available pages.
     *
     * @param page The identifier of the page to add. If this is the first added
     *        page, it is set as the currently active page.
     * @param component The component that represents the page.
     *
     * @throws NullPointerException if {@code page} or {@code component} is
     *         {@code null}.
     * @throws IllegalArgumentException if {@code page} is already registered.
     */
    @Override
    public void addPage(Object page, Component component) {
        super.addPage(page, component);
        wizardPages.add(page);
    }

    /**
     * Gets the identifier of the next page to show after the current page.
     * <p>
     * This implementation does simply return the next Page identifier of the
     * current page in the order they were added.
     *
     * @return The next page to show or {@code null} if no further page is
     *         available If {@link #getCurrentPage()} returns {@code null},
     *         the first available page is returned.
     */
    @Override
    public Object getNextPage() {
        for (int i = 0; i < wizardPages.size(); i++) {
            if (wizardPages.get(i).equals(getCurrentPage())) {
                return i + 1 < wizardPages.size() ? wizardPages.get(i + 1) : null;
            }
        }

        return null;
    }
}
