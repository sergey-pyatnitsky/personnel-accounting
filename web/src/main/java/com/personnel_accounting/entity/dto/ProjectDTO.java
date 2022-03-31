package com.personnel_accounting.entity.dto;

import lombok.Data;

@Data
public class ProjectDTO {
    private Long id;
    private String name;
    private DepartmentDTO department;
    private boolean isActive;
    private String start_date;
    private String end_date;
}