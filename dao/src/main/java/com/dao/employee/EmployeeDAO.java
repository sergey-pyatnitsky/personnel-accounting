package com.dao.employee;

import com.core.domain.Department;
import com.core.domain.Employee;
import com.core.domain.User;

import java.util.List;

public interface EmployeeDAO {
    Employee find(Long id);
    List<Employee> findAll();
    List<Employee> findByName(String name);
    List<Employee> findByActive(boolean isActive);
    Employee findByUser(User user);
    List<Employee> findByDepartment(Department department);

    Employee save(Employee employee);
    List<Employee> save(List<Employee> employees);
    Employee update(Employee employee);
    boolean removeById(Long id);
    boolean remove(Employee employee);

    boolean inactivateById(Long id);
    boolean inactivate(Employee employee);

    boolean activateById(Long id);
    boolean activate(Employee employee);
}
