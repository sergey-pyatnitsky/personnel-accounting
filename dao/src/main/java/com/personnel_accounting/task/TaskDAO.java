package com.personnel_accounting.task;

import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.domain.Project;
import com.personnel_accounting.domain.Task;
import com.personnel_accounting.enums.TaskStatus;
import com.personnel_accounting.pagination.entity.PagingRequest;

import java.util.List;

public interface TaskDAO {
    Task find(Long id);
    List<Task> findAll();
    Task findByName(String name);
    List<Task> findByProject(Project project);
    List<Task> findByProjectPaginated(PagingRequest pagingRequest, Project project);
    List<Task> findByReporter(Employee reporter);
    List<Task> findByAssignee(Employee assignee);
    List<Task> findByStatus(TaskStatus status);

    Long getTaskByStatusCount(Project project, TaskStatus status);

    Task save(Task task);
    Task merge(Task task);
    boolean removeById(Long id);
    boolean remove(Task task);
}
