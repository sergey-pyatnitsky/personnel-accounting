package com.personnel_accounting.task;

import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.domain.Project;
import com.personnel_accounting.domain.Task;
import com.personnel_accounting.enums.TaskStatus;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class TaskDAOImpl implements TaskDAO {
    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Task find(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Task.class, id);
    }

    @Override
    public List<Task> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Task").list();
    }

    @Override
    public Task findByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Task where name = :name");
        query.setParameter("name", name);
        return (Task) query.getSingleResult();
    }

    @Override
    public List<Task> findByProject(Project project) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Task where project = :project");
        query.setParameter("project", project);
        return query.list();
    }

    @Override
    public List<Task> findByReporter(Employee reporter) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Task where reporter = :reporter");
        query.setParameter("reporter", reporter);
        return query.list();
    }

    @Override
    public List<Task> findByAssignee(Employee assignee) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Task where assignee = :assignee");
        query.setParameter("assignee", assignee);
        return query.list();
    }

    @Override
    public List<Task> findByStatus(TaskStatus status) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Task where taskStatus = :status");
        query.setParameter("status", status);
        return query.list();
    }

    @Override
    public Task save(Task task) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(task);
        return task;
    }

    @Override
    public Task update(Task task) {
        Session session = sessionFactory.getCurrentSession();
        return (Task) session.merge(task);
    }

    @Override
    public boolean removeById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Task task = session.get(Task.class, id);
        if (task == null) return false;
        else {
            try {
                session.delete(task);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean remove(Task task) {
        Session session = sessionFactory.getCurrentSession();
        task = (Task) session.merge(task);
        if (task == null) return false;
        else {
            try {
                session.delete(task);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
