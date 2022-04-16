package com.personnel_accounting.entity.converter.domain;

import com.personnel_accounting.domain.Authority;
import com.personnel_accounting.domain.User;
import com.personnel_accounting.entity.dto.UserDTO;
import org.springframework.core.convert.converter.Converter;

public class UserConverter implements Converter<UserDTO, User> {

    @Override
    public User convert(UserDTO source) {
        User user = new User();
        user.setUsername(source.getUsername());
        user.setPassword(source.getPassword());
        user.setActive(source.isActive());

        Authority authority = new Authority();
        authority.setUsername(source.getUsername());
        authority.setRole(source.getAuthority().getRole());
        user.setAuthority(authority);
        return user;
    }
}
