package com.personnel_accounting.validation;

import com.personnel_accounting.domain.Department;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class DepartmentValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Department.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Department department = (Department) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.required");
        if (!checkSize(department.getName()))
            errors.rejectValue("name", "name.size");
    }

    private boolean checkSize(String input) {
        return input.length() <= 256;
    }
}
