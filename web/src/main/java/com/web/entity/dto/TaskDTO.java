package com.web.entity.dto;

import com.core.enums.TaskStatus;
import lombok.Data;

@Data
public class TaskDTO {
    private Long id;
    private String name;
    private String description;
    private ProjectDTO project;
    private EmployeeDTO reporter;
    private EmployeeDTO assignee;
    private TaskStatus status;
}
