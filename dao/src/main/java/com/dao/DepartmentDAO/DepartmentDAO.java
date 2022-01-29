package com.dao.DepartmentDAO;

import com.core.domain.Department;

import java.util.List;

public interface DepartmentDAO {
    Department find(int id);
    List<Department> findByActive(boolean isActive);
    List<Department> findAll();
    Department findByName(String username);

    Department create(Department user);
    Department update(Department user);
    boolean removeById(Long id);
    boolean remove(Department user);

    boolean inactivateById(long id);
    boolean inactivate(Department user);

    boolean activateById(long id);
    boolean activate(Department user);
}
