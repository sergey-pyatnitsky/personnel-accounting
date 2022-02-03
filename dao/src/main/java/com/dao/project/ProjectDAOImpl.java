package com.dao.project;

import com.core.domain.Department;
import com.core.domain.Project;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class ProjectDAOImpl implements ProjectDAO{
    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Project find(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Project.class, id);
    }

    @Override
    public List<Project> findByActive(boolean isActive) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Project where active = :isActive");
        query.setParameter("isActive", isActive);
        return (List<Project>) query.list();
    }

    @Override
    public List<Project> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return (List<Project>) session.createQuery("from Project").list();
    }

    @Override
    public List<Project> findByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Project where name = :name");
        query.setParameter("name", name);
        return (List<Project>) query.list();
    }

    @Override
    public List<Project> findByDepartment(Department department) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Project where department_id = :id");
        query.setParameter("id", department.getId());
        return (List<Project>) query.list();
    }

    @Override
    public Project save(Project project) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(project);
        return project;
    }

    @Override
    public Project update(Project project) {
        Session session = sessionFactory.getCurrentSession();
        return (Project) session.merge(project);
    }

    @Override
    public boolean removeById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Project project = session.get(Project.class, id);
        if (project == null) return false;
        else{
            try{
                session.delete(project);
            }catch (Exception e){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean remove(Project project) {
        Session session = sessionFactory.getCurrentSession();
        try{
            session.delete(project);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean inactivateById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Project project = session.get(Project.class, id);
        if (project == null) return false;
        else if (project.isActive()) {
            try{
                project.setActive(false);
            }catch (Exception e){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean inactivate(Project project) {
        Session session = sessionFactory.getCurrentSession();
        Project projectFromDB = session.get(Project.class, project.getId());
        if (!project.isActive()) return true;
        else if (projectFromDB == null) return false;
        else {
            try{
                project.setActive(false);
            }catch (Exception e){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean activateById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Project project = session.get(Project.class, id);
        if (project == null) return false;
        else if (!project.isActive()) {
            try{
                project.setActive(true);
            }catch (Exception e){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean activate(Project project) {
        Session session = sessionFactory.getCurrentSession();
        Project projectFromDB = session.get(Project.class, project.getId());
        if (project.isActive()) return true;
        else if (projectFromDB == null) return false;
        else {
            try{
                project.setActive(true);
            }catch (Exception e){
                return false;
            }
        }
        return true;
    }

}
