package com.personnel_accounting.project;

import com.personnel_accounting.domain.Department;
import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.domain.EmployeePosition;
import com.personnel_accounting.domain.Position;
import com.personnel_accounting.domain.Project;
import com.personnel_accounting.domain.Task;
import com.personnel_accounting.domain.User;
import com.personnel_accounting.enums.TaskStatus;
import com.personnel_accounting.pagination.entity.PagingRequest;

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
    List<Employee> findByProjectPaginated(PagingRequest pagingRequest, Project project);
    List<EmployeePosition> findProjectEmployeePositions(Employee employee, Project project);
    List<EmployeePosition> findEmployeePositions(Employee employee);
    List<EmployeePosition> findByEmployeePaginated(PagingRequest pagingRequest, Employee employee);

    Long getAllOpenProjectCount();
    Long getAllClosedProjectCount();

    Long getEmployeeCount();
    Long getByEmployeeCount(Employee employee);
    Long getTaskByStatusInProjectCount(Project project, TaskStatus status);
    Long getTaskByStatusInDepartmentCount(Department department, TaskStatus status);
    Long getTaskByStatusAndEmployeeInDepartmentCount(Department department, Employee employee, TaskStatus status);
    Long getTasksByEmployeeInProjectWithStatusCount(Employee employee, Project project, TaskStatus taskStatus);
    Long getTaskByStatusCount(TaskStatus status, User user);
    Long getTaskByStatusAndEmployeeCount(Employee employee, TaskStatus status);

    Project find(Long id);
    List<Project> findByActive(boolean isActive);
    List<Project> findAll(PagingRequest pagingRequest);
    List<Project> findAllOpen(PagingRequest pagingRequest);
    List<Project> findAllClosed(PagingRequest pagingRequest);
    List<Project> findByName(String name);
    List<Project> findByDepartment(Department department);

    List<Task> findTaskByStatus(PagingRequest pagingRequest, TaskStatus taskStatus, User user);
    List<Task> findTaskByStatusAndEmployee(PagingRequest pagingRequest, Employee employee, TaskStatus taskStatus);
    List<Task> findTaskInDepartmentByStatus(PagingRequest pagingRequest, Department department, TaskStatus taskStatus);
    List<Task> findTaskInDepartmentByStatusAndEmployee(PagingRequest pagingRequest, Department department, Employee employee, TaskStatus taskStatus);
    List<Task> findTaskInProjectByStatus(PagingRequest pagingRequest, Project project, TaskStatus taskStatus);
    List<Task> findTasksByEmployeeInProjectWithStatus(PagingRequest pagingRequest, Employee employee, Project project, TaskStatus taskStatus);

    Project save(Project project);
    Project merge(Project project);
    boolean remove(Project project);
    boolean removeById(Long id);

    boolean inactivate(Project project);
    boolean activate(Project project);
}
