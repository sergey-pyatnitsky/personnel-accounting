package com.personnel_accounting.department;

import com.personnel_accounting.domain.Department;
import com.personnel_accounting.pagination.entity.PagingRequest;

import java.util.List;

public interface DepartmentDAO {
    Department find(Long id);
    List<Department> findByActive(boolean isActive);
    List<Department> findAll(PagingRequest pagingRequest);
    List<Department> findAllOpen(PagingRequest pagingRequest);
    List<Department> findAllClosed(PagingRequest pagingRequest);
    List<Department> findByName(String name);

    Long getDepartmentCount();
    Long getOpenDepartmentCount();
    Long getClosedDepartmentCount();

    Department save(Department department);
    List<Department> save(List<Department> departments);
    Department merge(Department department);
    boolean removeById(Long id);
    boolean remove(Department department);

    boolean inactivateById(Long id);
    boolean inactivate(Department department);

    boolean activateById(Long id);
    boolean activate(Department department);
}
