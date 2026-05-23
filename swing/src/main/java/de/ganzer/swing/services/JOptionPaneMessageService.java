package de.ganzer.swing.services;

import de.ganzer.core.services.MessageService;

import javax.swing.FocusManager;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Frame;
import java.util.concurrent.FutureTask;

/**
 * The service implementation that uses a {@link JOptionPane} to display the
 * messages.
 */
public class JOptionPaneMessageService implements MessageService {
    /**
     * Shows a message box from the specified arguments.
     *
     * @param parent The parent that owns the message box or {@code null} to use
     *        the currently focused window. The box is displayed relatively to
     *        this parent.
     * @param title The title of the message box or {@code null} to display a
     *         default or no title.
     * @param message The message to display.
     * @param type The type of the icon to display.
     * @param buttons The buttons to display.
     * @param initialButton The initially selected button or {@code null} to
     *         select the first button initially.
     *
     * @return The button the user has clicked to close the message box or
     *         {@code null} if the message box is closed without a button.
     */
    @Override
    public String show(Object parent, String title, String message, MessageService.Type type, String[] buttons, String initialButton) {
        // This may be called from another thread:
        //
        if (!SwingUtilities.isEventDispatchThread()) {
            FutureTask<String> task = new FutureTask<>(() -> show(parent, title, message, type, buttons, initialButton));

            try {
                SwingUtilities.invokeAndWait(task);
                return task.get();
            } catch (InterruptedException e) {
                // Ignore.
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }

            return null;
        }

        int res = JOptionPane.showOptionDialog(getAvailableParent((Component) parent),
                                               message,
                                               title,
                                               JOptionPane.DEFAULT_OPTION,
                                               translateType(type),
                                               null,
                                               buttons,
                                               initialButton != null ? initialButton : buttons[0]);
        return translateResult(res, buttons);
    }

    private Component getAvailableParent(Component parent) {
        if (parent != null) {
            if (!(parent instanceof Frame))
                parent = SwingUtilities.getWindowAncestor(parent);

            if (parent != null && (!parent.isVisible() || parent != FocusManager.getCurrentManager().getFocusedWindow()))
                parent = null;
        }

        return parent != null ? parent : FocusManager.getCurrentManager().getFocusedWindow();
    }

    private int translateType(Type type) {
        return switch (type) {
            case ERROR -> JOptionPane.ERROR_MESSAGE;
            case WARNING -> JOptionPane.WARNING_MESSAGE;
            case INFORMATION -> JOptionPane.INFORMATION_MESSAGE;
            case CONFIRMATION -> JOptionPane.QUESTION_MESSAGE;
            default -> JOptionPane.PLAIN_MESSAGE;
        };
    }

    private String translateResult(int result, String[] buttons) {
        if (result == JOptionPane.CLOSED_OPTION)
            return null;

        return buttons[result];
    }
}
