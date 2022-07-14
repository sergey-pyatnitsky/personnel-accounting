package com.personnel_accounting.employee;

import com.personnel_accounting.domain.Department;
import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.domain.Profile;
import com.personnel_accounting.domain.User;
import com.personnel_accounting.pagination.entity.PagingRequest;

import java.util.List;

public interface EmployeeDAO {
    Employee find(Long id);
    List<Employee> findAll(PagingRequest pagingRequest);
    List<Employee> findAllActiveEmployee(PagingRequest pagingRequest);
    List<Employee> findAllActiveAdmins(PagingRequest pagingRequest);
    List<Employee> findAllFreeAndActiveEmployees(PagingRequest pagingRequest);
    List<Employee> findAllAssignedAndActiveEmployees(PagingRequest pagingRequest);
    List<Employee> findAllDismissed(PagingRequest pagingRequest);
    List<Employee> findByName(String name);
    List<Employee> findByNamePart(String namePart);
    List<Employee> findByActive(boolean isActive);
    Employee findByUser(User user);
    List<Employee> findByDepartment(Department department);
    List<Employee> findByDepartmentPaginated(Department department, PagingRequest pagingRequest);
    Employee findByProfile(Profile profile);

    Long getEmployeeCount();
    Long getActiveEmployeeCount();
    Long getActiveAdminCount();
    Long getFreeAndActiveEmployeesCount();
    Long getAssignedAndActiveEmployeesCount();
    Long getDismissedEmployeeCount();
    Long getEmployeeByDepartmentCount(Department department);

    Employee save(Employee employee);
    List<Employee> save(List<Employee> employees);
    Employee merge(Employee employee);
    boolean removeById(Long id);
    boolean remove(Employee employee);

    boolean inactivateById(Long id);
    boolean inactivate(Employee employee);

    boolean activateById(Long id);
    boolean activate(Employee employee);
}
