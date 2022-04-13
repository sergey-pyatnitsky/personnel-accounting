package com.personnel_accounting.entity.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String password;
    private AuthorityDTO authority;
    private boolean isActive;
}
