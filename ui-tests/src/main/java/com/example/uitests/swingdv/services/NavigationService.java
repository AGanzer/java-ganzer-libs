package com.example.uitests.swingdv.services;

import de.ganzer.dv.services.DVNavigationService;

import java.util.Collection;
import java.util.List;

public class NavigationService implements DVNavigationService {
    @Override
    public Collection<String> queryLocationsToOpen(List<String> filters, String initialFilter) {
        return List.of();
    }

    @Override
    public Boolean querySave(String name) {
        return false;
    }

    @Override
    public String querySaveLocation(String initialLocation, String filter) {
        return null;
    }

    @Override
    public void showError(String message, Throwable cause) {
    }
}
