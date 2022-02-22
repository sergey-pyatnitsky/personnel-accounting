package com.service.project;

import com.core.domain.*;

import java.util.List;

public interface ProjectService {
    EmployeePosition assignToProject(Employee employee, Project project, Position position);
    EmployeePosition changeEmployeeActiveStatusInProject(Employee employee, Project project, boolean isActive);
    List<Employee> findByProject(Project project);

    Task addTask(Task task);
    Task mergeTask(Task task);

    Project find(Long id);
    List<Project> findByActive(boolean isActive);
    List<Project> findAll();
    List<Project> findByName(String name);
    List<Project> findByDepartment(Department department);

    Project save(Project project);
    Project update(Project project);
    boolean remove(Project project);

    boolean inactivate(Project project);
    boolean activate(Project project);
}
