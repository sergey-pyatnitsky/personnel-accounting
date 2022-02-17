package com.web.entity.dto;

import com.core.domain.Department;
import com.core.domain.Profile;
import com.core.domain.User;
import lombok.Data;

@Data
public class EmployeeDTO {
    private Long id;
    private User user;
    private String name;
    private Department department;
    private Profile profile;
    private boolean isActive;
}
