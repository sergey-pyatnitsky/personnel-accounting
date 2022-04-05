package com.personnel_accounting.entity.converter.toDomain;

import com.personnel_accounting.domain.Task;
import com.personnel_accounting.entity.dto.TaskDTO;
import org.springframework.core.convert.converter.Converter;

import java.sql.Date;

public class TaskConverter implements Converter<TaskDTO, Task> {

    @Override
    public Task convert(TaskDTO source) {
        Task task = new Task();
        task.setId(source.getId());
        task.setName(source.getName());
        task.setDescription(source.getDescription());
        task.setTaskStatus(source.getStatus());
        if (source.getCreate_date() != null)
            task.setCreateDate(Date.valueOf(source.getCreate_date()));
        return task;
    }
}
