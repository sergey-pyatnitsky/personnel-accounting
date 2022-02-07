package com.service.employee;

import com.core.domain.*;
import com.dao.employee.EmployeeDAO;
import com.dao.employee_position.EmployeePositionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService{
    private EmployeeDAO employeeDAO;
    private EmployeePositionDAO employeePositionDAO;

    @Autowired
    public void setEmployeeDAO(EmployeeDAO employeeDAO){
        this.employeeDAO = employeeDAO;
    }

    @Autowired
    public void setEmployeePositionDAO(EmployeePositionDAO employeePositionDAO){
        this.employeePositionDAO = employeePositionDAO;
    }

    @Override
    public Employee addProfileData(Employee employee, Profile profile) {
        employee.setProfile(profile);
        return employeeDAO.save(employee);
    }

    @Override
    public Employee assignToDepartment(Employee employee, Department department) {
        return null;
    }

    @Override
    public EmployeePosition assignToProject(Employee employee, Project project) {
        return null;
    }

    @Override
    public void changeActiveStatusInProject(Employee employee,
                                                        Project project, boolean isActive) {
        List<EmployeePosition> employeePositions =
                employeePositionDAO.findByEmployee(employee);
        employeePositions.forEach(obj -> {
            if(obj.getProject().equals(project)) {
                obj.setActive(isActive);
            }
        });
    }

    @Override
    public List<Employee> findByProject(Project project) {
        List<Employee> employees = new ArrayList<>();

        List<EmployeePosition> allEmployeePositionsInProject =
                employeePositionDAO.findByProject(project);
        allEmployeePositionsInProject.forEach(obj -> employees.add(obj.getEmployee()));
        return employees;
    }

    @Override
    public Employee find(Long id) {
        return employeeDAO.find(id);
    }

    @Override
    public List<Employee> findAll() {
        return employeeDAO.findAll();
    }

    @Override
    public List<Employee> findByName(String name) {
        return employeeDAO.findByName(name);
    }

    @Override
    public List<Employee> findByActive(boolean isActive) {
        return employeeDAO.findByActive(isActive);
    }

    @Override
    public Employee findByUser(User user) {
        return employeeDAO.findByUser(user);
    }

    @Override
    public List<Employee> findByDepartment(Department department) {
        return employeeDAO.findByDepartment(department);
    }

    @Override
    public Employee save(Employee employee) {
        return employeeDAO.save(employee);
    }

    @Override
    public Employee update(Employee employee) {
        return employeeDAO.update(employee);
    }

    @Override
    public boolean removeById(Long id) {
        return employeeDAO.removeById(id);
    }

    @Override
    public boolean remove(Employee employee) {
        return employeeDAO.remove(employee);
    }

    @Override
    public boolean inactivate(Employee employee) {
        return employeeDAO.inactivate(employee);
    }

    @Override
    public boolean activate(Employee employee) {
        return employeeDAO.activate(employee);
    }
}
