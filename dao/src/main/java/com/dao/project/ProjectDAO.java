package com.dao.project;

import com.core.domain.Department;
import com.core.domain.Project;

import java.util.List;

public interface ProjectDAO {
    Project find(int id);
    List<Project> findByActive(boolean isActive);
    List<Project> findAll();
    Project findByName(String name);
    List<Project> findByDepartment(Department department);

    Project create(Project user);
    Project update(Project user);
    boolean removeById(Long id);
    boolean remove(Project user);

    boolean inactivateById(long id);
    boolean inactivate(Project user);

    boolean activateById(long id);
    boolean activate(Project user);
}
