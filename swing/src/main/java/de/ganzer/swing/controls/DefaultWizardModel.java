package de.ganzer.swing.controls;

import java.awt.Component;

/**
 * A default implementation of {@link AbstractWizardModel} that selects the next
 * page in the order they were added.
 * <p>
 * This class works with pages that implement {@link DefaultWizardModelPage}.
 * <p>
 * If it is necessary to sip pages in some cases, an implementor may derive
 * from {@link AbstractWizardModel} or from this class. In the latter case,
 * {@link #addPage(DefaultWizardModelPage)} can be overloaded or overridden
 * to put own identifiers to {@link #addPage(Object, DefaultWizardModelPage)}.
 * It is then most important to override {@link #getNextPage(Object)} too,
 * to work with the correct identifiers and to return the right identifier
 * for the next page.
 *
 * @see #addPage(DefaultWizardModelPage)
 * @see #getNextPage(Object)
 *
 * @since 6.3.0
 */
public class DefaultWizardModel extends AbstractWizardModel<DefaultWizardModelPage> {
    /**
     * Adds a page to the available pages.
     * <p>
     * If the given page is the first added page, it is set as the currently
     * active page.
     *
     * @param page The page that represents the page.
     *
     * @throws NullPointerException if {@code page} is {@code null}.
     * @throws IllegalArgumentException if {@code page} is already registered.
     */
    public void addPage(DefaultWizardModelPage page) {
        addPage(getPages().size(), page);
    }

    /**
     * Gets the identifier of the next page to show after the given page.
     * <p>
     * This implementation does simply return the next Page identifier of the
     * given page in the order they were added.
     *
     * @param pageId The identifier of the page to get the next page for.
     *
     * @return The next page to show or {@code null} if no further page is
     *         available.
     */
    @Override
    public Object getNextPage(Object pageId) {
        int next = Integer.parseInt(pageId.toString()) + 1;
        return next < getPages().size() ? next : null;
    }

    /**
     * Invoked to validate the data the user input into the given page.
     *
     * @param pageId The page to validate.
     *
     * @return {@code true} if the page is valid, {@code false} otherwise.
     */
    @Override
    public boolean validatePage(Object pageId) {
        return getPage(pageId).validateInput();
    }

    /**
     * Invoked when the given page is going to be left.
     * <p>
     * Implementors should update the wizard's data with the user's input.
     *
     * @param pageId The page to leave.
     */
    @Override
    public void pageLeaving(Object pageId) {
        getPage(pageId).updateWizardData();
    }

    /**
     * Invoked when the given page is entered.
     * <p>
     * Implementors should update the page's controls with the wizard's data.
     *
     * @param pageId The page to enter.
     */
    @Override
    public void pageEntered(Object pageId) {
        getPage(pageId).updatePageControls();
    }

    /**
     * Returns the Swing component for the given page.
     *
     * @param pageId The identifier of the page where to get the component for.
     *
     * @return The Swing component for the given page. This should not return
     *         {@code null}.
     */
    @Override
    public Component getComponent(Object pageId) {
        return getPage(pageId).getComponent();
    }
}
