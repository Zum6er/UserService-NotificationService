package ru.zumber.learning.userservice.validation.name;

import java.util.List;

public class CheckLong extends NameValidator{

    @Override
    protected void validate(String value, List<String> errors) {
        int length = value.length();
        if (length < 3 || length > 20)
            errors.add("Имя должно содержать больше 3 и меньше 21 символа");
    }
}
