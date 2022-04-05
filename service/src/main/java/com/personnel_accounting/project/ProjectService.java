package com.personnel_accounting.project;

import com.personnel_accounting.domain.Department;
import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.domain.EmployeePosition;
import com.personnel_accounting.domain.Position;
import com.personnel_accounting.domain.Project;
import com.personnel_accounting.domain.Task;
import com.personnel_accounting.domain.User;
import com.personnel_accounting.enums.TaskStatus;

import java.util.List;

public interface ProjectService {
    Department findDepartmentByUser(User user);

    Project addProject(Project project, Long departmentId);
    boolean closeProject(Project project);

    Position addNewPosition(Position position);
    EmployeePosition changeEmployeePositionInProject(EmployeePosition employeePosition, Position position);
    EmployeePosition assignToProject(Employee employee, Project project, Position position);
    Project assignProjectToDepartmentId(Project project, Long departmentId);
    EmployeePosition changeEmployeeStateInProject(EmployeePosition employeePosition, boolean isActive);
    List<Employee> findByProject(Project project);
    List<EmployeePosition> findProjectEmployeePositions(Employee employee, Project project);
    List<EmployeePosition> findEmployeePositions(Employee employee);

    Project find(Long id);
    List<Project> findByActive(boolean isActive);
    List<Project> findAll();
    List<Project> findByName(String name);
    List<Project> findByDepartment(Department department);

    List<Task> findTaskInProjectByStatus(Project project, TaskStatus taskStatus);
    List<Task> findTasksByEmployeeInProjectWithStatus(Employee employee, Project project, TaskStatus taskStatus);

    Project save(Project project);
    Project update(Project project);
    boolean remove(Project project);
    boolean removeById(Long id);

    boolean inactivate(Project project);
    boolean activate(Project project);
}
