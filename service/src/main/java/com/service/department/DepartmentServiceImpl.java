package com.service.department;

import com.core.domain.Department;
import com.core.domain.Employee;
import com.core.domain.Project;
import com.dao.department.DepartmentDAO;
import com.dao.employee.EmployeeDAO;
import com.dao.employee_position.EmployeePositionDAO;
import com.dao.project.ProjectDAO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentDAO departmentDAO;
    private final ProjectDAO projectDAO;
    private final EmployeeDAO employeeDAO;
    private final EmployeePositionDAO employeePositionDAO;

    public DepartmentServiceImpl(DepartmentDAO departmentDAO, ProjectDAO projectDAO,
                                 EmployeeDAO employeeDAO, EmployeePositionDAO employeePositionDAO) {
        this.departmentDAO = departmentDAO;
        this.projectDAO = projectDAO;
        this.employeeDAO = employeeDAO;
        this.employeePositionDAO = employeePositionDAO;
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
        return null;
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

        projects.forEach(obj -> obj.setActive(false));
        projectDAO.save(projects);
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
}
