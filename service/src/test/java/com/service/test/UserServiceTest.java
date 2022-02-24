package com.service.test;

import com.core.domain.Employee;
import com.core.domain.User;
import com.core.enums.Role;
import com.dao.authority.AuthorityDAO;
import com.service.configuration.ServiceConfiguration;
import com.service.employee.EmployeeService;
import com.service.user.UserService;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServiceConfiguration.class)
public class UserServiceTest {

    private static final Logger logger = LogManager.getLogger("UserDAOTest logger");

    @Autowired
    private UserService userService;
    private User user;
    private User secondUser;

    @Autowired
    private AuthorityDAO authorityDAO;

    @Autowired
    private EmployeeService employeeService;

    @After
    public void deleteUserEntity() {
        logger.info("START deleteUserEntity");
        try {
            userService.remove(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            userService.remove(secondUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Before
    public void entityToPersist() {
        logger.info("START entityToPersist");
        user = userService.save(new User("admin", "qwerty", true), Role.ADMIN);
        secondUser = userService.save(new User("employee", "123qwerty", false), Role.EMPLOYEE);

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
    public void changeAuthData() {
        logger.info("START changeAuthData");
        user = userService.changeAuthData(user, "bestAdminPass");
        Assert.assertEquals(user.getUsername(), "admin");
        Assert.assertEquals(user.getPassword(), "bestAdminPass");

        user = userService.find(user.getUsername());
        Assert.assertEquals(user.getUsername(), "admin");
        Assert.assertEquals(user.getPassword(), "bestAdminPass");
        Assert.assertTrue(user.isActive());
    }

    @Test
    public void changeUserRole() {
        logger.info("START changeUserRole");
        user = userService.changeUserRole(user, Role.EMPLOYEE);

        Assert.assertEquals(user.getUsername(), "admin");
        Assert.assertEquals(user.getPassword(), "qwerty");
        Assert.assertTrue(user.isActive());

        Assert.assertEquals(authorityDAO.find("admin").getRole(), Role.EMPLOYEE);

        user = userService.find(user.getUsername());
        Assert.assertEquals(user.getUsername(), "admin");
        Assert.assertEquals(user.getPassword(), "qwerty");
        Assert.assertTrue(user.isActive());
    }

    @Test
    public void registerUser() {
        logger.info("START registerUser");
        userService.remove(secondUser);
        secondUser = userService.registerUser(secondUser, "Иванов Иван Иванович", Role.EMPLOYEE);

        Assert.assertEquals(secondUser.getUsername(), "employee");
        Assert.assertEquals(secondUser.getPassword(), "123qwerty");
        Assert.assertFalse(secondUser.isActive());

        secondUser = userService.find(secondUser.getUsername());
        Assert.assertEquals(secondUser.getUsername(), "employee");
        Assert.assertEquals(secondUser.getPassword(), "123qwerty");
        Assert.assertFalse(secondUser.isActive());

        Employee employee = employeeService.findByUser(secondUser);
        Assert.assertEquals(employee.getName(), "Иванов Иван Иванович");

        employeeService.remove(employee);
    }
}
