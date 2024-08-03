package com.example.uitests.fx.charts;

import de.ganzer.core.validation.TextFormat;
import de.ganzer.core.validation.Validator;
import de.ganzer.core.validation.ValidatorExceptionRef;
import de.ganzer.fx.validation.ValidatorTextFormatter;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.text.NumberFormat;
import java.text.ParseException;

public class OneValueSettingsController implements DistributionSettingsController {
    private boolean changed;
    private Validator valueValidator;

    public TextField valueInput;
    public Label valueLabel;

    @Override
    public boolean isChanged() {
        return changed;
    }

    @Override
    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    @Override
    public TextField validate(ValidatorExceptionRef ref) {
        return valueValidator.validate(valueInput.getText(), ref)
                ? null
                : valueInput;
    }

    public void setValueLabelText(String text) {
        this.valueLabel.setText(text);
    }

    public void setValueValidator(Validator validator) {
        this.valueValidator = validator;
        new ValidatorTextFormatter(this.valueValidator, valueInput);
    }

    public Double getValue() {
        try {
            return NumberFormat.getInstance().parse(valueInput.getText()).doubleValue();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void setValue(Double value) {
        var text = NumberFormat.getInstance().format(value);
        valueInput.setText(valueValidator.formatText(text, valueInput.isFocused() ? TextFormat.EDIT : TextFormat.DISPLAY));
    }

    public void textChanged(KeyEvent ignored) {
        changed = true;
    }
}
