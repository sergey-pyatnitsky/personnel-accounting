package com.personnel_accounting.entity.converter;

import com.personnel_accounting.domain.User;
import com.personnel_accounting.user.UserService;
import com.personnel_accounting.entity.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

public class UserConverter implements Converter<UserDTO, User> {

    @Override
    public User convert(UserDTO source) {
        User user = new User();
        user.setUsername(source.getUsername());
        user.setPassword(source.getPassword());
        user.setActive(source.isActive());
        return user;
    }
}
