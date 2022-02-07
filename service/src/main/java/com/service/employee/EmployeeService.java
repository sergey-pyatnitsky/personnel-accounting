package com.service.employee;

import com.core.domain.*;

import java.util.List;

public interface EmployeeService {
    Employee addProfileData(Employee employee, Profile profile);
    Employee assignToDepartment(Employee employee, Department department);
    EmployeePosition assignToProject(Employee employee, Project project);
    void changeActiveStatusInProject(Employee employee, Project project, boolean isActive);

    List<Employee> findByProject(Project project);

    Employee find(Long id);
    List<Employee> findAll();
    List<Employee> findByName(String name);
    List<Employee> findByActive(boolean isActive);
    Employee findByUser(User user);
    List<Employee> findByDepartment(Department department);

    Employee save(Employee employee);
    Employee update(Employee employee);
    boolean removeById(Long id);
    boolean remove(Employee employee);

    boolean inactivate(Employee employee);
    boolean activate(Employee employee);
}
