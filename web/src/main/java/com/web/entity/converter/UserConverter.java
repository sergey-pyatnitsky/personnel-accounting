package com.web.entity.converter;

import com.core.domain.User;
import com.service.user.UserService;
import com.web.entity.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

public class UserConverter implements Converter<UserDTO, User> {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User convert(UserDTO source) {
        User user = new User();
        user.setUsername(source.getUsername());
        user.setPassword(source.getPassword());
        user.setActive(source.isActive());
        return user;
    }
}
