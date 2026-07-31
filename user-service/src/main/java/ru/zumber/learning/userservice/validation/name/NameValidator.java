package ru.zumber.learning.userservice.validation.name;

import java.util.List;

public abstract class NameValidator {
    private NameValidator nextNameValidator;

    public void check(String value, List<String> errors) {
        validate(value, errors);
        if (nextNameValidator != null) {
            nextNameValidator.check(value, errors);
        }
    }

    protected abstract void validate(String value, List<String> errors);

    public NameValidator setNextNameValidator(NameValidator nextNameValidator) {
        this.nextNameValidator = nextNameValidator;
        return nextNameValidator;
    }
}
