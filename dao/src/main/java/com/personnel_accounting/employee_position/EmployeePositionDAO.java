package com.personnel_accounting.employee_position;

import com.personnel_accounting.domain.Department;
import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.domain.EmployeePosition;
import com.personnel_accounting.domain.Position;
import com.personnel_accounting.domain.Project;
import com.personnel_accounting.pagination.entity.PagingRequest;

import java.util.List;

public interface EmployeePositionDAO {
    EmployeePosition find(Long id);
    List<EmployeePosition> findAll();
    List<EmployeePosition> findByEmployee(Employee employee);
    List<EmployeePosition> findByEmployeePaginated(PagingRequest pagingRequest, Employee employee);
    List<EmployeePosition> findByPosition(Position position);
    List<EmployeePosition> findByProject(Project project);
    List<EmployeePosition> findByProjectPaginated(PagingRequest pagingRequest, Project project);
    List<EmployeePosition> findByDepartment(Department department);
    List<EmployeePosition> findByActive(boolean isActive);

    Long getEmployeeByProjectCount(Project project);
    Long getByEmployeeCount(Employee employee);

    EmployeePosition save(EmployeePosition employeePosition);
    List<EmployeePosition> save(List<EmployeePosition> employeePositions);
    EmployeePosition merge(EmployeePosition employeePosition);
    boolean activate(EmployeePosition employeePosition);
    boolean inactivate(EmployeePosition employeePosition);
    boolean removeById(Long id);
    boolean remove(EmployeePosition employeePosition);
}
