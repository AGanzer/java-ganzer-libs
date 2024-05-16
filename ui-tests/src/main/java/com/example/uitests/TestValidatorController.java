package com.example.uitests;

import de.ganzer.core.validation.Validator;
import de.ganzer.core.validation.ValidatorExceptionRef;

public interface TestValidatorController {
    Validator getTestValidator();
    void setTestValidator(Validator validator);
    boolean validateSettings(ValidatorExceptionRef ref);
}
