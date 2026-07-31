package ru.zumber.learning.userservice.validation;

import ru.zumber.learning.userservice.dto.UserDTOForCreateAndUpdate;
import org.springframework.stereotype.Component;
import ru.zumber.learning.userservice.validation.name.CheckCapitalLetter;
import ru.zumber.learning.userservice.validation.name.CheckLong;
import ru.zumber.learning.userservice.validation.name.CheckSymbols;
import ru.zumber.learning.userservice.validation.name.NameValidator;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserValidation {
    private final NameValidator nameValidator;

    public UserValidation() {
        nameValidator = new CheckCapitalLetter();
        nameValidator.setNextNameValidator(new CheckLong())
                .setNextNameValidator(new CheckSymbols());
    }

    public List<String> validateUser(UserDTOForCreateAndUpdate user) {
        List<String> errors = new ArrayList<>();
        validateName(user.getName(), errors);
        validateEmail(user.getEmail(), errors);
        validateAge(user.getAge(), errors);
        return errors;
    }

    private void validateName(String name, List<String> errors) {
        nameValidator.check(name, errors);
    }

    private void validateEmail(String email, List<String> errors) {
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))
            errors.add("Должен быть указан корректный email");
    }

    private void validateAge(int age, List<String> errors) {
        if (age < 0) {
            errors.add("Возраст должен быть положительным числом");
        }
    }

}
