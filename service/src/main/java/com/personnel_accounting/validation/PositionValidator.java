package com.personnel_accounting.validation;

import com.personnel_accounting.domain.Position;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class PositionValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Position.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Position position = (Position) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.required");
        if (!checkSize(position.getName()))
            errors.rejectValue("name", "name.size");
    }

    private boolean checkSize(String input) {
        return input.length() <= 40;
    }
}
