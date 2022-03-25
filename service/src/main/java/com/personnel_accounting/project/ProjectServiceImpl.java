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
import com.personnel_accounting.enums.TaskStatus;
import com.personnel_accounting.position.PositionDAO;
import com.personnel_accounting.task.TaskDAO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectDAO projectDAO;
    private final DepartmentDAO departmentDAO;
    private final EmployeePositionDAO employeePositionDAO;
    private final PositionDAO positionDAO;
    private final EmployeeDAO employeeDAO;
    private final TaskDAO taskDAO;

    public ProjectServiceImpl(ProjectDAO projectDAO, DepartmentDAO departmentDAO,
                              EmployeePositionDAO employeePositionDAO, PositionDAO positionDAO,
                              EmployeeDAO employeeDAO, TaskDAO taskDAO) {
        this.projectDAO = projectDAO;
        this.departmentDAO = departmentDAO;
        this.employeePositionDAO = employeePositionDAO;
        this.positionDAO = positionDAO;
        this.employeeDAO = employeeDAO;
        this.taskDAO = taskDAO;
    }

    @Override //TODO test
    public Project addProject(Project project, Long departmentId) {
        project.setCreateDate(new Date(System.currentTimeMillis()));
        project.setActive(false);
        List<Project> projects = projectDAO.findByName(project.getName())
                .stream().filter(obj -> obj.getEndDate() == null).collect(Collectors.toList());
        return projects.size() == 0 || projects.stream().allMatch(obj ->
                !obj.getDepartment().getId().equals(departmentId) && obj.getDepartment().isActive())
                ? assignProjectToDepartmentId(project, departmentId)
                : project;
    }

    @Override //TODO test
    public Department findDepartmentByUser(User user) {
        return employeePositionDAO.findByEmployee(employeeDAO.findByUser(user)).stream().findFirst().get().getDepartment();
    }

    @Override //TODO test
    public boolean closeProject(Project project) {
        Project tempProject = projectDAO.update(project);
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
    public Position addNewPosition(Position position) {
        Position tempPosition = positionDAO.findByName(position.getName());
        if (tempPosition == null)
            return positionDAO.save(position);
        return positionDAO.update(tempPosition);
    }

    @Override
    public EmployeePosition changeEmployeePositionInProject(EmployeePosition employeePosition, Position position) {
        if (!employeePosition.getPosition().equals(position)) {
            position = positionDAO.update(position);
            employeePosition.setPosition(position);
            return employeePositionDAO.update(employeePosition);
        }
        return employeePositionDAO.update(employeePosition);
    }

    @Override
    public EmployeePosition assignToProject(Employee employee, Project project, Position position) {
        List<EmployeePosition> employeePositions = employeePositionDAO.findByEmployee(employee);

        for (EmployeePosition obj : employeePositions) {
            if (obj.getProject().getId().equals(project.getId()))
                return employeePositionDAO.update(obj);
        }
        return employeePositionDAO.save(
                new EmployeePosition(false, employee, positionDAO.save(position),
                        project, project.getDepartment()));
    }

    @Override //TODO test
    public Project assignProjectToDepartmentId(Project project, Long departmentId) {
        List<Project> projects = projectDAO.findByName(project.getName())
                .stream().filter(obj -> obj.getEndDate() == null).collect(Collectors.toList());
        if (projects.size() == 0 || projects.stream().allMatch(obj ->
                !obj.getDepartment().getId().equals(departmentId) && obj.getDepartment().isActive())) {
            project.setDepartment(departmentDAO.find(departmentId));
            return projectDAO.save(project);
        }
        return project;
    }

    @Override
    public EmployeePosition changeEmployeeStateInProject(EmployeePosition employeePosition, boolean isActive) {
        if (employeePosition.isActive() != isActive) {
            employeePosition.setActive(isActive);
            return employeePositionDAO.save(employeePosition);
        }
        return employeePositionDAO.update(employeePosition);
    }

    @Override
    public List<Employee> findByProject(Project project) {
        return employeePositionDAO.findByProject(project).stream()
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
    public Project find(Long id) {
        return projectDAO.find(id);
    }

    @Override
    public List<Project> findByActive(boolean isActive) {
        return projectDAO.findByActive(isActive);
    }

    @Override
    public List<Project> findAll() {
        return projectDAO.findAll();
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
    public Project update(Project project) {
        return projectDAO.update(project);
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
    public boolean inactivate(Project project) {
        project = projectDAO.update(project);
        employeePositionDAO.findByProject(project).forEach(employeePosition -> {
            employeePosition.setActive(true);
            employeePositionDAO.save(employeePosition);
        });
        return taskDAO.findByProject(project).stream().noneMatch(task -> task.getTaskStatus() != TaskStatus.CLOSED)
                && projectDAO.inactivate(project);
    }

    @Override
    public boolean activate(Project project) {
        project = projectDAO.update(project);
        employeePositionDAO.findByProject(project).forEach(employeePosition -> {
            employeePosition.setActive(true);
            employeePositionDAO.save(employeePosition);
        });
        return projectDAO.activate(project);
    }
}
