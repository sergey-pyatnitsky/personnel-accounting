package com.personnel_accounting.validation;

import com.personnel_accounting.domain.Task;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class TaskValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Task.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Task task = (Task) target;
        if (!checkSize(task.getName())) {
            errors.rejectValue("task", "task.size");
        }

        ValidationUtils.rejectIfEmpty(errors, "description", "description.required");
        ValidationUtils.rejectIfEmpty(errors, "project", "project.required");
        ValidationUtils.rejectIfEmpty(errors, "assignee", "assignee.required");
    }

    private boolean checkSize(String input) {
        return input.length() > 100;
    }
}
