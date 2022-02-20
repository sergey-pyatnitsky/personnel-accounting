package com.service.test;

import com.core.domain.User;
import com.core.enums.Role;
import com.service.configuration.ServiceConfiguration;
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
    public void changeAuthData() {
        logger.info("START changeAuthData");
        User tempUser = userService.changeAuthData(user, "bestAdmin", "bestAdmin");
        Assert.assertEquals(tempUser.getUsername(), "bestAdmin");
        Assert.assertEquals(tempUser.getPassword(), "bestAdmin");
    }
}
