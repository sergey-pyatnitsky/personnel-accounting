package com.personnel_accounting;

import com.personnel_accounting.domain.Authority;
import com.personnel_accounting.domain.User;
import com.personnel_accounting.enums.Role;
import com.personnel_accounting.authority.AuthorityDAO;
import com.personnel_accounting.configuration.DAOConfiguration;
import com.personnel_accounting.user.UserDAO;
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
public class AuthorityDAOTest {
    private static final Logger logger = LogManager.getLogger("AuthorityDAOTest logger");

    @Autowired
    private UserDAO userDAO;
    private User user;
    private User secondUser;

    @Autowired
    private AuthorityDAO authorityDAO;
    private Authority authority;
    private Authority secondAuthority;

    @After
    public void deleteUserEntity() {
        logger.info("START deleteUserEntity");
        try {
            authorityDAO.remove(authority);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            authorityDAO.remove(secondAuthority);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        authority = authorityDAO.save(new Authority("admin", Role.ADMIN));
        secondAuthority = authorityDAO.save(new Authority("employee", Role.EMPLOYEE));

        System.out.println("Первый пользователь" + " - " + user.getUsername() + " " + authority.getRole());
        System.out.println("Второй пользователь" + " - " + secondUser.getUsername() + " " + secondAuthority.getRole());
    }

    @Test
    public void save(){
        logger.info("START save");
        Assert.assertEquals(authority.getUsername(), "admin");
        Assert.assertEquals(authority.getRole(), Role.ADMIN);
    }

    @Test
    public void find(){
        logger.info("START find");
        Assert.assertEquals(authorityDAO.find(authority.getUsername()), authority);
    }

    @Test
    public void findAll(){
        logger.info("START findAll");
        List<Authority> authorityListFromDB = authorityDAO.findAll();
        authorityListFromDB.forEach(obj -> {
            if (obj.getUsername().equals("admin")) Assert.assertEquals(obj, authority);
            else if (obj.getUsername().equals("employee")) Assert.assertEquals(obj, secondAuthority);
        });
    }

    @Test
    public void findByRole(){
        logger.info("START findByRole");
        List<Authority> authorityListFromDB = authorityDAO.findByRole(Role.ADMIN);
        authorityListFromDB.forEach(obj -> {
            if (obj.getUsername().equals("admin")) Assert.assertEquals(obj, authority);
        });
    }

    @Test
    public void update(){
        logger.info("START update");
        authority.setRole(Role.SUPER_ADMIN);
        Assert.assertEquals(authorityDAO.merge(authority), authority);
    }

    @Test
    public void removeByUsername(){
        logger.info("START removeByUsername");
        Assert.assertTrue(authorityDAO.removeByUsername(authority.getUsername()));
    }

    @Test
    public void remove(){
        logger.info("START remove");
        Assert.assertTrue(authorityDAO.remove(authority));
    }
}
