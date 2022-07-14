package com.personnel_accounting.project;

import com.personnel_accounting.domain.Department;
import com.personnel_accounting.domain.Project;
import com.personnel_accounting.pagination.entity.PagingRequest;

import java.util.List;

public interface ProjectDAO {
    Project find(Long id);
    List<Project> findByActive(boolean isActive);
    List<Project> findAll(PagingRequest pagingRequest);
    List<Project> findAllOpen(PagingRequest pagingRequest);
    List<Project> findAllClosed(PagingRequest pagingRequest);
    List<Project> findByName(String name);
    List<Project> findByDepartment(Department department);
    List<Project> findByDepartmentPaginated(PagingRequest pagingRequest, Department department);
    List<Project> findOpenProjectsByDepartmentPaginated(PagingRequest pagingRequest, Department department);

    Long getProjectCount();
    Long getAllOpenProjectCount();
    Long getAllClosedProjectCount();
    Long getProjectsByDepartmentCount(Department department);
    Long getOpenProjectsByDepartmentCount(Department department);

    Project save(Project project);
    List<Project> save(List<Project> projects);
    Project merge(Project project);
    boolean removeById(Long id);
    boolean remove(Project project);

    boolean inactivateById(Long id);
    boolean inactivate(Project project);

    boolean activateById(Long id);
    boolean activate(Project project);
}
