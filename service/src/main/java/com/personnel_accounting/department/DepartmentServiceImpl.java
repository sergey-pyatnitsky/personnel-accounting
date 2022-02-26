package com.personnel_accounting.department;

import com.personnel_accounting.domain.*;
import com.personnel_accounting.employee.EmployeeDAO;
import com.personnel_accounting.employee_position.EmployeePositionDAO;
import com.personnel_accounting.position.PositionDAO;
import com.personnel_accounting.project.ProjectDAO;
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
    public List<Project> findProjects(Department department) {
        return projectDAO.findByDepartment(department);
    }

    @Override
    public List<Employee> findEmployees(Department department) {
        return employeeDAO.findByDepartment(department);
    }

    @Override
    @Transactional
    public Employee assignToDepartment(Employee employee, Department department) {
        if(employee.getDepartment() == null){
            employee.setDepartment(department);
            return employeeDAO.save(employee);
        }
        else if (!employee.getDepartment().getId().equals(department.getId())) {
            List<EmployeePosition> employeePositions = employeePositionDAO.findByEmployee(employee);
            employeePositions.forEach(obj -> obj.setActive(false));
            employeePositionDAO.save(employeePositions);

            employee.setDepartment(department);
            return employeeDAO.save(employee);
        }
        return employeeDAO.update(employee);
    }

    @Override
    public Department changeDepartmentState(Department department, boolean isActive) {
        List<Project> projects = projectDAO.findByDepartment(department);
        projects.forEach(obj -> obj.setActive(isActive));
        projectDAO.save(projects);

        List<Employee> employees = employeeDAO.findByDepartment(department);
        employees.forEach(obj -> obj.setActive(isActive));
        employeeDAO.save(employees);

        department.setActive(false);
        return departmentDAO.save(department);
    }

    @Override
    public Department find(Long id) {
        return departmentDAO.find(id);
    }

    @Override
    public List<Department> findByActive(boolean isActive) {
        return departmentDAO.findByActive(isActive);
    }

    @Override
    public List<Department> findAll() {
        return departmentDAO.findAll();
    }

    @Override
    public Department findByName(String name) {
        return departmentDAO.findByName(name);
    }

    @Override
    public Department save(Department department) {
        return departmentDAO.save(department);
    }

    @Override
    public Department update(Department department) {
        return departmentDAO.update(department);
    }

    @Override
    public boolean remove(Department department) {
        return departmentDAO.remove(department);
    }

    @Override
    public boolean inactivate(Department department) {
        return departmentDAO.inactivate(department);
    }

    @Override
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
