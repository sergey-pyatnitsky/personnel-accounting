package com.personnel_accounting.project;

import com.personnel_accounting.department.DepartmentDAO;
import com.personnel_accounting.domain.Department;
import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.domain.EmployeePosition;
import com.personnel_accounting.domain.Position;
import com.personnel_accounting.domain.Project;
import com.personnel_accounting.domain.Task;
import com.personnel_accounting.domain.User;
import com.personnel_accounting.employee.EmployeeDAO;
import com.personnel_accounting.employee_position.EmployeePositionDAO;
import com.personnel_accounting.enums.Role;
import com.personnel_accounting.enums.TaskStatus;
import com.personnel_accounting.exception.ActiveStatusDataException;
import com.personnel_accounting.exception.ExistingDataException;
import com.personnel_accounting.pagination.entity.PagingRequest;
import com.personnel_accounting.position.PositionDAO;
import com.personnel_accounting.task.TaskDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectDAO projectDAO;

    @Autowired
    private DepartmentDAO departmentDAO;

    @Autowired
    private EmployeePositionDAO employeePositionDAO;

    @Autowired
    private PositionDAO positionDAO;

    @Autowired
    private EmployeeDAO employeeDAO;

    @Autowired
    private TaskDAO taskDAO;

    @Override
    public Project addProject(Project project, Long departmentId) {
        project.setCreateDate(new Date(System.currentTimeMillis()));
        project.setActive(false);
        List<Project> projects = projectDAO.findByName(project.getName())
                .stream().filter(obj -> obj.getEndDate() == null).collect(Collectors.toList());
        if (projects.size() != 0) {
            if (!projects.stream().allMatch(obj -> !obj.getDepartment().getId().equals(departmentId)
                    && obj.getDepartment().isActive()))
                throw new ActiveStatusDataException("Данный отдел неактивен для добавление проектов!");
            else throw new ExistingDataException("Данный проект уже существует!");
        }
        return assignProjectToDepartmentId(project, departmentId);
    }

    @Override
    public Department findDepartmentByUser(User user) {
        return employeeDAO.findByUser(user).getDepartment();
    }

    @Override
    @Transactional
    public boolean closeProject(Project project) {
        Project tempProject = projectDAO.merge(project);
        if (tempProject.getStartDate() == null)
            return projectDAO.remove(tempProject);
        else {
            Date date = new Date(System.currentTimeMillis());
            List<EmployeePosition> employeePositions = employeePositionDAO.findByProject(tempProject);
            List<Task> tasks = taskDAO.findByProject(tempProject);
            if (employeePositions.size() == 0
                    || employeePositions.stream().noneMatch(employeePosition -> employeePosition.getEndDate() == null)) {
                if (tasks.size() == 0 || tasks.stream().noneMatch(task -> task.getTaskStatus() != TaskStatus.CLOSED)) {
                    tempProject.setEndDate(date);
                    tempProject.setModifiedDate(date);
                    projectDAO.save(tempProject);
                    return true;
                }
                return false;
            } else {
                employeePositions.forEach(employeePosition -> {
                    employeePosition.setEndDate(date);
                    employeePosition.setModifiedDate(date);
                    changeEmployeeStateInProject(employeePosition, false);
                });
            }
            return false;
        }
    }

    @Override
    public List<Task> findTaskByStatus(PagingRequest pagingRequest, TaskStatus taskStatus, User user) {
        if (user.getAuthority().getRole().equals(Role.ADMIN))
            return taskDAO.findByStatusPaginated(pagingRequest, taskStatus);
        else return taskDAO.findByStatusInProjectListPaginated(pagingRequest, taskStatus,
                employeePositionDAO.findByEmployee(employeeDAO.findByUser(user))
                        .stream().map(EmployeePosition::getProject).collect(Collectors.toList()));
    }

    @Override
    public List<Task> findTaskByStatusAndEmployee(PagingRequest pagingRequest, Employee employee, TaskStatus taskStatus) {
        return taskDAO.findTaskByStatusAndEmployee(pagingRequest, employee, taskStatus);
    }

    @Override
    public Long getTaskByStatusCount(TaskStatus status, User user) {
        if (user.getAuthority().getRole().equals(Role.ADMIN))
            return taskDAO.getTaskByStatusCount(status);
        else return taskDAO.getTaskByStatusInProjectListCount(
                employeePositionDAO.findByEmployee(employeeDAO.findByUser(user))
                        .stream().map(EmployeePosition::getProject).collect(Collectors.toList())
                , status);
    }

    @Override
    public Long getTaskByStatusAndEmployeeCount(Employee employee, TaskStatus status) {
        return taskDAO.getTaskByStatusAndEmployeeCount(employee, status);
    }

    @Override
    public List<Task> findTaskInProjectByStatus(PagingRequest pagingRequest, Project project, TaskStatus taskStatus) {
        return taskDAO.findByStatusInProjectPaginated(pagingRequest, project, taskStatus);
    }

    @Override
    public List<Task> findTaskInDepartmentByStatus(PagingRequest pagingRequest, Department department, TaskStatus taskStatus) {
        return taskDAO.findTaskInDepartmentByStatusPaginated(pagingRequest, department, taskStatus);
    }

    @Override
    public List<Task> findTaskInDepartmentByStatusAndEmployee(PagingRequest pagingRequest, Department department, Employee employee, TaskStatus taskStatus) {
        return taskDAO.findTaskInDepartmentByStatusAndEmployee(pagingRequest, department, employee, taskStatus);
    }

    @Override
    public List<Task> findTasksByEmployeeInProjectWithStatus(PagingRequest pagingRequest, Employee employee, Project project, TaskStatus taskStatus) {
        return taskDAO.findTasksByEmployeeInProjectWithStatus(pagingRequest, employee, project, taskStatus);
    }

    @Override
    @Transactional
    public Position addNewPosition(Position position) {
        Position tempPosition = positionDAO.findByName(position.getName());
        if (tempPosition == null)
            return positionDAO.save(position);
        return positionDAO.merge(tempPosition);
    }

    @Override
    @Transactional
    public EmployeePosition changeEmployeePositionInProject(EmployeePosition employeePosition, Position position) {
        if (!employeePosition.getPosition().equals(position)) {
            position = positionDAO.merge(position);
            employeePosition.setPosition(position);
            return employeePositionDAO.save(employeePosition);
        }
        return employeePositionDAO.merge(employeePosition);
    }

    @Override
    @Transactional
    public EmployeePosition assignToProject(Employee employee, Project project, Position position) {
        List<EmployeePosition> employeePositions = employeePositionDAO.findByEmployee(employee);

        for (EmployeePosition obj : employeePositions) {
            if (obj.getProject().getId().equals(project.getId()) && obj.getEndDate() == null)
                return employeePositionDAO.merge(obj);
        }
        EmployeePosition employeePosition = new EmployeePosition(true, employee, position,
                project, project.getDepartment());
        employeePosition.setStartDate(new Date(System.currentTimeMillis()));
        return employeePositionDAO.save(employeePosition);
    }

    @Override
    @Transactional
    public Project assignProjectToDepartmentId(Project project, Long departmentId) {
        List<Project> projects = projectDAO.findByName(project.getName())
                .stream().filter(obj -> obj.getEndDate() == null).collect(Collectors.toList());
        if (projects.size() == 0 || projects.stream().allMatch(obj ->
                !obj.getDepartment().getId().equals(departmentId) && obj.getDepartment().isActive())) {
            project.setDepartment(departmentDAO.find(departmentId));
            project = projectDAO.merge(project);
            return projectDAO.save(project);
        }
        return project;
    }

    @Override
    public EmployeePosition changeEmployeeStateInProject(EmployeePosition employeePosition, boolean isActive) {
        if (employeePosition.isActive() != isActive) {
            employeePosition.setActive(isActive);
            Date date = new Date(System.currentTimeMillis());
            employeePosition.setModifiedDate(date);
            employeePosition.setEndDate(date);
            return employeePositionDAO.save(employeePosition);
        }
        return employeePositionDAO.merge(employeePosition);
    }

    @Override
    public Long getEmployeeCount() {
        return projectDAO.getProjectCount();
    }

    @Override
    public Long getAllOpenProjectCount() {
        return projectDAO.getAllOpenProjectCount();
    }

    @Override
    public Long getAllClosedProjectCount() {
        return projectDAO.getAllClosedProjectCount();
    }

    @Override
    public Long getByEmployeeCount(Employee employee) {
        return employeePositionDAO.getByEmployeeCount(employee);
    }

    @Override
    public Long getTaskByStatusInProjectCount(Project project, TaskStatus status) {
        return taskDAO.getTaskByStatusInProjectCount(project, status);
    }

    @Override
    public Long getTaskByStatusInDepartmentCount(Department department, TaskStatus status) {
        return taskDAO.getTaskByStatusInDepartmentCount(department, status);
    }

    @Override
    public Long getTaskByStatusAndEmployeeInDepartmentCount(Department department, Employee employee, TaskStatus status) {
        return taskDAO.getTaskByStatusAndEmployeeInDepartmentCount(department, employee, status);
    }

    @Override
    public Long getTasksByEmployeeInProjectWithStatusCount(Employee employee, Project project, TaskStatus taskStatus) {
        return taskDAO.getTasksByEmployeeInProjectWithStatusCount(employee, project, taskStatus);
    }

    @Override
    public List<Employee> findByProject(Project project) {
        return employeePositionDAO.findByProject(project).stream()
                .map(EmployeePosition::getEmployee)
                .collect(Collectors.toList());
    }

    @Override
    public List<Employee> findByProjectPaginated(PagingRequest pagingRequest, Project project) {
        return employeePositionDAO.findByProjectPaginated(pagingRequest, project).stream()
                .map(EmployeePosition::getEmployee)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<EmployeePosition> findProjectEmployeePositions(Employee employee, Project project) {
        return employeePositionDAO.findByProject(project).stream().filter(
                employeePosition -> employeePosition.getEmployee().getId().equals(employee.getId())).collect(Collectors.toList());
    }

    @Override
    public List<EmployeePosition> findEmployeePositions(Employee employee) {
        return employeePositionDAO.findByEmployee(employee);
    }

    @Override
    public List<EmployeePosition> findByEmployeePaginated(PagingRequest pagingRequest, Employee employee) {
        return employeePositionDAO.findByEmployeePaginated(pagingRequest, employee);
    }

    @Override
    public Project find(Long id) {
        return projectDAO.find(id);
    }

    @Override
    public List<Project> findByActive(boolean isActive) {
        return projectDAO.findByActive(isActive);
    }

    @Override
    public List<Project> findAll(PagingRequest pagingRequest) {
        return projectDAO.findAll(pagingRequest);
    }

    @Override
    public List<Project> findAllOpen(PagingRequest pagingRequest) {
        return projectDAO.findAllOpen(pagingRequest);
    }

    @Override
    public List<Project> findAllClosed(PagingRequest pagingRequest) {
        return projectDAO.findAllClosed(pagingRequest);
    }

    @Override
    public List<Project> findByName(String name) {
        return projectDAO.findByName(name);
    }

    @Override
    public List<Project> findByDepartment(Department department) {
        return projectDAO.findByDepartment(department);
    }

    @Override
    public Project save(Project project) {
        return projectDAO.save(project);
    }

    @Override
    public Project merge(Project project) {
        return projectDAO.merge(project);
    }

    @Override
    public boolean remove(Project project) {
        return projectDAO.remove(project);
    }

    @Override
    public boolean removeById(Long id) {
        return projectDAO.removeById(id);
    }

    @Override
    @Transactional
    public boolean inactivate(Project project) {
        project = projectDAO.merge(project);
        employeePositionDAO.findByProject(project).forEach(employeePosition -> {
            employeePosition.setActive(true);
            employeePositionDAO.save(employeePosition);
        });
        return taskDAO.findByProject(project).stream().noneMatch(task -> task.getTaskStatus() != TaskStatus.CLOSED)
                && projectDAO.inactivate(project);
    }

    @Override
    @Transactional
    public boolean activate(Project project) {
        project = projectDAO.merge(project);
        employeePositionDAO.findByProject(project).forEach(employeePosition -> {
            employeePosition.setActive(true);
            employeePositionDAO.save(employeePosition);
        });
        return projectDAO.activate(project);
    }
}
