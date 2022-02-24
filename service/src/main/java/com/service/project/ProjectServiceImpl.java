package com.service.project;

import com.core.domain.*;
import com.dao.employee.EmployeeDAO;
import com.dao.employee_position.EmployeePositionDAO;
import com.dao.position.PositionDAO;
import com.dao.project.ProjectDAO;
import com.dao.task.TaskDAO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectDAO projectDAO;
    private final EmployeePositionDAO employeePositionDAO;
    private final PositionDAO positionDAO;

    public ProjectServiceImpl(ProjectDAO projectDAO, EmployeePositionDAO employeePositionDAO,
                              PositionDAO positionDAO) {
        this.projectDAO = projectDAO;
        this.employeePositionDAO = employeePositionDAO;
        this.positionDAO = positionDAO;
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
    public boolean inactivate(Project project) {
        return projectDAO.inactivate(project);
    }

    @Override
    public boolean activate(Project project) {
        return projectDAO.activate(project);
    }
}
