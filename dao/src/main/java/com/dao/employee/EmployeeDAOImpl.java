package com.dao.employee;

import com.core.domain.Department;
import com.core.domain.Employee;
import com.core.domain.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeDAOImpl implements EmployeeDAO {
    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Employee find(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Employee.class, id);
    }

    @Override
    public List<Employee> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Employee ").list();
    }

    @Override
    public List<Employee> findByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(
                "from Employee where name = :name");
        query.setParameter("name", name);
        return query.list();
    }

    @Override
    public List<Employee> findByActive(boolean isActive) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(
                "from Employee where isActive = :isActive");
        query.setParameter("isActive", isActive);
        return query.list();
    }

    @Override
    public Employee findByUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(
                "from Employee where user = :user");
        query.setParameter("user", user);
        return (Employee) query.getSingleResult();
    }

    @Override
    public List<Employee> findByDepartment(Department department) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(
                "from Employee where department = :department");
        query.setParameter("department", department);
        return query.list();
    }

    @Override
    public Employee save(Employee employee) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(employee);
        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        Session session = sessionFactory.getCurrentSession();
        return (Employee) session.merge(employee);
    }

    @Override
    public boolean removeById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Employee employee = session.get(Employee.class, id);
        if (employee == null) return false;
        else {
            try {
                session.delete(employee);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean remove(Employee employee) {
        Session session = sessionFactory.getCurrentSession();
        Employee employeeFromDB =
                session.get(Employee.class, employee.getId());
        if (employeeFromDB == null) return false;
        else {
            try {
                session.delete(employee);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    @Override
    public boolean inactivateById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Employee employee = session.get(Employee.class, id);
        if (employee == null) return false;
        else if (employee.isActive()) {
            try {
                employee.setActive(false);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean inactivate(Employee employee) {
        Session session = sessionFactory.getCurrentSession();
        Employee employeeFromDB = session.get(Employee.class, employee.getId());
        if (!employee.isActive()) return true;
        else if (employeeFromDB == null) return false;
        else {
            try {
                employee.setActive(false);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean activateById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Employee employee = session.get(Employee.class, id);
        if (employee == null) return false;
        else if (!employee.isActive()) {
            try {
                employee.setActive(true);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean activate(Employee employee) {
        Session session = sessionFactory.getCurrentSession();
        Employee employeeFromDB = session.get(Employee.class, employee.getId());
        if (employee.isActive()) return true;
        else if (employeeFromDB == null) return false;
        else {
            try {
                employee.setActive(true);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
}
