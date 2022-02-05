package com.dao.task;

import com.core.domain.Employee;
import com.core.domain.Project;
import com.core.domain.Task;
import com.core.enums.TaskStatus;

import java.util.List;

public interface TaskDAO {
    Task find(Long id);
    List<Task> findAll();
    Task findByName(String name);
    List<Task> findByProject(Project project);
    List<Task> findByReporter(Employee reporter);
    List<Task> findByAssignee(Employee assignee);
    List<Task> findByStatus(TaskStatus status);

    Task save(Task profile);
    Task update(Task profile);
    boolean removeById(Long id);
    boolean remove(Task task);
}
