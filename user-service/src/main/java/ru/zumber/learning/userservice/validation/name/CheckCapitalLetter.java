package ru.zumber.learning.userservice.validation.name;

import java.util.List;

public class CheckCapitalLetter extends NameValidator {

    @Override
    protected void validate(String value, List<String> errors) {
        if (!Character.isUpperCase(value.charAt(0)))
            errors.add("Первая буква имени должна быть заглавной");
    }
}
