package com.web.entity.converter;

import com.core.domain.Employee;
import com.core.domain.Project;
import com.core.domain.Task;
import com.service.employee.EmployeeService;
import com.service.project.ProjectService;
import com.web.entity.dto.TaskDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

public class TaskConverter implements Converter<TaskDTO, Task> {
    private ConversionService conversionService;
    private EmployeeService employeeService;

    @Autowired
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Autowired
    public void setProjectService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public Task convert(TaskDTO source) {
        Task task = new Task();
        task.setId(source.getId());
        task.setName(source.getName());
        task.setDescription(source.getDescription());
        task.setProject(
                conversionService.convert(source.getProject(), Project.class));
        task.setReporter(
                conversionService.convert(source.getReporter(), Employee.class));
        task.setAssignee(
                conversionService.convert(source.getAssignee(), Employee.class));
        task.setTaskStatus(source.getStatus());
        return employeeService.mergeTask(task);
    }
}
