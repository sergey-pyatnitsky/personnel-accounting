package com.personnel_accounting.entity.converter.toDTO;

import com.personnel_accounting.domain.User;
import com.personnel_accounting.entity.dto.UserDTO;
import org.springframework.core.convert.converter.Converter;

public class UserDTOConverter implements Converter<User, UserDTO> {

    @Override
    public UserDTO convert(User source) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(source.getUsername());
        userDTO.setPassword(source.getPassword());
        userDTO.setActive(source.isActive());
        return userDTO;
    }
}
