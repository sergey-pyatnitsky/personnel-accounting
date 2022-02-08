package com.service.department;

import com.core.domain.Department;
import com.core.domain.Employee;
import com.core.domain.Project;

import java.util.List;

public interface DepartmentService {
    List<Project> findProjects(Department department);
    List<Employee> findEmployees(Department department);
    Employee assignToDepartment(Employee employee, Department department);
    Department changeDepartmentActiveStatus(Department department, boolean isActive);

    Department find(Long id);
    List<Department> findByActive(boolean isActive);
    List<Department> findAll();
    Department findByName(String name);

    Department save(Department department);
    Department update(Department department);
    boolean remove(Department department);
    boolean inactivate(Department department);
    boolean activate(Department department);
}
