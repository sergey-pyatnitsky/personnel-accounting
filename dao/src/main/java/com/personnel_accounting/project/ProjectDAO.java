package com.personnel_accounting.project;

import com.personnel_accounting.domain.Department;
import com.personnel_accounting.domain.Project;

import java.util.List;

public interface ProjectDAO {
    Project find(Long id);
    List<Project> findByActive(boolean isActive);
    List<Project> findAll();
    List<Project> findByName(String name);
    List<Project> findByDepartment(Department department);

    Project save(Project project);
    List<Project> save(List<Project> projects);
    Project update(Project project);
    boolean removeById(Long id);
    boolean remove(Project project);

    boolean inactivateById(Long id);
    boolean inactivate(Project project);

    boolean activateById(Long id);
    boolean activate(Project project);
}
