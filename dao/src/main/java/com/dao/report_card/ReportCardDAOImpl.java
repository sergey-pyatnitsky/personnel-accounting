package com.dao.report_card;

import com.core.domain.Employee;
import com.core.domain.ReportCard;
import com.core.domain.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public class ReportCardDAOImpl implements ReportCardDAO {
    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public ReportCard find(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(ReportCard.class, id);
    }

    @Override
    public List<ReportCard> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from ReportCard ").list();
    }

    @Override
    public List<ReportCard> findByDate(Date date) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from ReportCard where date = :date");
        query.setParameter("date", date);
        return query.list();
    }

    @Override
    public ReportCard findByTask(Task task) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from ReportCard where task = :task");
        query.setParameter("task", task);
        return (ReportCard) query.getSingleResult();
    }

    @Override
    public List<ReportCard> findByEmployee(Employee employee) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from ReportCard where employee = :employee");
        query.setParameter("employee", employee);
        return query.list();
    }

    @Override
    public ReportCard save(ReportCard reportCard) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(reportCard);
        return reportCard;
    }

    @Override
    public ReportCard update(ReportCard reportCard) {
        Session session = sessionFactory.getCurrentSession();
        return (ReportCard) session.merge(reportCard);
    }

    @Override
    public boolean removeById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        ReportCard reportCard = session.get(ReportCard.class, id);
        if (reportCard == null) return false;
        else {
            try {
                session.delete(reportCard);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean remove(ReportCard reportCard) {
        Session session = sessionFactory.getCurrentSession();
        ReportCard reportCardFromDB = session.get(ReportCard.class, reportCard.getId());
        if (reportCardFromDB == null) return false;
        else {
            try {
                session.delete(reportCard);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
