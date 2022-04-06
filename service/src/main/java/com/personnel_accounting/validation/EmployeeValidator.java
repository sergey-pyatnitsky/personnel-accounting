package com.personnel_accounting.validation;

import com.personnel_accounting.domain.Employee;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class EmployeeValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Employee.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Employee employee = (Employee) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.required");
        if (!checkSize(employee.getName()))
            errors.rejectValue("name", "name.size");
    }

    private boolean checkSize(String input) {
        return input.length() <= 256;
    }
}
