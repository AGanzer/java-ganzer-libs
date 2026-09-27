package com.example.uitests.swingdv.services;

import com.example.uitests.swingdv.SwingDVApp;
import de.ganzer.dv.DVManager;
import de.ganzer.dv.DocumentTemplate;
import de.ganzer.dv.services.DVNavigationService;
import de.ganzer.swing.dlgfw.services.DFWNavigationService;

import javax.swing.FocusManager;
import javax.swing.JOptionPane;
import java.awt.Component;
import java.util.Collection;
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
        return List.of();
    }

    @Override
    public Boolean querySave(String name) {
        return getConfirmation(null, String.format("Soll %s gespeichert werden?", name), null);
    }

    @Override
    public String querySaveLocation(String initialLocation, String filter) {
        return null;
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
        return templates.stream().filter(DocumentTemplate::isDefault).findFirst().orElse(templates.get(0));
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
}
