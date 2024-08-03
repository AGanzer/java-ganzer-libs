package com.example.uitests.fx;

import de.ganzer.core.validation.*;
import de.ganzer.fx.validation.ValidatorTextFormatter;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;

@SuppressWarnings("unused")
public class ValidatorTestController implements TestProvider {
    //region fields
    private Validator testValidator;
    private ValidatorTextFormatter validatorTextFormatter;
    //endregion

    //region controls
    public ComboBox<String> validatorToTest;
    public TextField testInput;
    public TitledPane settingsContainer;
    public CheckBox needsInput;
    public CheckBox blanksValid;
    public CheckBox autoFill;
    //endregion

    //region properties
    private final ListProperty<String> availableValidators = new SimpleListProperty<>(FXCollections.observableArrayList(
            "CharCountValidator",
            "FilterValidator",
            "NumberValidator",
            "PxPicValidator",
            "RegularExpressionValidator",
            "Validator"));
    private final BooleanProperty blanksValidEnabled = new SimpleBooleanProperty(true);
    private final BooleanProperty autoFillEnabled = new SimpleBooleanProperty(false);

    public ObservableList<String> getAvailableValidators() {
        return availableValidators.get();
    }

    public ListProperty<String> availableValidatorsProperty() {
        return availableValidators;
    }

    public boolean isBlanksValidEnabled() {
        return blanksValidEnabled.get();
    }

    public BooleanProperty blanksValidEnabledProperty() {
        return blanksValidEnabled;
    }

    public void setBlanksValidEnabled(boolean blanksValidEnabled) {
        this.blanksValidEnabled.set(blanksValidEnabled);
    }

    public boolean isAutoFillEnabled() {
        return autoFillEnabled.get();
    }

    public BooleanProperty autoFillEnabledProperty() {
        return autoFillEnabled;
    }

    public void setAutoFillEnabled(boolean autoFillEnabled) {
        this.autoFillEnabled.set(autoFillEnabled);
    }
    //endregion

    //region init
    @FXML
    private void initialize() {
        validatorToTest.setValue("NumberValidator");
        setupValidator("NumberValidator");

        Platform.runLater(() -> testInput.requestFocus());
    }
    //endregion

    //region methods
    private void setupValidator(String className) {
        testInput.clear();

        switch (className) {
            case "CharCountValidator":
                setupSettings(new CharCountValidator(), "char-count-validator-settings-view.fxml");
                setBlanksValidEnabled(true);
                setAutoFillEnabled(false);
                break;

            case "FilterValidator":
                setupSettings(new FilterValidator(), "filter-validator-settings-view.fxml");
                setBlanksValidEnabled(true);
                setAutoFillEnabled(false);
                break;

            case "NumberValidator":
                setupSettings(new NumberValidator(), "number-validator-settings-view.fxml");
                setBlanksValidEnabled(false);
                setAutoFillEnabled(false);
                break;

            case "PxPicValidator":
                setupSettings(new PxPicValidator(), "px-pic-validator-settings-view.fxml");
                setBlanksValidEnabled(false);
                setAutoFillEnabled(true);
                break;

            case "RegularExpressionValidator":
                setupSettings(new RegularExpressionValidator(), "regular-expression-validator-settings-view.fxml");
                setBlanksValidEnabled(false);
                setAutoFillEnabled(false);
                break;

            case "Validator":
                setupSettings(new CharCountValidator(), "validator-settings-view.fxml");
                setBlanksValidEnabled(true);
                setAutoFillEnabled(false);
                break;

            default:
                FxTestApp.alertError("Unknown Validator.");
        }
    }

    private void setupSettings(Validator validator, String viewName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FxTestApp.class.getResource(viewName));
            Parent parent = fxmlLoader.load();

            TestValidatorController controller = (TestValidatorController)parent.getUserData();
            controller.setTestValidator(validator);

            settingsContainer.setContent(parent);
        } catch (IOException e) {
            FxTestApp.alertError(String.format("Cannot load %s.", viewName));
            return;
        }

        if (validatorTextFormatter != null)
            validatorTextFormatter.resetIndicators();

        testValidator = validator;
        validatorTextFormatter = new ValidatorTextFormatter(testValidator, testInput);

        enableOptions();
    }

    private void enableOptions() {
        autoFill.setSelected(testValidator.hasOption(ValidatorOptions.AUTO_FILL));
        blanksValid.setSelected(testValidator.hasOption(ValidatorOptions.BLANKS_VALID));
        needsInput.setSelected(testValidator.hasOption(ValidatorOptions.NEEDS_INPUT));
    }

    private void adjustValidatorOptions() {
        var options = ValidatorOptions.NONE;

        if (needsInput.isSelected())
            options |= ValidatorOptions.NEEDS_INPUT;

        if (blanksValid.isSelected())
            options |= ValidatorOptions.BLANKS_VALID;

        if (autoFill.isSelected())
            options |= ValidatorOptions.AUTO_FILL;

        testValidator.setOptions(options);
    }

    private void validate() {
        var message = "Input is valid!";

        try {
            validatorTextFormatter.validate();
        } catch (ValidatorException e) {
            message = e.getMessage();
        }

        FxTestApp.alertInfo(message);

        testInput.requestFocus();
        testInput.selectAll();
    }
    //endregion

    //region actions
    public void onOptionsChanged(ActionEvent ignored) {
        adjustValidatorOptions();
    }

    public void onValidatorChanged(ActionEvent ignored) {
        setupValidator(validatorToTest.getValue());
    }
    //endregion

    @Override
    public void test() {
        validate();
    }
}
