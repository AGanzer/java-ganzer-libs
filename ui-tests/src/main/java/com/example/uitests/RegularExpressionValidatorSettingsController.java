package com.example.uitests;

import de.ganzer.core.validation.RegularExpressionValidator;
import de.ganzer.core.validation.Validator;
import de.ganzer.core.validation.ValidatorExceptionRef;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@SuppressWarnings("unused")
public class RegularExpressionValidatorSettingsController implements TestValidatorController {
    //region fields
    private RegularExpressionValidator testValidator;
    //endregion

    //region controls
    public TextField expression;
    public CheckBox ignoreCase;
    //endregion

    //region properties
    private final StringProperty expressionInfo = new SimpleStringProperty();

    public String getExpressionInfo() {
        return expressionInfo.get();
    }

    public StringProperty expressionInfoProperty() {
        return expressionInfo;
    }

    public void setExpressionInfo(String expressionInfo) {
        this.expressionInfo.set(expressionInfo);
    }
    //endregion

    //region getter/setter
    public Validator getTestValidator() {
        return testValidator;
    }

    public void setTestValidator(Validator testValidator) {
        this.testValidator = (RegularExpressionValidator)testValidator;
    }
    //endregion

    @Override
    public boolean validateSettings(ValidatorExceptionRef ref) {
        return true;
    }

    //region init
    @FXML
    private void initialize() {
        initializeListeners();
        updateExpressionHint(true);
    }

    private void initializeListeners() {
        expression.textProperty().addListener((p, o, n) -> updateValidator());
    }
    //endregion

    //region methods
    private void updateExpressionHint(boolean valid) {
        setExpressionInfo(valid ? "The expression is valid." : "The expression is invalid yet.");
    }

    private void updateValidator() {
        try {
            var flags = ignoreCase.isSelected() ? Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE : 0;
            var pattern = Pattern.compile(expression.getText(), flags);

            testValidator.setPattern(pattern);

            updateExpressionHint(true);
        } catch (PatternSyntaxException e) {
            updateExpressionHint(false);
        }
    }
    //endregion

    //region event handling
    public void onIgnoreCaseChanged(ActionEvent ignored) {
        updateValidator();
    }
    //endregion
}
