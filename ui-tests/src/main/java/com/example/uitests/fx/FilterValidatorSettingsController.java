package com.example.uitests.fx;

import de.ganzer.core.validation.FilterValidator;
import de.ganzer.core.validation.NumberValidator;
import de.ganzer.core.validation.Validator;
import de.ganzer.core.validation.ValidatorExceptionRef;
import de.ganzer.fx.validation.ValidatorTextFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.text.NumberFormat;
import java.text.ParseException;

public class FilterValidatorSettingsController implements TestValidatorController {
    //region fields
    private ValidatorTextFormatter minInputLengthFormatter;
    private ValidatorTextFormatter maxInputLengthFormatter;
    private FilterValidator testValidator;
    //endregion

    //region controls
    public TextField validMask;
    public TextField invalidMask;
    public TextField minInputLength;
    public TextField maxInputLength;
    //endregion

    //region getter/setter
    public Validator getTestValidator() {
        return testValidator;
    }

    public void setTestValidator(Validator testValidator) {
        this.testValidator = (FilterValidator)testValidator;

        minInputLength.setText(String.format("%,d", this.testValidator.getMinLength()));
        maxInputLength.setText(String.format("%,d", this.testValidator.getMaxLength()));
    }
    //endregion

    @Override
    public boolean validateSettings(ValidatorExceptionRef ref) {
        return minInputLengthFormatter.validate(ref)
                && maxInputLengthFormatter.validate(ref);
    }

    //region init
    @FXML
    private void initialize() {
        var validator = new NumberValidator(0, Short.MAX_VALUE);

        minInputLengthFormatter = new ValidatorTextFormatter(validator, minInputLength);
        maxInputLengthFormatter = new ValidatorTextFormatter(validator, maxInputLength);

        initializeListeners();
    }

    @SuppressWarnings("DuplicatedCode")
    private void initializeListeners() {
        validMask.textProperty().addListener((p, o, n) -> testValidator.setValidMask(validMask.getText()));
        invalidMask.textProperty().addListener((p, o, n) -> testValidator.setInvalidMask(invalidMask.getText()));
        minInputLength.textProperty().addListener((p, o, n) -> {
            try {
                if (minInputLength.getText().isEmpty())
                    return;

                testValidator.setMinLength(NumberFormat.getInstance().parse(minInputLength.getText()).intValue());
                maxInputLength.setText(String.format("%d", testValidator.getMaxLength()));
            } catch (ParseException e) {
                System.err.println(e.getMessage());
            }
        });
        maxInputLength.textProperty().addListener((p, o, n) -> {
            try {
                if (maxInputLength.getText().isEmpty())
                    return;

                testValidator.setMaxLength(NumberFormat.getInstance().parse(maxInputLength.getText()).intValue());
                minInputLength.setText(String.format("%d", testValidator.getMinLength()));
            } catch (ParseException e) {
                System.err.println(e.getMessage());
            }
        });
    }
    //endregion
}
