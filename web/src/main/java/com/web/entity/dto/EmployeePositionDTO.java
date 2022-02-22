package com.web.entity.dto;

import lombok.Data;

@Data
public class EmployeePositionDTO {
    private Long id;
    private EmployeeDTO employee;
    private ProjectDTO project;
    private DepartmentDTO department;
    private PositionDTO position;
    private boolean isActive;
}
