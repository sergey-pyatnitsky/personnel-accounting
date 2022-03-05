package com.personnel_accounting.entity.converter.toDTO;

import com.personnel_accounting.domain.Task;
import com.personnel_accounting.entity.dto.TaskDTO;
import org.springframework.core.convert.converter.Converter;

public class TaskDTOConverter implements Converter<Task, TaskDTO> {

    @Override
    public TaskDTO convert(Task source) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(source.getId());
        taskDTO.setName(source.getName());
        taskDTO.setDescription(source.getDescription());
        taskDTO.setStatus(source.getTaskStatus());
        return taskDTO;
    }
}
