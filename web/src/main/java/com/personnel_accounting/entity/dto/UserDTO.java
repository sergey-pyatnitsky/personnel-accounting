package com.personnel_accounting.entity.dto;

import com.personnel_accounting.enums.Role;
import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String password;
    private String repeatPassword;
    private Role role;
    private boolean isActive;
}
