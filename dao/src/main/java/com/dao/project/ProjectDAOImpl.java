package com.dao.project;

import com.core.domain.Department;
import com.core.domain.Project;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProjectDAOImpl implements ProjectDAO{
    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Project find(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Project.class, id);
    }

    @Override
    public List<Project> findByActive(boolean isActive) {
        Session session = sessionFactory.getCurrentSession();
        List<Project> projectList = session.createQuery(
                "from project where active = :isActive").list();
        return projectList;
    }

    @Override
    public List<Project> findAll() {
        Session session = sessionFactory.getCurrentSession();
        List<Project> projectList = session.createQuery("from project").list();
        return projectList;
    }

    @Override
    public Project findByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Project project = (Project) session.createQuery(
                "from project where name = :name").getSingleResult();
        return project;
    }

    @Override
    public List<Project> findByDepartment(Department department) {
        Session session = sessionFactory.getCurrentSession();
        Long departmentId = department.getId();
        List<Project> projectList = session.createQuery(
                "from project where department_id = :departmentId").list();
        return projectList;
    }

    @Override
    public Project create(Project project) {
        Session session = sessionFactory.getCurrentSession();
        session.save(project);
        return project;
    }

    @Override
    public Project update(Project project) {
        Session session = sessionFactory.getCurrentSession();
        session.update(project);
        return project;
    }

    @Override
    public boolean removeById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Project project = session.get(Project.class, id);
        if (project != null)
            session.delete(project);
        return true;
    }

    @Override
    public boolean remove(Project project) {
        Session session = sessionFactory.getCurrentSession();
        if (session.get(Project.class, project.getId()) != null)
            session.delete(project);
        return true;
    }

    @Override
    public boolean inactivateById(long id) {
        Session session = sessionFactory.getCurrentSession();
        Project project = session.get(Project.class, id);
        if (project == null) return false;
        else if (project.isActive()) {
            project.setActive(false);
            session.update(project);
        }
        return true;
    }

    @Override
    public boolean inactivate(Project project) {
        Session session = sessionFactory.getCurrentSession();
        if (!project.isActive()) return true;
        else if (session.get(Project.class, project.getId()) == null) return false;
        else {
            project.setActive(false);
            session.update(project);
        }
        return true;
    }

    @Override
    public boolean activateById(long id) {
        Session session = sessionFactory.getCurrentSession();
        Project project = session.get(Project.class, id);
        if (project == null) return false;
        else if (!project.isActive()) {
            project.setActive(true);
            session.update(project);
        }
        return true;
    }

    @Override
    public boolean activate(Project project) {
        Session session = sessionFactory.getCurrentSession();
        if (project.isActive()) return true;
        else if (session.get(Project.class, project.getId()) == null) return false;
        else {
            project.setActive(true);
            session.update(project);
        }
        return true;
    }

}
