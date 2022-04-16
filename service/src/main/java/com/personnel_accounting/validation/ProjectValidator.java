package com.personnel_accounting.validation;

import com.personnel_accounting.domain.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ProjectValidator implements Validator {

    @Autowired
    private MessageSource messageSource;

    @Override
    public boolean supports(Class<?> clazz) {
        return Project.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Project project = (Project) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.required",
                messageSource.getMessage("project.validator.name.empty", null, null));
        if (!checkSize(project.getName())) errors.rejectValue("name", "name.size",
                messageSource.getMessage("project.validator.name.size", null, null));
        ValidationUtils.rejectIfEmpty(errors, "department", "department.required",
                messageSource.getMessage("project.validator.department.empty", null, null));
    }

    private boolean checkSize(String input) {
        return input.length() <= 256;
    }
}
