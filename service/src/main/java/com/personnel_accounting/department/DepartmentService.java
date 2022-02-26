package com.personnel_accounting.department;

import com.personnel_accounting.domain.*;

import java.util.List;

public interface DepartmentService {
    List<Project> findProjects(Department department);
    List<Employee> findEmployees(Department department);
    Employee assignToDepartment(Employee employee, Department department);
    Department changeDepartmentState(Department department, boolean isActive);

    Position addPosition(Position position);
    Position mergePosition(Position position);

    EmployeePosition addEmployeePosition(EmployeePosition employeePosition);
    EmployeePosition mergeEmployeePosition(EmployeePosition employeePosition);

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
