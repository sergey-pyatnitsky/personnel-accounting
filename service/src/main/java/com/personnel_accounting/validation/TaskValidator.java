package com.personnel_accounting.validation;

import com.personnel_accounting.domain.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class TaskValidator implements Validator {

    @Autowired
    private MessageSource messageSource;

    @Override
    public boolean supports(Class<?> clazz) {
        return Task.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Task task = (Task) target;
        if (!checkSize(task.getName())) {
            errors.rejectValue("name", "task.size",
                    messageSource.getMessage("task.validator.name.size", null, null));
        }

        ValidationUtils.rejectIfEmpty(errors, "description", "description.required",
                messageSource.getMessage("task.validator.description.empty", null, null));
        ValidationUtils.rejectIfEmpty(errors, "project", "project.required",
                messageSource.getMessage("task.validator.project.empty", null, null));
        ValidationUtils.rejectIfEmpty(errors, "assignee", "assignee.required",
                messageSource.getMessage("task.validator.assignee.empty", null, null));
    }

    private boolean checkSize(String input) {
        return input.length() <= 100;
    }
}
