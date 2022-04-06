package com.personnel_accounting.validation;

import com.personnel_accounting.domain.Project;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ProjectValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Project.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Project project = (Project) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.required");
        if (!checkSize(project.getName())) errors.rejectValue("name", "name.size");
        ValidationUtils.rejectIfEmpty(errors, "department", "department.required");
    }

    private boolean checkSize(String input) {
        return input.length() <= 256;
    }
}
