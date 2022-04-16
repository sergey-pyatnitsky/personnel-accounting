package com.personnel_accounting.validation;

import com.personnel_accounting.domain.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class EmployeeValidator implements Validator {

    @Autowired
    private MessageSource messageSource;

    @Override
    public boolean supports(Class<?> clazz) {
        return Employee.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Employee employee = (Employee) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.required",
                messageSource.getMessage("employee.validator.name.empty", null, null));
        if (!checkSize(employee.getName()))
            errors.rejectValue("name", "name.size",
                    messageSource.getMessage("employee.validator.name.size", null, null));
    }

    private boolean checkSize(String input) {
        return input.length() <= 256;
    }
}
