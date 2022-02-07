package com.service.project;

import com.core.domain.Department;
import com.core.domain.Project;
import com.dao.project.ProjectDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService{
    private ProjectDAO projectDAO;

    @Autowired
    public void setProjectDAO(ProjectDAO projectDAO){
        this.projectDAO = projectDAO;
    }

    @Override
    @Transactional
    public Project find(Long id) {
        return projectDAO.find(id);
    }

    @Override
    @Transactional
    public List<Project> findByActive(boolean isActive) {
        return projectDAO.findByActive(isActive);
    }

    @Override
    @Transactional
    public List<Project> findAll() {
        return projectDAO.findAll();
    }

    @Override
    @Transactional
    public List<Project> findByName(String name) {
        return projectDAO.findByName(name);
    }

    @Override
    @Transactional
    public List<Project> findByDepartment(Department department) {
        return projectDAO.findByDepartment(department);
    }

    @Override
    @Transactional
    public Project save(Project project) {
        return projectDAO.save(project);
    }

    @Override
    @Transactional
    public Project update(Project project) {
        return projectDAO.update(project);
    }

    @Override
    @Transactional
    public boolean remove(Project project) {
        return projectDAO.remove(project);
    }

    @Override
    @Transactional
    public boolean inactivate(Project project) {
        return projectDAO.inactivate(project);
    }

    @Override
    @Transactional
    public boolean activate(Project project) {
        return projectDAO.activate(project);
    }
}
