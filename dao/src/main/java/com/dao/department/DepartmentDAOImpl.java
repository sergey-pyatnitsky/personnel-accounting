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
        Department department = session.get(Department.class, id);
        return department;
    }

    @Override
    public List<Department> findByActive(boolean isActive) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Department where active = :isActive");
        query.setParameter("isActive", isActive);

        List<Department> departmentList = query.list();
        return departmentList;
    }

    @Override
    public List<Department> findAll() {
        Session session = sessionFactory.getCurrentSession();
        List<Department> departmentList = session.createQuery("from Department").list();
        return departmentList;
    }

    @Override
    public Department findByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Department where name = :nameDepartment");
        query.setParameter("nameDepartment", name);
        return (Department) query.list().get(0);
    }

    @Override
    public Department create(Department department) {
        Session session = sessionFactory.getCurrentSession();
        session.save(department);
        return findByName(department.getName());
    }

    @Override
    public Department update(Department department) {
        Session session = sessionFactory.getCurrentSession();
        session.update(department);
        return findByName(department.getName());
    }

    @Override
    public boolean removeById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Department department = session.get(Department.class, id);
        if (department != null)
            session.delete(department);
        return true;
    }

    @Override
    public boolean remove(Department department) {
        Session session = sessionFactory.getCurrentSession();
        if (session.get(Department.class, department.getId()) != null)
            session.delete(department);
        return true;
    }

    @Override
    public boolean inactivateById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Department department = session.get(Department.class, id);
        if (department == null) return false;
        else if (department.isActive()) {
            department.setActive(false);
            session.update(department);
        }
        return true;
    }

    @Override
    public boolean inactivate(Department department) {
        Session session = sessionFactory.getCurrentSession();
        if (!department.isActive()) return true;
        else if (session.get(Department.class, department.getId()) == null) return false;
        else {
            department.setActive(false);
            session.update(department);
        }
        return true;
    }

    @Override
    public boolean activateById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Department department = session.get(Department.class, id);
        if (department == null) return false;
        else if (!department.isActive()) {
            department.setActive(true);
            session.update(department);
        }
        return true;
    }

    @Override
    public boolean activate(Department department) {
        Session session = sessionFactory.getCurrentSession();
        if (department.isActive()) return true;
        else if (session.get(Department.class, department.getId()) == null) return false;
        else {
            department.setActive(true);
            session.update(department);
        }
        return true;
    }
}
