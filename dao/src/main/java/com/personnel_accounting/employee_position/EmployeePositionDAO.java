package com.personnel_accounting.employee_position;

import com.personnel_accounting.domain.*;

import java.util.List;

public interface EmployeePositionDAO {
    EmployeePosition find(Long id);
    List<EmployeePosition> findAll();
    List<EmployeePosition> findByEmployee(Employee employee);
    List<EmployeePosition> findByPosition(Position position);
    List<EmployeePosition> findByProject(Project project);
    List<EmployeePosition> findByDepartment(Department department);
    List<EmployeePosition> findByActive(boolean isActive);

    EmployeePosition save(EmployeePosition employeePosition);
    List<EmployeePosition> save(List<EmployeePosition> employeePositions);
    EmployeePosition update(EmployeePosition employeePosition);
    boolean activate(EmployeePosition employeePosition); //TODO test
    boolean inactivate(EmployeePosition employeePosition); //TODO test
    boolean removeById(Long id);
    boolean remove(EmployeePosition employeePosition);
}
