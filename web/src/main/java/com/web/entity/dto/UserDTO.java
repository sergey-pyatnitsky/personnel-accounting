package com.web.entity.dto;

import com.core.enums.Role;
import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String password;
    private Role role;
    private boolean isActive;
}
