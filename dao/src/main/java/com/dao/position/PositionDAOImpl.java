package com.dao.position;

import com.core.domain.Position;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class PositionDAOImpl implements PositionDAO {
    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Position find(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Position.class, id);
    }

    @Override
    public Position findByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Position where name = :name");
        query.setParameter("name", name);
        return (Position) query.getSingleResult();
    }

    @Override
    public List<Position> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Position ").list();

    }

    @Override
    public Position save(Position position) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(position);
        return position;
    }

    @Override
    public Position update(Position position) {
        Session session = sessionFactory.getCurrentSession();
        return (Position) session.merge(position);
    }

    @Override
    public boolean removeById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Position position = session.get(Position.class, id);
        if (position == null) return false;
        else {
            try {
                session.delete(position);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean remove(Position position) {
        Session session = sessionFactory.getCurrentSession();
        position = (Position) session.merge(position);
        if (position == null) return false;
        else {
            try {
                session.delete(position);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
