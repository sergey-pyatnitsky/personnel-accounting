package com.dao.department;

import com.core.domain.Department;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class DepartmentDAOImpl implements DepartmentDAO {
    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Department find(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Department.class, id);
    }

    @Override
    public List<Department> findByActive(boolean isActive) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Department where isActive = :isActive");
        query.setParameter("isActive", isActive);
        return (List<Department>) query.list();
    }

    @Override
    public List<Department> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return (List<Department>) session.createQuery("from Department").list();
    }

    @Override
    public Department findByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Department where name = :nameDepartment");
        query.setParameter("nameDepartment", name);
        return (Department) query.getSingleResult();
    }

    @Override
    public Department save(Department department) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(department);
        return department;
    }

    @Override
    public List<Department> save(List<Department> departments) {
        Session session = sessionFactory.getCurrentSession();
        departments.forEach(session::saveOrUpdate);
        return departments;
    }

    @Override
    public Department update(Department department) {
        Session session = sessionFactory.getCurrentSession();
        return (Department) session.merge(department);
    }

    @Override
    public boolean removeById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Department department = session.get(Department.class, id);
        if (department == null) return false;
        else {
            try {
                session.delete(department);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean remove(Department department) {
        Session session = sessionFactory.getCurrentSession();
        department = (Department) session.merge(department);
        if (department == null) return false;
        else {
            try {
                session.delete(department);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    @Override
    public boolean inactivateById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Department department = session.get(Department.class, id);
        if (department == null) return false;
        else if (department.isActive()) {
            try {
                department.setActive(false);
                session.saveOrUpdate(department);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean inactivate(Department department) {
        Session session = sessionFactory.getCurrentSession();
        department = (Department) session.merge(department);
        if (department == null) return false;
        else if (!department.isActive()) return true;
        else {
            try {
                department.setActive(false);
                session.saveOrUpdate(department);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean activateById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Department department = session.get(Department.class, id);
        if (department == null) return false;
        else if (!department.isActive()) {
            try {
                department.setActive(true);
                session.saveOrUpdate(department);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean activate(Department department) {
        Session session = sessionFactory.getCurrentSession();
        department = (Department) session.merge(department);
        if (department == null) return false;
        else if (department.isActive()) return true;
        else {
            try {
                department.setActive(true);
                session.saveOrUpdate(department);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
}
