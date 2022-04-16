package com.personnel_accounting.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserDTO {
    @NotBlank(message = "{user.validator.login.empty}")
    private String username;

    @NotBlank(message = "{user.validator.password.empty}")
    private String password;
    private AuthorityDTO authority;
    private boolean isActive;
}
