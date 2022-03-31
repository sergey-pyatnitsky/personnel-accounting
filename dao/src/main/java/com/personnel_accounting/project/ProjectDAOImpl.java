package com.personnel_accounting.project;

import com.personnel_accounting.domain.Department;
import com.personnel_accounting.domain.Project;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.List;

@Repository
@Transactional
public class ProjectDAOImpl implements ProjectDAO {
    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
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
        Query query = session.createQuery("from Project where isActive = :isActive");
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
        Query query = session.createQuery("from Project where department = :department");
        query.setParameter("department", department);
        return (List<Project>) query.list();
    }

    @Override
    public Project save(Project project) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(project);
        return project;
    }

    @Override
    public List<Project> save(List<Project> projects) {
        Session session = sessionFactory.getCurrentSession();
        projects.forEach(session::saveOrUpdate);
        return projects;
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
        else {
            try {
                session.delete(project);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean remove(Project project) {
        Session session = sessionFactory.getCurrentSession();
        project = (Project) session.merge(project);
        if(project == null) return false;
        else {
            try {
                session.delete(project);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    @Override //FIXME test
    public boolean inactivateById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Project project = session.get(Project.class, id);
        if (project == null) return false;
        else if (project.isActive()) {
            try {
                project.setModifiedDate(new Date(System.currentTimeMillis()));
                project.setActive(false);
                session.saveOrUpdate(project);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override //FIXME test
    public boolean inactivate(Project project) {
        Session session = sessionFactory.getCurrentSession();
        project = (Project) session.merge(project);
        if (project == null) return false;
        else if (!project.isActive()) return true;
        else {
            try {
                project.setModifiedDate(new Date(System.currentTimeMillis()));
                project.setActive(false);
                session.saveOrUpdate(project);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override //FIXME test
    public boolean activateById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Project project = session.get(Project.class, id);
        if (project == null) return false;
        else if (!project.isActive()) {
            try {
                Date date = new Date(System.currentTimeMillis());
                project.setActive(true);
                project.setModifiedDate(date);
                if(project.getStartDate() == null) project.setStartDate(date);
                project.setActive(true);
                session.saveOrUpdate(project);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override //FIXME test
    public boolean activate(Project project) {
        Session session = sessionFactory.getCurrentSession();
        project = (Project) session.merge(project);
        if (project == null) return false;
        else if (project.isActive()) return true;
        else {
            try {
                Date date = new Date(System.currentTimeMillis());
                project.setActive(true);
                project.setModifiedDate(date);
                if(project.getStartDate() == null) project.setStartDate(date);
                session.saveOrUpdate(project);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

}