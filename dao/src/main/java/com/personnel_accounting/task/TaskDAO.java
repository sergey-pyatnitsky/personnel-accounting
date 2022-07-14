package com.personnel_accounting.task;

import com.personnel_accounting.domain.Department;
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
    List<Task> findByStatusInProjectListPaginated(PagingRequest pagingRequest, TaskStatus taskStatus, List<Project> projects);
    List<Task> findByStatusInProjectPaginated(PagingRequest pagingRequest, Project project, TaskStatus taskStatus);
    List<Task> findTaskInDepartmentByStatusPaginated(PagingRequest pagingRequest, Department department, TaskStatus taskStatus);
    List<Task> findTasksByEmployeeInProjectWithStatus(PagingRequest pagingRequest, Employee employee, Project project, TaskStatus taskStatus);
    List<Task> findTaskInDepartmentByStatusAndEmployee(PagingRequest pagingRequest, Department department, Employee employee, TaskStatus taskStatus);
    List<Task> findTaskByStatusAndEmployee(PagingRequest pagingRequest, Employee employee, TaskStatus taskStatus);
    List<Task> findByReporter(Employee reporter);
    List<Task> findByAssignee(Employee assignee);
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByStatusPaginated(PagingRequest pagingRequest, TaskStatus status);

    Long getTaskByStatusCount(TaskStatus status);
    Long getTaskByStatusInProjectCount(Project project, TaskStatus status);
    Long getTaskByStatusInDepartmentCount(Department department, TaskStatus status);
    Long getTaskByStatusInProjectListCount(List<Project> projects, TaskStatus status);
    Long getTasksByEmployeeInProjectWithStatusCount(Employee employee, Project project, TaskStatus status);
    Long getTaskByStatusAndEmployeeCount(Employee employee, TaskStatus status);
    Long getTaskByStatusAndEmployeeInDepartmentCount(Department department, Employee employee, TaskStatus status);

    Task save(Task task);
    Task merge(Task task);
    boolean removeById(Long id);
    boolean remove(Task task);
}
