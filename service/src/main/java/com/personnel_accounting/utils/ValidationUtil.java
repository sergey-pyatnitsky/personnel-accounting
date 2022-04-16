package com.personnel_accounting.utils;

import com.personnel_accounting.exception.IncorrectDataException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

public class ValidationUtil {
    public static void validate(Object entity, Validator validator) {
        final DataBinder dataBinder = new DataBinder(entity);
        dataBinder.addValidators(validator);
        dataBinder.validate();

        if (dataBinder.getBindingResult().hasErrors()) {
            throw new IncorrectDataException(dataBinder.getBindingResult().getFieldError().getDefaultMessage());
        }
    }
}
