package com.personnel_accounting.entity.converter.toDTO;

import com.personnel_accounting.domain.Task;
import com.personnel_accounting.entity.dto.EmployeeDTO;
import com.personnel_accounting.entity.dto.ProjectDTO;
import com.personnel_accounting.entity.dto.TaskDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

public class TaskDTOConverter implements Converter<Task, TaskDTO> {
    private ConversionService conversionService;

    @Autowired
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public TaskDTO convert(Task source) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(source.getId());
        taskDTO.setName(source.getName());
        taskDTO.setDescription(source.getDescription());
        taskDTO.setProject(
                conversionService.convert(source.getProject(), ProjectDTO.class));
        taskDTO.setReporter(
                conversionService.convert(source.getReporter(), EmployeeDTO.class));
        taskDTO.setAssignee(
                conversionService.convert(source.getAssignee(), EmployeeDTO.class));
        taskDTO.setStatus(source.getTaskStatus());
        return taskDTO;
    }
}
