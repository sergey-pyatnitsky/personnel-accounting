package com.personnel_accounting.image;

import com.personnel_accounting.domain.Image;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public class ImageDAOImpl implements ImageDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Image save(Image image) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(image);
        return image;
    }

    @Override
    public Image findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Image.class, id);
    }

    @Override
    public boolean removeById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Image image = session.get(Image.class, id);
        if (image == null) return false;
        else {
            try {
                session.delete(image);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
}
