package com.dao.user;

import com.core.domain.Task;
import com.core.domain.User;
import com.core.enums.Role;
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
    public User find(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(User.class, id);
    }

    @Override
    public List<User> findByActive(boolean isActive) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from User where active = :isActive");
        query.setParameter("isActive", isActive);
        return query.list();
    }

    @Override
    public List<User> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from User").list();
    }

    @Override
    public User findByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from User where username = :username");
        query.setParameter("username", username);
        try{
            return  (User) query.getSingleResult();
        }catch(Exception e){
            System.out.println("Не найдено!");
            return null;
        }
    }

    @Override
    public List<User> findByRole(Role role) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from User where role = :role");
        query.setParameter("role", role);
        return query.list();
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
    public boolean removeById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, id);
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
    public boolean inactivateById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, id);
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
    public boolean activateById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, id);
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
