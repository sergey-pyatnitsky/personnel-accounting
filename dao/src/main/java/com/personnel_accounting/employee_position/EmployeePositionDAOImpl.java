package com.personnel_accounting.employee_position;

import com.personnel_accounting.domain.Department;
import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.domain.EmployeePosition;
import com.personnel_accounting.domain.Position;
import com.personnel_accounting.domain.Project;
import com.personnel_accounting.pagination.entity.Column;
import com.personnel_accounting.pagination.entity.Order;
import com.personnel_accounting.pagination.entity.PagingRequest;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class EmployeePositionDAOImpl implements EmployeePositionDAO {
    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public EmployeePosition find(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(EmployeePosition.class, id);
    }

    @Override
    public List<EmployeePosition> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from EmployeePosition ").list();
    }

    @Override
    public List<EmployeePosition> findByEmployee(Employee employee) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(
                "from EmployeePosition where employee = :employee");
        query.setParameter("employee", employee);
        return query.list();
    }

    @Override
    public List<EmployeePosition> findByPosition(Position position) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(
                "from EmployeePosition where position = :position");
        query.setParameter("position", position);
        return query.list();
    }

    @Override
    public List<EmployeePosition> findByProject(Project project) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(
                "from EmployeePosition where project = :project");
        query.setParameter("project", project);
        return query.list();
    }

    @Override
    public List<EmployeePosition> findByProjectPaginated(PagingRequest pagingRequest, Project project) {
        Session session = sessionFactory.getCurrentSession();
        Order order = pagingRequest.getOrder().get(0);
        Column column = pagingRequest.getColumns().get(order.getColumn());
        String hql = "from EmployeePosition where project = :project";
        if (!pagingRequest.getSearch().getValue().equals(""))
            hql += " and where concat(employee.id, employee.name, employee.user.username, employee.user.authority.role, isActive) " +
                    "like '%" + pagingRequest.getSearch().getValue() + "%'";
        hql += " order by " + column.getData() + " " + order.getDir().toString();
        Query query = session.createQuery(hql);
        query.setParameter("project", project);
        query.setFirstResult(pagingRequest.getStart());
        query.setMaxResults(pagingRequest.getLength());
        return query.list();
    }

    @Override
    public List<EmployeePosition> findByEmployeePaginated(PagingRequest pagingRequest, Employee employee) {
        Session session = sessionFactory.getCurrentSession();
        Order order = pagingRequest.getOrder().get(0);
        Column column = pagingRequest.getColumns().get(order.getColumn());
        String hql = "from EmployeePosition where employee = :employee";
        if (!pagingRequest.getSearch().getValue().equals(""))
            hql += " and where concat(employee.id, employee.name, employee.user.username, employee.user.authority.role, isActive) " +
                    "like '%" + pagingRequest.getSearch().getValue() + "%'";
        hql += " order by " + column.getData() + " " + order.getDir().toString();
        Query query = session.createQuery(hql);
        query.setParameter("employee", employee);
        query.setFirstResult(pagingRequest.getStart());
        query.setMaxResults(pagingRequest.getLength());
        return query.list();
    }

    @Override
    public List<EmployeePosition> findByDepartment(Department department) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(
                "from EmployeePosition where department = :department");
        query.setParameter("department", department);
        return query.list();
    }

    @Override
    public List<EmployeePosition> findByActive(boolean isActive) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(
                "from EmployeePosition where isActive = :isActive");
        query.setParameter("isActive", isActive);
        return query.list();
    }

    @Override
    public Long getEmployeeByProjectCount(Project project) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(
                "select count(*) from EmployeePosition where project = :project");
        query.setParameter("project", project);
        return (Long) query.getSingleResult();
    }

    @Override
    public Long getByEmployeeCount(Employee employee) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(
                "select count(*) from EmployeePosition where employee = :employee");
        query.setParameter("employee", employee);
        return (Long) query.getSingleResult();
    }

    @Override
    public EmployeePosition save(EmployeePosition employeePosition) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(employeePosition);
        return employeePosition;
    }

    @Override
    public List<EmployeePosition> save(List<EmployeePosition> employeePositions) {
        Session session = sessionFactory.getCurrentSession();
        employeePositions.forEach(session::saveOrUpdate);
        return employeePositions;
    }

    @Override
    public EmployeePosition merge(EmployeePosition employeePosition) {
        Session session = sessionFactory.getCurrentSession();
        return (EmployeePosition) session.merge(employeePosition);
    }

    @Override
    public boolean activate(EmployeePosition employeePosition) {
        Session session = sessionFactory.getCurrentSession();
        employeePosition = (EmployeePosition) session.merge(employeePosition);
        if (employeePosition == null) return false;
        else if (employeePosition.isActive()) return true;
        else {
            try {
                employeePosition.setActive(true);
                session.saveOrUpdate(employeePosition);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean inactivate(EmployeePosition employeePosition) {
        Session session = sessionFactory.getCurrentSession();
        employeePosition = (EmployeePosition) session.merge(employeePosition);
        if (employeePosition == null) return false;
        else if (!employeePosition.isActive()) return true;
        else {
            try {
                employeePosition.setActive(false);
                session.saveOrUpdate(employeePosition);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean removeById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        EmployeePosition employeePosition = session.get(EmployeePosition.class, id);
        if (employeePosition == null) return false;
        else {
            try {
                session.delete(employeePosition);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean remove(EmployeePosition employeePosition) {
        Session session = sessionFactory.getCurrentSession();
        employeePosition = (EmployeePosition) session.merge(employeePosition);
        if (employeePosition == null) return false;
        else {
            try {
                session.delete(employeePosition);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
