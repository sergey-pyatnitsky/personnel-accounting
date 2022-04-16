package com.personnel_accounting.entity.dto;

import com.personnel_accounting.enums.Role;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthorityDTO {
    @NotBlank(message = "{username.validation.null}")
    private String username;
    private Role role;
}
