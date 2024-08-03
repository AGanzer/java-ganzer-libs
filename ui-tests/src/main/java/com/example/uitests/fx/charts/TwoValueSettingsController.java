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

public class TwoValueSettingsController implements DistributionSettingsController {
    private boolean changed;
    private Validator value1Validator;
    private Validator value2Validator;

    public TextField value1Input;
    public TextField value2Input;
    public Label value1Label;
    public Label value2Label;

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
        if (!value1Validator.validate(value1Input.getText(), ref))
            return value1Input;

        if (!value2Validator.validate(value2Input.getText(), ref))
            return value2Input;

        return null;
    }

    public void setValue1LabelText(String text) {
        this.value1Label.setText(text);
    }

    public void setValue2LabelText(String text) {
        this.value2Label.setText(text);
    }

    public void setValue1Validator(Validator validator) {
        this.value1Validator = validator;
        new ValidatorTextFormatter(this.value1Validator, value1Input);
    }

    public void setValue2Validator(Validator validator) {
        this.value2Validator = validator;
        new ValidatorTextFormatter(this.value2Validator, value2Input);
    }

    public Double getValue1() {
        try {
            return NumberFormat.getInstance().parse(value1Input.getText()).doubleValue();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void setValue1(Double value) {
        var text = NumberFormat.getInstance().format(value);
        value1Input.setText(value1Validator.formatText(text, value1Input.isFocused() ? TextFormat.EDIT : TextFormat.DISPLAY));
    }

    public Double getValue2() {
        try {
            return NumberFormat.getInstance().parse(value2Input.getText()).doubleValue();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void setValue2(Double value) {
        var text = NumberFormat.getInstance().format(value);
        value2Input.setText(value2Validator.formatText(text, value2Input.isFocused() ? TextFormat.EDIT : TextFormat.DISPLAY));
    }

    public void textChanged(KeyEvent ignored) {
        changed = true;
    }
}
