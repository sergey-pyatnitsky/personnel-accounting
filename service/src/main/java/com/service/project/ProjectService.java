package com.service.project;

import com.core.domain.Department;
import com.core.domain.Project;

import java.util.List;

public interface ProjectService {
    Project find(Long id);
    List<Project> findByActive(boolean isActive);
    List<Project> findAll();
    List<Project> findByName(String name);
    List<Project> findByDepartment(Department department);

    Project save(Project project);
    Project update(Project project);
    boolean remove(Project project);

    boolean inactivate(Project project);
    boolean activate(Project project);
}
