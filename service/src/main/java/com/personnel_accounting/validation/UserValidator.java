package com.personnel_accounting.validation;

import com.personnel_accounting.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@PropertySource("classpath:regexp.properties")
@Component
public class UserValidator implements Validator {
    private final Environment env;

    @Autowired
    private MessageSource messageSource;

    public UserValidator(Environment env) {
        this.env = env;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (checkForRegexp(user.getUsername(), env.getProperty("username.regexp"))) {
            errors.rejectValue("username", "username.not_matches",
                    messageSource.getMessage("user.validator.login", null, null));
        }

        if (checkForRegexp(user.getPassword(), env.getProperty("password.regexp"))) {
            errors.rejectValue("password", "password.not_matches",
                    messageSource.getMessage("user.validator.password", null, null));
        }
    }

    private boolean checkForRegexp(String input, String regexp) {
        return !input.matches(regexp);
    }
}
