package com.example.uitests;

import de.ganzer.core.validation.Validator;

public interface TestValidatorController {
    Validator getTestValidator();
    void setTestValidator(Validator validator);
}
