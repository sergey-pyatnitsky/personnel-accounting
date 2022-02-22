package com.service.department;

import com.core.domain.*;
import com.dao.department.DepartmentDAO;
import com.dao.employee_position.EmployeePositionDAO;
import com.dao.position.PositionDAO;
import com.dao.project.ProjectDAO;
import com.dao.employee.EmployeeDAO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentDAO departmentDAO;
    private final ProjectDAO projectDAO;
    private final EmployeeDAO employeeDAO;
    private final EmployeePositionDAO employeePositionDAO;
    private final PositionDAO positionDAO;

    public DepartmentServiceImpl(DepartmentDAO departmentDAO, ProjectDAO projectDAO,
                                 EmployeeDAO employeeDAO, EmployeePositionDAO employeePositionDAO,
                                 PositionDAO positionDAO) {
        this.departmentDAO = departmentDAO;
        this.projectDAO = projectDAO;
        this.employeeDAO = employeeDAO;
        this.employeePositionDAO = employeePositionDAO;
        this.positionDAO = positionDAO;
    }

    @Override
    @Transactional
    public List<Project> findProjects(Department department) {
        return projectDAO.findByDepartment(department);
    }

    @Override
    @Transactional
    public List<Employee> findEmployees(Department department) {
        return employeeDAO.findByDepartment(department);
    }

    @Override
    @Transactional
    public Employee assignToDepartment(Employee employee, Department department) {
        employeePositionDAO.findByEmployee(employee).forEach(obj -> obj.setActive(false));

        employee = employeeDAO.update(employee);
        employee.setDepartment(department);
        employeeDAO.save(employee);
        return employee;
    }

    @Override
    @Transactional
    public Department changeDepartmentActiveStatus(Department department, boolean isActive) {
        List<Project> projects = projectDAO.findByDepartment(department);
        projects.forEach(obj -> obj.setActive(false));
        projectDAO.save(projects);

        List<Employee> employees = employeeDAO.findByDepartment(department);
        employees.forEach(obj -> obj.setActive(false));
        employeeDAO.save(employees);

        department = departmentDAO.update(department);
        department.setActive(false);
        return departmentDAO.save(department);
    }

    @Override
    @Transactional
    public Department find(Long id) {
        return departmentDAO.find(id);
    }

    @Override
    @Transactional
    public List<Department> findByActive(boolean isActive) {
        return departmentDAO.findByActive(isActive);
    }

    @Override
    @Transactional
    public List<Department> findAll() {
        return departmentDAO.findAll();
    }

    @Override
    @Transactional
    public Department findByName(String name) {
        return departmentDAO.findByName(name);
    }

    @Override
    @Transactional
    public Department save(Department department) {
        return departmentDAO.save(department);
    }

    @Override
    @Transactional
    public Department update(Department department) {
        return departmentDAO.update(department);
    }

    @Override
    @Transactional
    public boolean remove(Department department) {
        return departmentDAO.remove(department);
    }

    @Override
    @Transactional
    public boolean inactivate(Department department) {
        return departmentDAO.inactivate(department);
    }

    @Override
    @Transactional
    public boolean activate(Department department) {
        return departmentDAO.activate(department);
    }

    @Override
    public Position addPosition(Position position) {
        return positionDAO.save(position);
    }

    @Override
    public Position mergePosition(Position position) {
        return positionDAO.update(position);
    }

    @Override
    public EmployeePosition addEmployeePosition(EmployeePosition employeePosition) {
        return employeePositionDAO.save(employeePosition);
    }

    @Override
    public EmployeePosition mergeEmployeePosition(EmployeePosition employeePosition) {
        return employeePositionDAO.update(employeePosition);
    }
}
