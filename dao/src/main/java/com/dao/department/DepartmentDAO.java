package com.dao.department;

import com.core.domain.Department;

import java.util.List;

public interface DepartmentDAO {
    Department find(Long id);
    List<Department> findByActive(boolean isActive);
    List<Department> findAll();
    Department findByName(String username);

    Department save(Department user);
    Department update(Department user);
    boolean removeById(Long id);
    boolean remove(Department user);

    boolean inactivateById(Long id);
    boolean inactivate(Department user);

    boolean activateById(Long id);
    boolean activate(Department user);
}
