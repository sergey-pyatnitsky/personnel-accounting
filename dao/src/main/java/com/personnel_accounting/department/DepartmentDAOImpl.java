package com.personnel_accounting.department;

import com.personnel_accounting.domain.Department;
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
    public List<Department> findByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Department where name = :nameDepartment");
        query.setParameter("nameDepartment", name);
        return query.getResultList();
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

    @Override //FIXME test
    public boolean inactivateById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Department department = session.get(Department.class, id);
        if (department == null) return false;
        else if (department.isActive()) {
            try {
                department.setModifiedDate(new Date(System.currentTimeMillis()));
                department.setActive(false);
                session.saveOrUpdate(department);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override //FIXME test
    public boolean inactivate(Department department) {
        Session session = sessionFactory.getCurrentSession();
        department = (Department) session.merge(department);
        if (department == null) return false;
        else if (!department.isActive()) return true;
        else {
            try {
                department.setModifiedDate(new Date(System.currentTimeMillis()));
                department.setActive(false);
                session.saveOrUpdate(department);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override //FIXME test
    public boolean activateById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Department department = session.get(Department.class, id);
        if (department == null) return false;
        else if (!department.isActive()) {
            try {
                Date date = new Date(System.currentTimeMillis());
                department.setModifiedDate(date);
                if(department.getCreateDate() == null) department.setCreateDate(date);
                department.setActive(true);
                session.saveOrUpdate(department);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override //FIXME test
    public boolean activate(Department department) {
        Session session = sessionFactory.getCurrentSession();
        department = (Department) session.merge(department);
        if (department == null) return false;
        else if (department.isActive()) return true;
        else {
            try {
                Date date = new Date(System.currentTimeMillis());
                department.setModifiedDate(date);
                if(department.getStartDate() == null) department.setStartDate(date);
                department.setActive(true);
                session.saveOrUpdate(department);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
}