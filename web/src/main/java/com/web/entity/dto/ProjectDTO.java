package com.web.entity.dto;

import lombok.Data;

@Data
public class ProjectDTO {
    private Long id;
    private String name;
    private DepartmentDTO department;
    private boolean isActive;
}
