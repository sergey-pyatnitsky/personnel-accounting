package com.dao.DepartmentDAO;

import com.core.domain.Department;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DepartmentDAOImpl implements DepartmentDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Department find(int id) {
        Session session = sessionFactory.getCurrentSession();
        Department department = session.get(Department.class, id);
        return department;
    }

    @Override
    public List<Department> findByActive(boolean isActive) {
        Session session = sessionFactory.getCurrentSession();
        List<Department> departmentList = session.createQuery(
                "from department where active = :isActive").list();
        return departmentList;
    }

    @Override
    public List<Department> findAll() {
        Session session = sessionFactory.getCurrentSession();
        List<Department> departmentList = session.createQuery("from department").list();
        return departmentList;
    }

    @Override
    public Department findByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Department department = (Department) session.createQuery(
                "from department where name = :name").getSingleResult();
        return department;
    }

    @Override
    public Department create(Department department) {
        Session session = sessionFactory.getCurrentSession();
        session.save(department);
        return department;
    }

    @Override
    public Department update(Department department) {
        Session session = sessionFactory.getCurrentSession();
        session.update(department);
        return department;
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
    public boolean inactivateById(long id) {
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
        if (!department.isActive()) ;
        else if (session.get(Department.class, department.getId()) == null) return false;
        else {
            department.setActive(false);
            session.update(department);
        }
        return true;
    }

    @Override
    public boolean activateById(long id) {
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
        if (department.isActive()) ;
        else if (session.get(Department.class, department.getId()) == null) return false;
        else {
            department.setActive(true);
            session.update(department);
        }
        return true;
    }
}
