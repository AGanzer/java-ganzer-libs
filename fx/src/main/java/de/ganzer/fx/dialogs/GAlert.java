package de.ganzer.fx.dialogs;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Callback;

import java.util.Optional;

/**
 * This utility class simplifies the way to show an {@link Alert} dialog with
 * expandable content and to unify the result handling with {@link GDialog}.
 */
@SuppressWarnings("unused")
public class GAlert {
    private final Alert alert;
    private StageStyle style;
    private Modality modality;
    private Callback<ButtonType, Integer> resultTranslator;

    /**
     * Creates a new instance from the specified argument.
     *
     * @param type The type of the alert box to show.
     */
    public GAlert(Alert.AlertType type) {
        alert = new Alert(type);
    }

    /**
     * Sets the title of the alert box.
     *
     * @param title The title to set.
     */
    public void setTitle(String title) {
        alert.setTitle(title);
    }

    /**
     * Sets the header text of the alert box.
     *
     * @param headerText The header text to set.
     */
    public void setHeaderText(String headerText) {
        alert.setHeaderText(headerText);
    }

    /**
     * Sets the content text of the alert box.
     *
     * @param contentText The content text to set.
     */
    public void setContentText(String contentText) {
        alert.setContentText(contentText);
    }

    /**
     * This simplifies the way to create an expandable content by creating a
     * default content with a write protected text area.
     *
     * @param text The text to set. If this is {@code null}, the expandable
     *             content is removed.
     *
     * @see #setExpandableContent(Node)
     */
    public void setExpandableContentText(String text) {
        if (text == null)
            setExpandableContent(null);
        else {
            TextArea textArea = new TextArea();
            textArea.setText(text);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            VBox vBox = new VBox();
            vBox.getChildren().add(textArea);

            setExpandableContent(vBox);
        }
    }

    /**
     * Sets the expandable content of the alert box.
     *
     * @param expandableContent The expandable content to set.
     *
     * @see #setExpandableContentText(String)
     */
    public void setExpandableContent(Node expandableContent) {
        alert.getDialogPane().setExpandableContent(expandableContent);
    }

    /**
     * Changes the style of the alert box.
     *
     * @param style The new style. If this is {@code null} (the default), the
     *              used style depends on the used method to show the dialog.
     *
     * @see #showAndWait(Window)
     */
    public void setStyle(StageStyle style) {
        this.style = style;
    }

    /**
     * Changes the modality of the alert box.
     *
     * @param modality The modality to set. If this is {@code null} (the
     *                 default), the used modality depends on the used method
     *                 to show the dialog.
     *
     * @see #showAndWait(Window)
     */
    public void setModality(Modality modality) {
        this.modality = modality;
    }

    /**
     * Sets a translator to translate the result of the alert box.
     * <p>
     * A translator is required if other buttons than the provided ones are set.
     * The buttons that are known by {@code GAlert} are the following:
     * <p>
     * <ul>
     *     <li>{@link ButtonType#OK}
     *     <li>{@link ButtonType#CANCEL}
     *     <li>{@link ButtonType#YES}
     *     <li>{@link ButtonType#NO}
     *     <li>{@link GButtonType#YES_TO_ALL}
     *     <li>{@link GButtonType#NO_TO_ALL}
     *     <li>{@link GButtonType#ABORT}
     *     <li>{@link GButtonType#RETRY}
     *     <li>{@link GButtonType#IGNORE}
     * </ul>
     * <p>
     * The translator should translate the unknown button types into an integer.
     *
     * @param resultTranslator The translator to set.
     */
    public void setResultTranslator(Callback<ButtonType, Integer> resultTranslator) {
        this.resultTranslator = resultTranslator;
    }

    /**
     * Sets the buttons of the alert box.
     *
     * @param buttons The buttons to set.
     */
    public void setButtons(ButtonType... buttons) {
        alert.getDialogPane().getButtonTypes().clear();
        alert.getDialogPane().getButtonTypes().addAll(buttons);
    }

    /**
     * This calls {@link #showAndWait(Window, double, double)} with the specified
     * parent to open the alert box centered to {@code parent}.
     *
     * @param parent The parent to set.
     */
    public int showAndWait(Window parent) {
        return showAndWait(parent, Double.MIN_VALUE, Double.MIN_VALUE);
    }

    /**
     * Shows the alert box modal.
     * <p>
     * This method does not return before the alert box is closed.
     * <p>
     * If the modality is {@code null}, {@link Modality#APPLICATION_MODAL} is
     * used.
     * <p>
     * If the style is {@code null}, {@link StageStyle#UTILITY} is used.
     *
     * @param parent The parent to set.
     * @param xPos   The X-position of the alert box' top-left corner when it is
     *               shown. If this is {@link Double#MIN_VALUE}, the alert box is
     *               horizontally centered to its parent.
     * @param yPos   The Y-position of the alert box' top-left corner when it is
     *               shown. If this is {@link Double#MIN_VALUE}, the alert box is
     *               vertically centered to its parent.
     *
     * @return The modal result of the alert box. This depends on the set buttons.
     *
     * @see #setButtons(ButtonType...)
     * @see #setModality(Modality)
     * @see #setStyle(StageStyle)
     */
    public int showAndWait(Window parent, double xPos, double yPos) {
        adjustAlertBox(
                parent,
                xPos,
                yPos,
                modality != null ? modality : Modality.APPLICATION_MODAL,
                style != null ? style : StageStyle.UTILITY);
        Optional<ButtonType> result = alert.showAndWait();

        return result.map(this::translate).orElse(0);
    }

    private int translate(ButtonType result) {
        if (result == ButtonType.CANCEL)
            return ModalResult.CANCEL;

        if (result == ButtonType.OK)
            return ModalResult.OK;

        if (result == ButtonType.YES)
            return ModalResult.YES;

        if (result == ButtonType.NO)
            return ModalResult.NO;

        if (result == GButtonType.YES_TO_ALL)
            return ModalResult.YES_TO_ALL;

        if (result == GButtonType.NO_TO_ALL)
            return ModalResult.NO_TO_ALL;

        if (result == GButtonType.RETRY)
            return ModalResult.RETRY;

        if (result == GButtonType.ABORT)
            return ModalResult.ABORT;

        if (result == GButtonType.IGNORE)
            return ModalResult.IGNORE;

        if (resultTranslator != null)
            return resultTranslator.call(result);

        return 0;
    }

    private void adjustAlertBox(Window parent, double xPos, double yPos, Modality modality, StageStyle stageStyle) {
        alert.initOwner(parent);
        alert.initModality(modality);
        alert.initStyle(stageStyle);
        alert.onShowingProperty().addListener((p, o, n) -> adjustPosition(alert, parent, xPos, yPos));
    }

    private static void adjustPosition(Alert alert, Window parent, double xPos, double yPos) {
        if (parent == null)
            return;

        double adjustedX = xPos != Double.MIN_VALUE
                ? xPos
                : parent.getX() + (parent.getWidth() - alert.getWidth()) / 2;
        double adjustedY = yPos != Double.MIN_VALUE
                ? yPos
                : parent.getY() + (parent.getHeight() - alert.getHeight()) / 2;

        alert.setX(adjustedX);
        alert.setY(adjustedY);
    }
}
