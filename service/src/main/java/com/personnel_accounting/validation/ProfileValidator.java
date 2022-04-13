package com.personnel_accounting.validation;

import com.personnel_accounting.domain.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@PropertySource("classpath:application.properties")
@Component
public class ProfileValidator implements Validator {
    private final Environment env;

    public ProfileValidator(Environment env) {
        this.env = env;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Profile.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Profile profile = (Profile) target;
        if (!checkForRegexp(profile.getEmail(), env.getProperty("email.regexp")))
            errors.rejectValue("email", "email.not_matches",
                    "Неверный формат адреса почты");
        if (!checkForRegexp(profile.getPhone(), env.getProperty("phone.regexp")))
            errors.rejectValue("phone", "phone.not_matches",
                    "Неверный формат номера телефона");
        if (checkSize(profile.getEducation(), 2048))
            errors.rejectValue("education", "education.size",
                    "Поле \"Образование\" превышает максимальный допустимый размер");
        if (checkSize(profile.getAddress(), 256))
            errors.rejectValue("address", "address.size",
                    "Поле \"Адрес\" превышает максимальный допустимый размер");
        if (checkSize(profile.getPhone(), 60))
            errors.rejectValue("phone", "phone.size",
                    "Поле \"Мобильный телефон\" превышает максимальный допустимый размер");
        if (checkSize(profile.getEmail(), 256))
            errors.rejectValue("email", "email.size",
                    "Поле \"Адрес электронной почты\" превышает максимальный допустимый размер");
    }

    private boolean checkForRegexp(String input, String regexp) {
        return input.matches(regexp);
    }

    private boolean checkSize(String input, int max) {
        return (input.length() > max);
    }
}
