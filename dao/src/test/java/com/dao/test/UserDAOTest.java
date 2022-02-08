package com.dao.test;

import com.core.domain.User;
import com.core.enums.Role;
import com.dao.configuration.DAOConfiguration;
import com.dao.user.UserDAO;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DAOConfiguration.class)
public class UserDAOTest {

    private static final Logger logger = LogManager.getLogger("UserDAOTest logger");

    @Autowired
    private UserDAO userDAO;
    private static User user;
    private static User secondUser;

    @After
    public void deleteUserEntity() {
        logger.info("START deleteUserEntity");
        try {
            userDAO.remove(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            userDAO.remove(secondUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Before
    public void entityToPersist() {
        logger.info("START entityToPersist");
        user = userDAO.save(new User("admin", "qwerty",
                Role.ADMIN, true));
        secondUser = userDAO.save(new User("employee", "123qwerty",
                Role.EMPLOYEE, false));

        System.out.println(user.getUsername() + " - " + user.getId());
        System.out.println(secondUser.getUsername() + " - " + secondUser.getId());
    }

    @Test
    public void save() {
        logger.info("START save");
        Assert.assertEquals(user.getUsername(), "admin");
        Assert.assertEquals(user.getPassword(), "qwerty");
        Assert.assertTrue(user.isActive());
        Assert.assertEquals(user.getRole(), Role.ADMIN);
    }

    @Test
    public void findByActive() {
        logger.info("START findByActive");
        List<User> userListFromDB = userDAO.findByActive(true);
        userListFromDB.forEach(obj -> Assert.assertTrue(obj.isActive()));
    }

    @Test
    public void findByRole() {
        logger.info("START findByRole");
        List<User> userListFromDB = userDAO.findByRole(Role.ADMIN);
        userListFromDB.forEach(obj -> Assert.assertEquals(obj.getRole(), Role.ADMIN));
    }

    @Test
    public void findByUsername() {
        logger.info("START findByUsername");
        Assert.assertEquals(userDAO.findByUsername("admin"), user);
    }

    @Test
    public void find() {
        logger.info("START find");
        Assert.assertEquals(userDAO.find(user.getId()), user);
    }

    @Test
    public void findAll() {
        logger.info("START findAll");
        List<User> userListFromDB = userDAO.findAll();
        Assert.assertEquals(userListFromDB.get(userListFromDB.size() - 1), secondUser);
        Assert.assertEquals(userListFromDB.get(userListFromDB.size() - 2), user);
    }

    @Test
    public void update() {
        logger.info("START update");
        user.setPassword("admin");
        Assert.assertEquals(userDAO.update(user), user);
    }

    @Test
    public void inactivateById() {
        logger.info("START inactivateById");
        Assert.assertTrue(userDAO.inactivateById(user.getId()));
    }

    @Test
    public void activateById() {
        logger.info("START activateById");
        Assert.assertTrue(userDAO.activateById(secondUser.getId()));
    }

    @Test
    public void inactivate() {
        logger.info("START inactivate");
        Assert.assertTrue(userDAO.inactivate(user));
    }

    @Test
    public void activate() {
        logger.info("START activate");
        Assert.assertTrue(userDAO.activate(secondUser));
    }

    @Test
    public void removeById() {
        logger.info("START removeById");
        Assert.assertTrue(userDAO.removeById(secondUser.getId()));
    }

    @Test
    public void remove() {
        logger.info("START remove");
        Assert.assertTrue(userDAO.remove(user));
    }
}
