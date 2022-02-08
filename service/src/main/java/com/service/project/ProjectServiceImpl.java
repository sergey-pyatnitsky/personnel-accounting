package com.service.project;

import com.core.domain.Department;
import com.core.domain.Employee;
import com.core.domain.EmployeePosition;
import com.core.domain.Project;
import com.dao.employee_position.EmployeePositionDAO;
import com.dao.project.ProjectDAO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectDAO projectDAO;
    private final EmployeePositionDAO employeePositionDAO;

    public ProjectServiceImpl(ProjectDAO projectDAO, EmployeePositionDAO employeePositionDAO) {
        this.projectDAO = projectDAO;
        this.employeePositionDAO = employeePositionDAO;
    }

    @Override
    @Transactional
    public EmployeePosition assignToProject(EmployeePosition employeePosition) {
        return employeePositionDAO.save(employeePosition);
    }

    @Override
    @Transactional
    public void changeEmployeeActiveStatusInProject(Employee employee, Project project, boolean isActive) {
        List<EmployeePosition> employeePositions = employeePositionDAO.findByEmployee(employee);
        employeePositions.forEach(obj -> {
            if (obj.getProject() == project) obj.setActive(isActive);
        });
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
}
