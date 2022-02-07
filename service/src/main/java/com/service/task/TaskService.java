package com.service.task;

import com.core.domain.Employee;
import com.core.domain.Project;
import com.core.domain.ReportCard;
import com.core.domain.Task;
import com.core.enums.TaskStatus;

import java.sql.Time;
import java.util.List;

public interface TaskService {
    Task changeTaskStatus(Task task, TaskStatus taskStatus);
    ReportCard assigneeTime(Employee assignee, Task task, Time workingTime);

    Task find(Long id);
    List<Task> findAll();
    Task findByName(String name);
    List<Task> findByProject(Project project);
    List<Task> findByReporter(Employee reporter);
    List<Task> findByAssignee(Employee assignee);
    List<Task> findByStatus(TaskStatus status);

    Task save(Task task);
    Task update(Task task);
    boolean remove(Task task);
}
