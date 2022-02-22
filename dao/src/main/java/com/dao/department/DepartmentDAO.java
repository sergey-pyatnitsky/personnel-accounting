package com.dao.department;

import com.core.domain.Department;

import java.util.List;

public interface DepartmentDAO {
    Department find(Long id);
    List<Department> findByActive(boolean isActive);
    List<Department> findAll();
    Department findByName(String name);

    Department save(Department department);
    List<Department> save(List<Department> departments);
    Department update(Department department);
    boolean removeById(Long id);
    boolean remove(Department department);

    boolean inactivateById(Long id);
    boolean inactivate(Department department);

    boolean activateById(Long id);
    boolean activate(Department department);
}
