package com.dao.UserDAO;

import com.core.domain.User;
import com.core.enums.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public User find(int id) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, id);
        return user;
    }

    @Override
    public List<User> findByActive(boolean isActive) {
        Session session = sessionFactory.getCurrentSession();
        List<User> userList = session.createQuery("from user where active = :isActive").list();
        return userList;
    }

    @Override
    public List<User> findAll() {
        Session session = sessionFactory.getCurrentSession();
        List<User> userList = session.createQuery("from user").list();
        return userList;
    }

    @Override
    public User findByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        User user = (User) session.createQuery("from user where username = :username").getSingleResult();
        return user;
    }

    @Override
    public List<User> findByRole(Role role) {
        Session session = sessionFactory.getCurrentSession();
        List<User> userList = session.createQuery("from user where role = :role").list();
        return userList;
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
        if (!user.isActive()) ;
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
        if (user.isActive()) ;
        else if (session.get(User.class, user.getId()) == null) return false;
        else {
            user.setActive(true);
            session.update(user);
        }
        return true;
    }
}
