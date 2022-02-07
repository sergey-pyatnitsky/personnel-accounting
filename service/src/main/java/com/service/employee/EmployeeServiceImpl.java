package com.service.employee;

import com.core.domain.*;
import com.dao.employee.EmployeeDAO;
import com.dao.employee_position.EmployeePositionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    @Transactional
    public Employee addProfileData(Employee employee, Profile profile) {
        employee.setProfile(profile);
        return employeeDAO.save(employee);
    }

    @Override
    @Transactional
    public Employee assignToDepartment(Employee employee, Department department) {
        employee.setDepartment(department);
        employeeDAO.save(employee);
        return null;
    }

    @Override
    @Transactional
    public EmployeePosition assignToProject(Employee employee, Project project, Position position) {
        employeePositionDAO.save(new EmployeePosition(false, employee,
                position, project, project.getDepartment()));
        return null;
    }

    @Override
    @Transactional
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
    @Transactional
    public List<Employee> findByProject(Project project) {
        List<Employee> employees = new ArrayList<>();

        List<EmployeePosition> allEmployeePositionsInProject =
                employeePositionDAO.findByProject(project);
        allEmployeePositionsInProject.forEach(obj -> employees.add(obj.getEmployee()));
        return employees;
    }

    @Override
    @Transactional
    public Employee find(Long id) {
        return employeeDAO.find(id);
    }

    @Override
    @Transactional
    public List<Employee> findAll() {
        return employeeDAO.findAll();
    }

    @Override
    @Transactional
    public List<Employee> findByName(String name) {
        return employeeDAO.findByName(name);
    }

    @Override
    @Transactional
    public List<Employee> findByActive(boolean isActive) {
        return employeeDAO.findByActive(isActive);
    }

    @Override
    @Transactional
    public Employee findByUser(User user) {
        return employeeDAO.findByUser(user);
    }

    @Override
    @Transactional
    public List<Employee> findByDepartment(Department department) {
        return employeeDAO.findByDepartment(department);
    }

    @Override
    @Transactional
    public Employee save(Employee employee) {
        return employeeDAO.save(employee);
    }

    @Override
    @Transactional
    public Employee update(Employee employee) {
        return employeeDAO.update(employee);
    }

    @Override
    @Transactional
    public boolean removeById(Long id) {
        return employeeDAO.removeById(id);
    }

    @Override
    @Transactional
    public boolean remove(Employee employee) {
        return employeeDAO.remove(employee);
    }

    @Override
    @Transactional
    public boolean inactivate(Employee employee) {
        return employeeDAO.inactivate(employee);
    }

    @Override
    @Transactional
    public boolean activate(Employee employee) {
        return employeeDAO.activate(employee);
    }
}
