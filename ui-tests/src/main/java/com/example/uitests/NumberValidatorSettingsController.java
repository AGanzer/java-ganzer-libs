package com.example.uitests;

import de.ganzer.core.validation.NumberValidator;
import de.ganzer.core.validation.Validator;
import de.ganzer.fx.validation.ValidatorTextFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.text.NumberFormat;
import java.text.ParseException;

public class NumberValidatorSettingsController implements TestValidatorController {
    //region fields
    private final NumberValidator minMaxValueValidator = new NumberValidator();
    private final NumberValidator numDecimalsValidator = new NumberValidator(0, 6);
    private NumberValidator testValidator;
    //endregion

    //region controls
    public TextField minValue;
    public TextField maxValue;
    public TextField numDecimals;
    public TextField displayFormat;
    public TextField editFormat;
    //endregion

    //region getter/setter
    public Validator getTestValidator() {
        return testValidator;
    }

    public void setTestValidator(Validator testValidator) {
        this.testValidator = (NumberValidator)testValidator;

        var min = this.testValidator.getMinValue();
        var max = this.testValidator.getMaxValue();

        numDecimals.setText(String.format("%,d", this.testValidator.getNumDecimals()));
        displayFormat.setText(this.testValidator.getDisplayFormat());
        editFormat.setText(this.testValidator.getEditFormat());

        // The previous call changes the range to 0, therefore we have to reset:
        //
        this.testValidator.setRange(min, max);

        updateText(minValue, this.testValidator.getMinValue());
        updateText(maxValue, this.testValidator.getMaxValue());
    }
    //endregion

    //region init
    @FXML
    private void initialize() {
        new ValidatorTextFormatter(minMaxValueValidator, minValue);
        new ValidatorTextFormatter(minMaxValueValidator, maxValue);
        new ValidatorTextFormatter(numDecimalsValidator, numDecimals);

        initializeListeners();
    }

    @SuppressWarnings("DuplicatedCode")
    private void initializeListeners() {
        minValue.textProperty().addListener((p, o, n) -> {
            try {
                if (minValue.getText().length() == 0)
                    return;

                testValidator.setMinValue(NumberFormat.getInstance().parse(minValue.getText()).doubleValue());
                updateText(maxValue, testValidator.getMaxValue());
            } catch (ParseException e) {
                System.err.println(e.getMessage());
            }
        });
        maxValue.textProperty().addListener((p, o, n) -> {
            try {
                if (maxValue.getText().length() == 0)
                    return;

                testValidator.setMaxValue(NumberFormat.getInstance().parse(maxValue.getText()).doubleValue());
                updateText(minValue, testValidator.getMinValue());
            } catch (ParseException e) {
                System.err.println(e.getMessage());
            }
        });
        numDecimals.textProperty().addListener((p, o, n) -> {
            try {
                if (numDecimals.getText().length() == 0)
                    return;

                int numDec = NumberFormat.getInstance().parse(numDecimals.getText()).intValue();

                testValidator.setNumDecimals(numDec);
                minMaxValueValidator.setNumDecimals(numDec);

                updateText(minValue, testValidator.getMinValue());
                updateText(maxValue, testValidator.getMaxValue());
            } catch (ParseException e) {
                System.err.println(e.getMessage());
            }
        });
        displayFormat.textProperty().addListener((p, o, n) -> testValidator.setDisplayFormat(displayFormat.getText()));
        editFormat.textProperty().addListener((p, o, n) -> testValidator.setDisplayFormat(editFormat.getText()));
    }

    private void updateText(TextField field, double value) {
        try {
            var numDec = NumberFormat.getInstance().parse(numDecimals.getText()).intValue();
            var format = String.format("%%,.%df", numDec);

            field.setText(String.format(format, value));
        } catch (ParseException e) {
            System.err.println(e.getMessage());
        }
    }
    //endregion
}
