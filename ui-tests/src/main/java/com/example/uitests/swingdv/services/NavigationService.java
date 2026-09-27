package com.example.uitests.swingdv.services;

import com.example.uitests.swingdv.SwingDVApp;
import de.ganzer.dv.DVManager;
import de.ganzer.dv.services.DVNavigationService;
import de.ganzer.swing.dlgfw.services.DFWNavigationService;

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
        return getConfirmation((Component) DVManager.getActiveView(),
                               String.format("Soll %s gespeichert werden?", name),
                               SwingDVApp.TITLE);
    }

    @Override
    public String querySaveLocation(String initialLocation, String filter) {
        return null;
    }

    @Override
    public void showError(String message, Throwable cause) {
    }

    @Override
    public Boolean getConfirmation(Component parent, String question, String title) {
        return false;
    }

    private NavigationService() {
    }
}
