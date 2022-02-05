package com.dao.test;

import com.core.domain.Position;
import com.dao.configuration.DAOConfiguration;
import com.dao.position.PositionDAO;
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
public class PositionDAOTest {

    @Autowired
    private PositionDAO positionDAO;
    private Position position;
    private Position secondPosition;

    @After
    public void deleteUserEntity() {
        try {
            positionDAO.remove(position);
        } catch (Exception e) {
        }
        try {
            positionDAO.remove(secondPosition);
        } catch (Exception e) {
        }
    }

    @Before
    public void entityToPersist() {
        position = positionDAO.save(new Position("Проектировщик"));
        secondPosition = positionDAO.save(new Position("Проектировщик БД"));

        System.out.println(position.getName() + " - " + position.getId());
        System.out.println(secondPosition.getName() + " - " + secondPosition.getId());
    }

    @Test
    public void save() {
        Assert.assertEquals(position.getName(), "Проектировщик");
    }

    @Test
    public void findByName() {
        Assert.assertEquals(positionDAO.findByName("Проектировщик"), position);
    }

    @Test
    public void find() {
        Assert.assertEquals(positionDAO.find(position.getId()), position);
    }

    @Test
    public void findAll() {
        List<Position> positionListFromDB = positionDAO.findAll();
        Assert.assertEquals(positionListFromDB.get(positionListFromDB.size() - 1), secondPosition);
        Assert.assertEquals(positionListFromDB.get(positionListFromDB.size() - 2), position);
    }

    @Test
    public void update() {
        position.setName("Проеткировщик архитектуры");
        Assert.assertEquals(positionDAO.update(position), position);
    }

    @Test
    public void removeById() {
        Assert.assertTrue(positionDAO.removeById(position.getId()));
    }

    @Test
    public void remove() {
        Assert.assertTrue(positionDAO.remove(position));
    }
}
