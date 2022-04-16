package com.personnel_accounting.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class EmployeePositionDTO {
    private Long id;

    @NotEmpty(message = "{employee_position.validation.employee.empty}")
    private EmployeeDTO employee;

    @NotEmpty(message = "{employee_position.validation.project.empty}")
    private ProjectDTO project;

    @NotEmpty(message = "{employee_position.validation.department.empty}")
    private DepartmentDTO department;

    @NotEmpty(message = "{employee_position.validation.position.empty}")
    private PositionDTO position;
    private boolean isActive;
    private String start_date;
    private String end_date;
}
