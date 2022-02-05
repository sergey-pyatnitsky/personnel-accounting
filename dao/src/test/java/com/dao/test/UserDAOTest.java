package com.dao.test;

import com.core.domain.User;
import com.core.enums.Role;
import com.dao.configuration.DAOConfiguration;
import com.dao.user.UserDAO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DAOConfiguration.class)
@Transactional
public class UserDAOTest {

    @Autowired
    private UserDAO userDAO;
    private static User user;
    private static User secondUser;

    @After
    public void deleteUserEntity() {
        try {
            userDAO.remove(user);
        } catch (Exception e) {
        }
        try {
            userDAO.remove(secondUser);
        } catch (Exception e) {
        }
    }

    @Before
    public void entityToPersist() {
        user = userDAO.update(new User("admin", "qwerty",
                Role.ADMIN, true));
        secondUser = userDAO.update(new User("employee", "123qwerty",
                Role.EMPLOYEE, false));

        System.out.println(user.getUsername() + " - " + user.getId());
        System.out.println(secondUser.getUsername() + " - " + secondUser.getId());
    }

    @Test
    public void save() {
        Assert.assertEquals(user.getUsername(), "admin");
        Assert.assertEquals(user.getPassword(), "qwerty");
        Assert.assertTrue(user.isActive());
        Assert.assertEquals(user.getRole(), Role.ADMIN);
    }

    @Test
    public void findByActive() {
        List<User> userListFromDB = userDAO.findByActive(true);
        userListFromDB.forEach(obj -> Assert.assertTrue(obj.isActive()));
    }

    @Test
    public void findByRole() {
        List<User> userListFromDB = userDAO.findByRole(Role.ADMIN);
        userListFromDB.forEach(obj -> Assert.assertEquals(obj.getRole(), Role.ADMIN));
    }

    @Test
    public void findByUsername() {
        Assert.assertEquals(userDAO.findByUsername("admin"), user);
    }

    @Test
    public void find() {
        Assert.assertEquals(userDAO.find(user.getId()), user);
    }

    @Test
    public void findAll() {
        List<User> userListFromDB = userDAO.findAll();
        Assert.assertEquals(userListFromDB.get(userListFromDB.size() - 1), secondUser);
        Assert.assertEquals(userListFromDB.get(userListFromDB.size() - 2), user);
    }

    @Test
    public void update() {
        user.setPassword("admin");
        Assert.assertEquals(userDAO.update(user), user);
    }

    @Test
    public void inactivateById() {
        Assert.assertTrue(userDAO.inactivateById(user.getId()));
    }

    @Test
    public void activateById() {
        Assert.assertTrue(userDAO.activateById(secondUser.getId()));
    }

    @Test
    public void inactivate() {
        Assert.assertTrue(userDAO.inactivate(user));
    }

    @Test
    public void activate() {
        Assert.assertTrue(userDAO.activate(secondUser));
    }

    @Test
    public void removeById() {
        Assert.assertTrue(userDAO.removeById(secondUser.getId()));
    }

    @Test
    public void remove() {
        Assert.assertTrue(userDAO.remove(user));
    }
}
