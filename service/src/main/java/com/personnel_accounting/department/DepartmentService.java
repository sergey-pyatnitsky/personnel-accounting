package com.personnel_accounting.department;

import com.personnel_accounting.domain.*;

import java.util.List;

public interface DepartmentService {
    Department addDepartment(Department department);
    boolean closeDepartment(Department department);

    List<Project> findProjects(Department department);
    List<Employee> findEmployees(Department department);
    Employee assignToDepartment(Employee employee, Department department);
    Department changeDepartmentState(Department department, boolean isActive);

    Position addPosition(Position position);
    Position mergePosition(Position position);

    List<Position> findAllPositions();
    Position findPosition(Long id);

    EmployeePosition addEmployeePosition(EmployeePosition employeePosition);
    EmployeePosition mergeEmployeePosition(EmployeePosition employeePosition);

    Department find(Long id);
    List<Department> findByActive(boolean isActive);
    List<Department> findAll();
    List<Department> findByName(String name);

    Department save(Department department);
    Department update(Department department);
    boolean remove(Department department);
    boolean removeById(Long id);
    boolean inactivate(Department department);
    boolean activate(Department department);
}
