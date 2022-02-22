package com.service.project;

import com.core.domain.*;
import com.dao.employee_position.EmployeePositionDAO;
import com.dao.position.PositionDAO;
import com.dao.project.ProjectDAO;
import com.dao.task.TaskDAO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectDAO projectDAO;
    private final EmployeePositionDAO employeePositionDAO;
    private final PositionDAO positionDAO;
    private final TaskDAO taskDAO;

    public ProjectServiceImpl(ProjectDAO projectDAO, EmployeePositionDAO employeePositionDAO,
                              PositionDAO positionDAO, TaskDAO taskDAO) {
        this.projectDAO = projectDAO;
        this.employeePositionDAO = employeePositionDAO;
        this.positionDAO = positionDAO;
        this.taskDAO = taskDAO;
    }

    @Override
    public EmployeePosition assignToProject(Employee employee, Project project, Position position) {
        List<EmployeePosition> employeePositions = employeePositionDAO.findByEmployee(employee);

        for (EmployeePosition obj : employeePositions) {
            if (obj.getProject().getId().equals(project.getId()))
                return obj;
        }
        return employeePositionDAO.save(
                new EmployeePosition(false, employee, positionDAO.update(position),
                        project, project.getDepartment()));
    }

    @Override
    @Transactional
    public EmployeePosition changeEmployeeActiveStatusInProject(Employee employee, Project project, boolean isActive) {
        List<EmployeePosition> employeePositions = employeePositionDAO.findByEmployee(employee);
        for (EmployeePosition employeePosition : employeePositions) {
            if (employeePosition.getProject().getId().equals(project.getId())) {
                employeePosition.setActive(isActive);
                return employeePositionDAO.save(employeePosition);
            }
        }
        return null;
    }

    @Override
    @Transactional
    public List<Employee> findByProject(Project project) {
        List<EmployeePosition> employeePositions = employeePositionDAO.findByProject(project);
        List<Employee> employees = new ArrayList<>();
        employeePositions.forEach(obj -> employees.add(obj.getEmployee()));
        return employees;
    }

    @Override
    @Transactional
    public Project find(Long id) {
        return projectDAO.find(id);
    }

    @Override
    @Transactional
    public List<Project> findByActive(boolean isActive) {
        return projectDAO.findByActive(isActive);
    }

    @Override
    @Transactional
    public List<Project> findAll() {
        return projectDAO.findAll();
    }

    @Override
    @Transactional
    public List<Project> findByName(String name) {
        return projectDAO.findByName(name);
    }

    @Override
    @Transactional
    public List<Project> findByDepartment(Department department) {
        return projectDAO.findByDepartment(department);
    }

    @Override
    @Transactional
    public Project save(Project project) {
        return projectDAO.save(project);
    }

    @Override
    @Transactional
    public Project update(Project project) {
        return projectDAO.update(project);
    }

    @Override
    @Transactional
    public boolean remove(Project project) {
        return projectDAO.remove(project);
    }

    @Override
    @Transactional
    public boolean inactivate(Project project) {
        return projectDAO.inactivate(project);
    }

    @Override
    @Transactional
    public boolean activate(Project project) {
        return projectDAO.activate(project);
    }

    @Override
    public Task addTask(Task task) {
        return taskDAO.save(task);
    }

    @Override
    public Task mergeTask(Task task) {
        return taskDAO.update(task);
    }
}
