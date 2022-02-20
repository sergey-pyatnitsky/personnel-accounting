package com.dao.user;

import com.core.domain.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class UserDAOImpl implements UserDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User find(String username) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(User.class, username);
    }

    @Override
    public List<User> findByActive(boolean isActive) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from User where isActive = :isActive");
        query.setParameter("isActive", isActive);
        return query.list();
    }

    @Override
    public List<User> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from User").list();
    }

    @Override
    public User save(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(user);
        return user;
    }

    @Override
    public User update(User user) {
        Session session = sessionFactory.getCurrentSession();
        return (User) session.merge(user);
    }

    @Override
    public boolean removeByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, username);
        if (user == null) return false;
        else {
            try {
                session.delete(user);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean remove(User user) {
        Session session = sessionFactory.getCurrentSession();
        user = (User) session.merge(user);
        if (user == null) return false;
        else {
            try {
                session.delete(user);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    @Override
    public boolean inactivateByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, username);
        if (user == null) return false;
        else if (user.isActive()) {
            try {
                user.setActive(false);
                session.saveOrUpdate(user);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean inactivate(User user) {
        Session session = sessionFactory.getCurrentSession();
        user = (User) session.merge(user);
        if (user == null) return false;
        else if (!user.isActive()) return true;
        else {
            try {
                user.setActive(false);
                session.saveOrUpdate(user);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean activateByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, username);
        if (user == null) return false;
        else if (!user.isActive()) {
            try {
                user.setActive(true);
                session.saveOrUpdate(user);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean activate(User user) {
        Session session = sessionFactory.getCurrentSession();
        user = (User) session.merge(user);
        if (user == null) return false;
        else if (user.isActive()) return true;
        else {
            try {
                user.setActive(true);
                session.saveOrUpdate(user);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
}
