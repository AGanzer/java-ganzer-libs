package de.ganzer.fx.validation;

import javafx.application.Platform;
import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

class ValidationChangeFilter implements UnaryOperator<TextFormatter.Change> {
    private ValidationTextFormatter formatter;

    public void setFormatter(ValidationTextFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public TextFormatter.Change apply(TextFormatter.Change change) {
        var current = change.getControlText();
        var input = change.getControlNewText();

        if (input.equals(current))
            return change;

        var text = new StringBuilder(input);

        if (formatter.isLiveValidation())
            formatter.validate(ValidationBehavior.SET_VISUAL_HINTS);

        if (!formatter.getValidator().isValidInput(text, true))
            return null;

        if (!text.toString().equals(input)) {
            formatter.getControl().setText(text.toString());

            Platform.runLater(() -> {
                var pos = formatter.getControl().getText().length();
                formatter.getControl().selectRange(pos, pos);
            });

            return null;
        }

        return change;
    }
}
