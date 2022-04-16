package com.personnel_accounting.entity.dto;

import com.personnel_accounting.enums.TaskStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class TaskDTO {
    private Long id;

    @NotBlank(message = "{task.validation.name.empty}")
    @Size(max=100, message = "{task.validator.name.size}")
    private String name;

    @NotBlank(message = "{task.validator.description.empty}")
    private String description;

    @NotEmpty(message = "{task.validator.project.empty}")
    private ProjectDTO project;
    private EmployeeDTO reporter;

    @NotEmpty(message = "{task.validator.assignee.empty}")
    private EmployeeDTO assignee;
    private TaskStatus status;
    private String create_date;
}
