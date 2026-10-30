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
     * Invoked to get the title of the application that is displayed in the
     * main window.
     *
     * @return The title of the application.
     */
    String getAppTitle();

    /**
     * Invoked to get the document's name the given view relates to, to set in
     * the main window's title.
     *
     * @param view The view where to get the title from.
     *
     * @return The title of the view to use in the main window's title. This
     *         implementation does return {@code view.getTitle()}.
     */
    default String getViewTitleForMainWindow(View<?> view) {
        return view.getTitle();
    }

    /**
     * Invoked to set the title of the main window.
     *
     * @param tile The title of the main window.
     */
    void setMainWindowTitle(String tile);

    /**
     * Invoked to get the format string to format the title of the main window.
     * <p>
     * The format string may contain up to 2 placeholders. The first is
     * replaced with the currently active document (detected by
     * {@link #getActiveMDISubView()}), the second with the application's
     * title received by {@link #getAppTitle()}.
     *
     * @return The format string to use. This implementation does always return
     *         "%1$s - %2$s".
     */
    default String getMainWindowTitleFormat() {
        return "%1$s - %2$s";
    }

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
     * available actions in {@link DVManager#openDocumentsActions}.
     */
    void updateOpenDocumentsMenu();

    /**
     * Invoked to update the menu items in the recent files menu with the
     * currently available actions in {@link DVManager#recentFilesActions}.
     */
    void updateRecentFilesMenu();
}
