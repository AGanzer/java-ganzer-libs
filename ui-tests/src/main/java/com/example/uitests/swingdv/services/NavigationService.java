package com.example.uitests.swingdv.services;

import com.example.uitests.swingdv.SwingDVApp;
import com.example.uitests.swingdv.dialogs.AboutDialog;
import com.example.uitests.swingdv.dialogs.ChooseFromListData;
import com.example.uitests.swingdv.dialogs.ChooseFromListDialog;
import de.ganzer.dv.swing.DVManager;
import de.ganzer.dv.DocumentTemplate;
import de.ganzer.dv.services.DVNavigationService;
import de.ganzer.swing.dlgfw.services.DFWNavigationService;

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

public class NavigationService implements DVNavigationService, DFWNavigationService {
    private static NavigationService instance;

    public static NavigationService getInstance() {
        if (instance == null)
            instance = new NavigationService();

        return instance;
    }

    @Override
    public Collection<String> queryLocationsToOpen(List<String> filters, String initialFilter) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(true);
        chooser.setDialogTitle(SwingDVApp.TITLE);
        setFilters(chooser, filters);

        return chooser.showOpenDialog(SwingDVApp.getMainWindow()) == JFileChooser.APPROVE_OPTION
                ? Arrays.stream(chooser.getSelectedFiles()).map(File::getAbsolutePath).toList()
                : null;
    }

    @Override
    public Boolean querySave(String name) {
        return getConfirmation(null, String.format("\"%s\" has changed.\n\nSave it now?", name), null);
    }

    @Override
    public String querySaveLocation(String initialLocation, String filter) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        chooser.setDialogTitle(SwingDVApp.TITLE);
        chooser.setSelectedFile(new File(initialLocation));
        setFilters(chooser, Collections.singletonList(filter));

        return chooser.showSaveDialog(SwingDVApp.getMainWindow()) == JFileChooser.APPROVE_OPTION
                ? chooser.getSelectedFile().getAbsolutePath()
                : null;
    }

    @Override
    public void showError(String message, Throwable cause) {
        JOptionPane.showMessageDialog(getParent(null), message, SwingDVApp.TITLE, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Invoked to choose a document template.
     *
     * @param templates The available templates to choose from.
     *
     * @return The chosen document template or {@code null} if the user has
     *         canceled.
     */
    @Override
    public DocumentTemplate<?> chooseDocumentTemplate(List<DocumentTemplate<?>> templates) {
        var data = new ChooseFromListData<>("Coose Template", templates);
        var dialog = new ChooseFromListDialog<>(SwingDVApp.getMainWindow(), data);
        dialog.setVisible(true);

        return dialog.isAccepted() ? data.chosen : null;
    }

    @Override
    public Boolean getConfirmation(Component parent, String question, String title) {
        return switch (JOptionPane.showConfirmDialog(getParent(parent),
                                                     question,
                                                     title != null ? title : SwingDVApp.TITLE,
                                                     JOptionPane.YES_NO_CANCEL_OPTION)) {
            case JOptionPane.YES_OPTION -> true;
            case JOptionPane.NO_OPTION -> false;
            default -> null;
        };
    }

    public void showAboutInfo() {
        var dialog = new AboutDialog(SwingDVApp.getMainWindow());
        dialog.setVisible(true);
    }

    private NavigationService() {
    }

    private Component getParent(Component parent) {
        if (parent != null)
            return parent;

        parent = (Component) DVManager.getActiveView();

        if (parent == null)
            parent = FocusManager.getCurrentManager().getPermanentFocusOwner();

        return parent;
    }

    private void setFilters(JFileChooser chooser, List<String> filters) {
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
}
