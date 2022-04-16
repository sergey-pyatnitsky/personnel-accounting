package com.personnel_accounting;

import com.personnel_accounting.domain.Profile;
import com.personnel_accounting.configuration.DAOConfiguration;
import com.personnel_accounting.profile.ProfileDAO;
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
public class ProfileDAOTest {

    private static final Logger logger = LogManager.getLogger("ProfileDAOTest logger");

    @Autowired
    private ProfileDAO profileDAO;
    private Profile profile;
    private Profile secondProfile;

    @After
    public void deleteDepartmentEntity() {
        logger.info("START deleteDepartmentEntity");
        try {
            profileDAO.remove(profile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            profileDAO.remove(secondProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Before
    public void entityToPersist() {
        logger.info("START entityToPersist");
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
        logger.info("START save");
        Assert.assertEquals(profile.getEducation(), "Инженер-программист");
        Assert.assertEquals(profile.getAddress(), "г.Минск ул.Якуба Коласа 89");
        Assert.assertEquals(profile.getPhone(), "+375294894561");
        Assert.assertEquals(profile.getEmail(), "qwerty@mail.ru");
        Assert.assertEquals(profile.getSkills(), "Java Python");
    }

    @Test
    public void findAll() {
        logger.info("START findAll");
        List<Profile> profileListFromDB = profileDAO.findAll();
        Assert.assertEquals(profileListFromDB.get(profileListFromDB.size() - 1), secondProfile);
        Assert.assertEquals(profileListFromDB.get(profileListFromDB.size() - 2), profile);
    }

    @Test
    public void find() {
        logger.info("START find");
        Assert.assertEquals(profileDAO.find(profile.getId()), profile);
    }

    @Test
    public void findByEducation() {
        logger.info("START findByEducation");
        List<Profile> profileListFromDB = profileDAO.findByEducation("Инженер-программист");
        profileListFromDB.forEach(obj -> Assert.assertEquals(obj.getEducation(), "Инженер-программист"));
    }

    @Test
    public void findByAddress() {
        logger.info("START findByAddress");
        Assert.assertEquals(profileDAO.findByAddress("г.Минск ул.Якуба Коласа 89").getAddress(),
                "г.Минск ул.Якуба Коласа 89");
    }

    @Test
    public void findByPhone() {
        logger.info("START findByPhone");
        Assert.assertEquals(profileDAO.findByPhone("+375294894561").getPhone(),
                "+375294894561");
    }

    @Test
    public void findByEmail() {
        logger.info("START findByEmail");
        Assert.assertEquals(profileDAO.findByEmail("qwerty@mail.ru").getEmail(),
                "qwerty@mail.ru");
    }

    @Test
    public void update() {
        logger.info("START update");
        profile.setEmail("qwerty123@mail.ru");
        Assert.assertEquals(profileDAO.merge(profile), profile);
    }

    @Test
    public void removeById() {
        logger.info("START removeById");
        Assert.assertTrue(profileDAO.removeById(secondProfile.getId()));
    }

    @Test
    public void remove() {
        logger.info("START remove");
        Assert.assertTrue(profileDAO.remove(profile));
    }
}
