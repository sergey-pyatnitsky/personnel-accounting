package com.dao.test;

import com.core.domain.Profile;
import com.dao.configuration.DAOConfiguration;
import com.dao.profile.ProfileDAO;
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
public class ProfileDAOTest {

    @Autowired
    private ProfileDAO profileDAO;
    private Profile profile;
    private Profile secondProfile;

    @After
    public void deleteDepartmentEntity() {
        try {
            profileDAO.remove(profile);
        } catch (Exception e) {
        }
        try {
            profileDAO.remove(secondProfile);
        } catch (Exception e) {
        }
    }

    @Before
    public void entityToPersist() {
        profile = profileDAO.save(new Profile("Инженер-программист", "г.Минск ул.Якуба Коласа 89",
                "+375294894561", "qwerty@mail.ru", "Java Python"));
        secondProfile = profileDAO.save(new Profile("Инженер-программист-экономист",
                "г.Минск пр-т.Независимости 89", "+375294564561",
                "qwertyqwe@mail.ru", "Java"));

        System.out.println("Профиль - " + profile.getId());
        System.out.println("Профиль - " + secondProfile.getId());
    }

    @Test
    public void save() {
        Assert.assertEquals(profile.getEducation(), "Инженер-программист");
        Assert.assertEquals(profile.getAddress(), "г.Минск ул.Якуба Коласа 89");
        Assert.assertEquals(profile.getPhone(), "+375294894561");
        Assert.assertEquals(profile.getEmail(), "qwerty@mail.ru");
        Assert.assertEquals(profile.getSkills(), "Java Python");
    }

    @Test
    public void findAll() {
        List<Profile> profileListFromDB = profileDAO.findAll();
        Assert.assertEquals(profileListFromDB.get(profileListFromDB.size() - 1), secondProfile);
        Assert.assertEquals(profileListFromDB.get(profileListFromDB.size() - 2), profile);
    }

    @Test
    public void find() {
        Assert.assertEquals(profileDAO.find(profile.getId()), profile);
    }

    @Test
    public void findByEducation() {
        List<Profile> profileListFromDB = profileDAO.findByEducation("Инженер-программист");
        profileListFromDB.forEach(obj -> Assert.assertEquals(obj.getEducation(), "Инженер-программист"));
    }

    @Test
    public void findByAddress() {
        Assert.assertEquals(profileDAO.findByAddress("г.Минск ул.Якуба Коласа 89"), profile);
    }

    @Test
    public void findByPhone() {
        Assert.assertEquals(profileDAO.findByPhone("+375294894561"), profile);
    }

    @Test
    public void findByEmail() {
        Assert.assertEquals(profileDAO.findByEmail("qwerty@mail.ru"), profile);
    }

    @Test
    public void update() {
        profile.setEmail("qwerty123@mail.ru");
        Assert.assertEquals(profileDAO.update(profile), profile);
    }

    @Test
    public void removeById() {
        Assert.assertTrue(profileDAO.removeById(secondProfile.getId()));
    }

    @Test
    public void remove() {
        Assert.assertTrue(profileDAO.remove(profile));
    }
}
