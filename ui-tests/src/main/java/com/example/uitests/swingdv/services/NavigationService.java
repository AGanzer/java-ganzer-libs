package com.example.uitests.swingdv.services;

import com.example.uitests.swingdv.SwingDVApp;
import com.example.uitests.swingdv.dialogs.AboutDialog;
import com.example.uitests.swingdv.dialogs.ChooseFromListData;
import com.example.uitests.swingdv.dialogs.ChooseFromListDialog;
import de.ganzer.dv.DocumentTemplate;
import de.ganzer.dv.ViewTemplate;
import de.ganzer.swing.dlgfw.services.DFWNavigationService;
import de.ganzer.swing.dv.services.AbstractDVNavigationService;

import javax.swing.JOptionPane;
import java.awt.Component;
import java.util.List;

public class NavigationService extends AbstractDVNavigationService implements DFWNavigationService {
    private static NavigationService instance;

    public static NavigationService getInstance() {
        if (instance == null)
            instance = new NavigationService();

        return instance;
    }

    @Override
    public DocumentTemplate<?> chooseDocumentTemplate(List<DocumentTemplate<?>> templates) {
        var data = new ChooseFromListData<>("Choose Document Type", templates);
        var dialog = new ChooseFromListDialog<>(SwingDVApp.getMainWindow(), data);
        dialog.setVisible(true);

        return dialog.isAccepted() ? data.chosen : null;
    }

    @Override
    public ViewTemplate<?, ?> chooseViewTemplate(List<ViewTemplate<?, ?>> templates) {
        var data = new ChooseFromListData<>("Choose View Type", templates);
        var dialog = new ChooseFromListDialog<>(SwingDVApp.getMainWindow(), data);
        dialog.setVisible(true);

        return dialog.isAccepted() ? data.chosen : null;
    }

    @Override
    public Boolean getConfirmation(Component parent, String question, String title) {
        return switch (JOptionPane.showConfirmDialog(findParentComponent(parent),
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

    @Override
    protected String getDialogTitle() {
        return SwingDVApp.TITLE;
    }

    private NavigationService() {
    }
}
