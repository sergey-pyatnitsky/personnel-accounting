package com.personnel_accounting.department;

import com.personnel_accounting.domain.Department;
import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.domain.EmployeePosition;
import com.personnel_accounting.domain.Position;
import com.personnel_accounting.domain.Project;
import com.personnel_accounting.pagination.entity.PagingRequest;

import java.util.List;

public interface DepartmentService {
    Department addDepartment(Department department);
    boolean closeDepartment(Department department);

    List<Project> findProjects(Department department);
    List<Project> findProjectsPaginated(PagingRequest pagingRequest, Department department);
    List<Employee> findEmployees(Department department);
    Employee assignToDepartment(Employee employee, Department department);
    Department changeDepartmentState(Department department, boolean isActive);
    Department editDepartmentName(Department department, String name);

    Long getDepartmentCount();
    Long getProjectsByDepartmentCount(Department department);

    Position addPosition(Position position);
    Position editPosition(Position position);
    boolean removePositionById(Long id);
    Position mergePosition(Position position);

    List<Position> findAllPositions();
    List<Position> findAllPositionsWithSearchSorting(PagingRequest pagingRequest);
    Position findPosition(Long id);

    EmployeePosition addEmployeePosition(EmployeePosition employeePosition);
    EmployeePosition mergeEmployeePosition(EmployeePosition employeePosition);

    Department find(Long id);
    List<Department> findByActive(boolean isActive);
    List<Department> findAll(PagingRequest pagingRequest);
    List<Department> findByName(String name);

    Department save(Department department);
    Department merge(Department department);
    boolean remove(Department department);
    boolean removeById(Long id);
    boolean inactivate(Department department);
    boolean activate(Department department);
}
