package com.personnel_accounting.entity.dto;

import lombok.Data;

@Data
public class EmployeeDTO {
    private Long id;
    private UserDTO user;
    private String name;
    private DepartmentDTO department;
    private ProfileDTO profile;
    private boolean isActive;
}
