package de.ganzer.swing.dv;

import de.ganzer.dv.Document;
import de.ganzer.dv.View;

import java.util.List;

/**
 * This interface is used to provide additional functionality to the
 * {@link DVManager}.
 *
 * @since 6.0.0
 */
public interface DVManagerSupport {
    /**
     * Invoked to get the active MDI subview.
     *
     * @return The active MDI subview or {@code null} if there is no active MDI
     *         subview.
     */
    View<? extends Document> getActiveMDISubView();

    /**
     * Invoked to get a list of all open MDI subviews.
     *
     * @return All open MDI subviews or an empty list if there are no open MDI
     *         subviews.
     */
    List<View<? extends Document>> getAllMDISubViews();

    /**
     * Invoked to update the menu items in the "Window" menu with the currently
     * opened documents.
     */
    void updateOpenDocumentsMenu();
}
