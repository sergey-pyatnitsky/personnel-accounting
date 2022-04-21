package com.personnel_accounting;

import com.personnel_accounting.configuration.DAOTestConfiguration;
import com.personnel_accounting.domain.Position;
import com.personnel_accounting.position.PositionDAO;
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
@ContextConfiguration(classes = DAOTestConfiguration.class)
public class PositionDAOTest {

    private static final Logger logger = LogManager.getLogger("PositionDAOTest logger");

    @Autowired
    private PositionDAO positionDAO;
    private Position position;
    private Position secondPosition;

    @After
    public void deleteUserEntity() {
        logger.info("START deleteUserEntity");
        try {
            positionDAO.remove(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            positionDAO.remove(secondPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Before
    public void entityToPersist() {
        logger.info("START entityToPersist");
        position = positionDAO.save(new Position("Проектировщик"));
        secondPosition = positionDAO.save(new Position("Проектировщик БД"));

        System.out.println(position.getName() + " - " + position.getId());
        System.out.println(secondPosition.getName() + " - " + secondPosition.getId());
    }

    @Test
    public void save() {
        logger.info("START save");
        Assert.assertEquals(position.getName(), "Проектировщик");
    }

    @Test
    public void findByName() {
        logger.info("START findByName");
        Assert.assertEquals(positionDAO.findByName("Проектировщик"), position);
    }

    @Test
    public void find() {
        logger.info("START find");
        Assert.assertEquals(positionDAO.find(position.getId()), position);
    }

    @Test
    public void findAll() {
        logger.info("START findAll");
        List<Position> positionListFromDB = positionDAO.findAll();
        Assert.assertEquals(positionListFromDB.get(positionListFromDB.size() - 1), secondPosition);
        Assert.assertEquals(positionListFromDB.get(positionListFromDB.size() - 2), position);
    }

    @Test
    public void merge() {
        logger.info("START merge");
        position.setName("Проеткировщик архитектуры");
        Assert.assertEquals(positionDAO.merge(position), position);
    }

    @Test
    public void removeById() {
        logger.info("START removeById");
        Assert.assertTrue(positionDAO.removeById(position.getId()));
    }

    @Test
    public void remove() {
        logger.info("START remove");
        Assert.assertTrue(positionDAO.remove(position));
    }
}
