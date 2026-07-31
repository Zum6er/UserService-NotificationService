package ru.zumber.learning.userservice.validation.name;

import java.util.List;

public class CheckSymbols extends NameValidator{
    @Override
    protected void validate(String value, List<String> errors) {
        if (!(value.matches("^\\p{L}+$"))) {
            errors.add("Имя должно содержать только буквы");
        }
    }
}
