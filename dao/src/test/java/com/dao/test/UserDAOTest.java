package com.dao.test;

import com.core.domain.User;
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
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DAOConfiguration.class)
public class UserDAOTest {
    private static final Logger logger = LogManager.getLogger("UserDAOTest logger");

    @Autowired
    private UserDAO userDAO;
    private User user;
    private User secondUser;

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

        user = userDAO.save(new User("admin", "qwerty", true));
        secondUser = userDAO.save(new User("employee", "123qwerty", false));

        System.out.println("Первый пользователь" + " - " + user.getUsername());
        System.out.println("Второй пользователь" + " - " + secondUser.getUsername());
    }

    @Test
    public void save() {
        logger.info("START save");
        Assert.assertEquals(user.getUsername(), "admin");
        Assert.assertEquals(user.getPassword(), "qwerty");
        Assert.assertTrue(user.isActive());
    }

    @Test
    public void findByActive() {
        logger.info("START findByActive");
        List<User> userListFromDB = userDAO.findByActive(true);
        userListFromDB.forEach(obj -> Assert.assertTrue(obj.isActive()));
    }

    @Test
    public void find() {
        logger.info("START find");
        User tempUser = userDAO.find(user.getUsername());
        Assert.assertEquals(tempUser.getUsername(), "admin");
        Assert.assertEquals(tempUser.getPassword(), "qwerty");
        Assert.assertTrue(user.isActive());
    }

    @Test
    public void findAll() {
        logger.info("START findAll");
        List<User> users = userDAO.findAll();

        Optional<User> tempUser= users.stream().filter(obj -> obj.getUsername().equals("admin")).findFirst();
        Assert.assertEquals(tempUser.get().getUsername(), "admin");
        Assert.assertEquals(tempUser.get().getPassword(), "qwerty");
        Assert.assertTrue(tempUser.get().isActive());

        tempUser= users.stream().filter(obj -> obj.getUsername().equals("employee")).findFirst();
        Assert.assertEquals(tempUser.get().getUsername(), "employee");
        Assert.assertEquals(tempUser.get().getPassword(), "123qwerty");
        Assert.assertFalse(tempUser.get().isActive());
    }

    @Test
    public void update() {
        logger.info("START update");
        user.setPassword("admin");
        user = userDAO.update(user);
        Assert.assertEquals(user.getUsername(), "admin");
        Assert.assertEquals(user.getPassword(), "admin");
        Assert.assertTrue(user.isActive());
    }

    @Test
    public void inactivateByUsername() {
        logger.info("START inactivateByUsername");
        Assert.assertTrue(userDAO.inactivateByUsername(user.getUsername()));
    }

    @Test
    public void activateByUsername() {
        logger.info("START activateByUsername");
        Assert.assertTrue(userDAO.activateByUsername(secondUser.getUsername()));
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
    public void removeByUsername() {
        logger.info("START removeByUsername");
        Assert.assertTrue(userDAO.removeByUsername(secondUser.getUsername()));
    }

    @Test
    public void remove() {
        logger.info("START remove");
        Assert.assertTrue(userDAO.remove(user));
    }
}
