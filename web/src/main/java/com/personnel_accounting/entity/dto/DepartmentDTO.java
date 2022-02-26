package com.personnel_accounting.entity.dto;

import lombok.Data;

@Data
public class DepartmentDTO {
    private Long id;
    private String name;
    private boolean isActive;
}
