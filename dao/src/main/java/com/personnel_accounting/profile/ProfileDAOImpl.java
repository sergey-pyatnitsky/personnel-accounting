package com.personnel_accounting.profile;

import com.personnel_accounting.domain.Profile;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class ProfileDAOImpl implements ProfileDAO {
    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Profile find(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Profile.class, id);
    }

    @Override
    public List<Profile> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Profile ").list();
    }

    @Override
    public List<Profile> findByEducation(String education) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(
                "from Profile where education = :education");
        query.setParameter("education", education);
        return query.list();
    }

    @Override
    public Profile findByAddress(String address) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(
                "from Profile where address = :address");
        query.setParameter("address", address);
        return (Profile) query.getSingleResult();
    }

    @Override
    public Profile findByPhone(String phone) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(
                "from Profile where phone = :phone");
        query.setParameter("phone", phone);
        return (Profile) query.getSingleResult();
    }

    @Override
    public Profile findByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(
                "from Profile where email = :email");
        query.setParameter("email", email);
        return (Profile) query.getSingleResult();
    }

    @Override
    public List<Profile> findByPhonePart(String phonePart) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(
                "from Profile where phone like:phonePart");
        query.setParameter("phonePart", "%" + phonePart + "%");
        return query.list();
    }

    @Override
    public List<Profile> findByEmailPart(String emailPart) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(
                "from Profile where email like:emailPart");
        query.setParameter("emailPart", "%" + emailPart + "%");
        return query.list();
    }

    @Override
    public Profile save(Profile profile) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(profile);
        return profile;
    }

    @Override
    public Profile update(Profile profile) {
        Session session = sessionFactory.getCurrentSession();
        return (Profile) session.merge(profile);
    }

    @Override
    public boolean removeById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Profile profile = session.get(Profile.class, id);
        if (profile == null) return false;
        else {
            try {
                session.delete(profile);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean remove(Profile profile) {
        Session session = sessionFactory.getCurrentSession();
        profile = (Profile) session.merge(profile);
        if (profile == null) return false;
        else {
            try {
                session.delete(profile);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
