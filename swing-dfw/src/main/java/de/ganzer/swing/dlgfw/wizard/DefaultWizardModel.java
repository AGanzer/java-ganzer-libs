package de.ganzer.swing.dlgfw.wizard;

import java.util.*;

/**
 * The model that is used by default by an instance of {@link WizardPane}.
 *
 * @param <Data> The type of the data that is passed between the pages.
 */
public class DefaultWizardModel<Data> extends AbstractWizardModel<Data> {
    private final List<WizardPage<Data>> wizardPages = new ArrayList<>();

    /**
     * Adds a page to the available pages.
     *
     * @param page The page to add. If this is the first added page, it is set
     *         as the currently active page.
     *
     * @throws NullPointerException if {@code page} is {@code null}.
     * @throws IllegalArgumentException if {@code page} is already registered.
     */
    @Override
    public void addPage(WizardPage<Data> page) {
        super.addPage(page);
        wizardPages.add(page);
    }

    /**
     * Gets the next page to show after the current page.
     * <p>
     * This implementation gets the page that was added after the currently
     * active page.
     *
     * @param data The data to work with.
     *
     * @return The next page to show or {@code null} if no further page is
     *         available.
     */
    @Override
    public WizardPage<Data> getNextPage(Data data) {
        var pageIndex = wizardPages.indexOf(getCurrentPage());
        return pageIndex < wizardPages.size() - 1 ? wizardPages.get(pageIndex + 1) : null;
    }
}
