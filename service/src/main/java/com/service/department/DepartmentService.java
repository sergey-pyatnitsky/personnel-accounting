package com.service.department;

import com.core.domain.Department;

import java.util.List;

public interface DepartmentService {
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
