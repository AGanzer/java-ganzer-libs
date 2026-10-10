package de.ganzer.swing.dv.services;

import de.ganzer.dv.services.DVNavigationService;
import de.ganzer.swing.dv.DVManager;

import javax.swing.FocusManager;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Component;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Abstract implementation of the {@link DVNavigationService} interface that
 * provides basic dialogs.
 */
public abstract class AbstractDVNavigationService implements DVNavigationService {
    private boolean rememberLastOpenDir;
    private File lastOpenDir;

    /**
     * Creates a new instance.
     *
     * @param rememberLastOpenDir Indicates whether the last open directory
     *        should be remembered.
     */
    protected AbstractDVNavigationService(boolean rememberLastOpenDir) {
        this.rememberLastOpenDir = rememberLastOpenDir;
    }

    /**
     * Indicates whether the last open directory should be remembered.
     *
     * @return {@code true} if the last open directory should be remembered,
     *          {@code false} otherwise.
     *
     * @see #AbstractDVNavigationService(boolean)
     */
    public boolean shouldRememberLastOpenDir() {
        return rememberLastOpenDir;
    }

    /**
     * Sets whether the last open directory should be remembered.
     *
     * @param rememberLastOpenDir {@code true} to remember the last open
     *         directory, {@code false} otherwise.
     */
    public void setRememberLastOpenDir(boolean rememberLastOpenDir) {
        this.rememberLastOpenDir = rememberLastOpenDir;
    }

    /**
     * Invoked to get one or more locations that shall be opened as documents.
     * <p>
     * This implementation opens a {@link JFileChooser} dialog and uses
     * {@link #setFilters(JFileChooser, List)} to parse and set the filters.
     * The initial directory is set to the last open directory if available
     * and if {@link #shouldRememberLastOpenDir()} returns {@code true}.
     *
     * @param filters The filters to filter the possible results or {@code null}
     *         if no filter is provided.
     * @param initialFilter The initial filter to set or {@code null} if no
     *         initial filter is provided.
     *
     * @return The list of locations to open or {@code null} or an empty list
     *          if the user has canceled.
     */
    @Override
    public Collection<String> queryLocationsToOpen(List<String> filters, String initialFilter) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(true);
        chooser.setDialogTitle(getDialogTitle());
        chooser.setCurrentDirectory(lastOpenDir);
        setFilters(chooser, filters);

        var files = chooser.showOpenDialog(findParentComponent(null)) == JFileChooser.APPROVE_OPTION
                ? Arrays.stream(chooser.getSelectedFiles()).map(File::getAbsolutePath).toList()
                : null;

        if (files != null)
            lastOpenDir = chooser.getCurrentDirectory();

        return files;
    }

    /**
     * Invoked to query the user whether the document with the given name should
     * be saved.
     * <p>
     * This implementation opens a {@link JOptionPane} confirmation dialog.
     *
     * @param name The name of the document that contains unsaved modified data.
     *
     * @return {@code true} if the document can be saved, {@code false} not to
     *          save and {@code null} if the user wants to cancel to ongoing
     *          operation.
     */
    @Override
    public Boolean querySave(String name) {
        return switch (JOptionPane.showConfirmDialog(findParentComponent(null),
                                                     String.format("\"%s\" has changed.\n\nSave it now?", name),
                                                     getDialogTitle(),
                                                     JOptionPane.YES_NO_CANCEL_OPTION)) {
            case JOptionPane.YES_OPTION -> true;
            case JOptionPane.NO_OPTION -> false;
            default -> null;
        };
    }

    /**
     * Invoked to query a location where to save new data.
     * <p>
     * This implementation opens a {@link JFileChooser} dialog and uses
     * {@link #setFilters(JFileChooser, List)} to parse and set the filter.
     *
     * @param initialLocation The initial location to show in a dialog.
     * @param filter the filter to filer to possible results or {@code null}
     *         if no filter is provided.
     *
     * @return The chosen location or {@code null} if the user has canceled.
     */
    @Override
    public String querySaveLocation(String initialLocation, String filter) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        chooser.setDialogTitle(getDialogTitle());
        chooser.setSelectedFile(new File(initialLocation));
        setFilters(chooser, Collections.singletonList(filter));

        return chooser.showSaveDialog(findParentComponent(null)) == JFileChooser.APPROVE_OPTION
                ? chooser.getSelectedFile().getAbsolutePath()
                : null;
    }

    /**
     * Displays an error message to the user.
     * <p>
     * This implementation opens a {@link JOptionPane} message dialog.
     *
     * @param message The error message to display.
     * @param cause The exception that caused the error or {@code null} if no
     *        exception is available.
     */
    @Override
    public void showError(String message, Throwable cause) {
        JOptionPane.showMessageDialog(findParentComponent(null), message, getDialogTitle(), JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Searches for a component that can be used as a parent to show dialogs.
     * <p>
     * This implementation return {@code parent} if it is not {@code null}.
     * Otherwise, it tries to find the currently focused window. If there is
     * none, it tries to find the active view. If there is currently no
     * active view, it tries to find the permanent focus owner.
     *
     * @param parent The original parent component.
     *
     * @return The parent component.
     */
    protected Component findParentComponent(Component parent) {
        if (parent != null)
            return parent;

        parent = FocusManager.getCurrentManager().getFocusedWindow();

        if (parent == null)
            parent = (Component) DVManager.getActiveView();

        if (parent == null)
            parent = FocusManager.getCurrentManager().getPermanentFocusOwner();

        return parent;
    }

    /**
     * Sets the filters of the given file chooser.
     * <p>
     * A simple filter description may be like "CSV Files|*.csv" or like
     * "Source Files|*.c *.cpp *.h".
     * <p>
     * This implementation does even exclude filters from more complex ones like
     * "Text Files|*.txt;Log Files|*.log;Source Files|*.c *.cpp *.h *.java *.py"
     * and fills the file chooser with the extracted filters.
     *
     * @param chooser The file chooser where the filters should be set.
     * @param filters The filters to set or {@code null} if no filter is provided.
     */
    protected void setFilters(JFileChooser chooser, List<String> filters) {
        chooser.setAcceptAllFileFilterUsed(false);

        for (var flt : filters) {
            for (var filter : flt.split("[,;]")) {
                var parts = filter.split("\\|");

                chooser.addChoosableFileFilter(new FileNameExtensionFilter(
                        String.format("%s (%s)", parts[0], parts[1]),
                        Arrays.stream(parts[1].split(" ")).map(e -> e.substring(2)).toArray(String[]::new)));
            }
        }

        chooser.setAcceptAllFileFilterUsed(true);
    }

    /**
     * Invoked to get the title to show in the opened dialogs.
     *
     * @return The title to show in the opened dialogs.
     */
    protected abstract String getDialogTitle();
}
