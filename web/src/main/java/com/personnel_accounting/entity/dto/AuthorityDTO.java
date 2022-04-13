package com.personnel_accounting.entity.dto;

import com.personnel_accounting.enums.Role;
import lombok.Data;

@Data
public class AuthorityDTO {
    private String username;
    private Role role;
}
