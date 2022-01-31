package com.dao.user;

import com.core.domain.User;
import com.core.enums.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User find(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(User.class, id);
    }

    @Override
    public List<User> findByActive(boolean isActive) {
        Session session = sessionFactory.getCurrentSession();
        return (List<User>) session.createQuery("from user where active = :isActive").list();
    }

    @Override
    public List<User> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from user").list();
    }

    @Override
    public User findByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        return (User) session.createQuery("from user where username = :username").getSingleResult();
    }

    @Override
    public List<User> findByRole(Role role) {
        Session session = sessionFactory.getCurrentSession();
        return (List<User>) session.createQuery("from user where role = :role").list();
    }

    @Override
    public User create(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.save(user);
        return user;
    }

    @Override
    public User update(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.update(user);
        return user;
    }

    @Override
    public boolean removeById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, id);
        if (user != null)
            session.delete(user);
        return true;
    }

    @Override
    public boolean remove(User user) {
        Session session = sessionFactory.getCurrentSession();
        if (session.get(User.class, user.getId()) != null)
            session.delete(user);
        return true;
    }

    @Override
    public boolean inactivateById(long id) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, id);
        if (user == null) return false;
        else if (user.isActive()) {
            user.setActive(false);
            session.update(user);
        }
        return true;
    }

    @Override
    public boolean inactivate(User user) {
        Session session = sessionFactory.getCurrentSession();
        if (!user.isActive()) return true;
        else if (session.get(User.class, user.getId()) == null) return false;
        else {
            user.setActive(false);
            session.update(user);
        }
        return true;
    }

    @Override
    public boolean activateById(long id) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, id);
        if (user == null) return false;
        else if (!user.isActive()) {
            user.setActive(true);
            session.update(user);
        }
        return true;
    }

    @Override
    public boolean activate(User user) {
        Session session = sessionFactory.getCurrentSession();
        if (user.isActive()) return true;
        else if (session.get(User.class, user.getId()) == null) return false;
        else {
            user.setActive(true);
            session.update(user);
        }
        return true;
    }
}
