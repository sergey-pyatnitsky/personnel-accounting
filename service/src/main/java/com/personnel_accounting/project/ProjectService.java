package com.personnel_accounting.project;

import com.personnel_accounting.domain.*;

import java.util.List;

public interface ProjectService {
    Position addNewPosition(Position position);
    EmployeePosition changeEmployeePositionInProject(EmployeePosition employeePosition, Position position);
    EmployeePosition assignToProject(Employee employee, Project project, Position position);
    EmployeePosition changeEmployeeStateInProject(EmployeePosition employeePosition, boolean isActive);
    List<Employee> findByProject(Project project);
    List<EmployeePosition> findProjectEmployeePositions(Employee employee, Project project);
    List<EmployeePosition> findEmployeePositions(Employee employee);

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
