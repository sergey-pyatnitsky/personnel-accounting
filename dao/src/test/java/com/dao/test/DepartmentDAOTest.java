package com.dao.test;

import com.core.domain.Department;
import com.dao.configuration.ApplicationConfiguration;
import com.dao.department.DepartmentDAO;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@Transactional
public class DepartmentDAOTest {

    @Autowired
    private DepartmentDAO departmentDAO;
    private static Department department;
    private static Department secondDepartment;

    @BeforeClass
    public static void initDepartmentEntity() {
        Date date = new Date(2022, 12, 15);
        department = new Department("Отдел Java разработки", true, date);
    }

    @Test
    public void create() {
        department = departmentDAO.create(department);
        Assert.assertEquals(department.getName(), "Отдел Java разработки");
        Assert.assertTrue(department.isActive());
        Assert.assertEquals(department.getStartDate(), new Date(2022, 12, 15));

        secondDepartment = departmentDAO.create(new Department("Отдел Python разработки",
                true, new Date(System.currentTimeMillis())));
    }

    @Test
    public void findByActive() {
        departmentDAO.create(department);
        List<Department> departmentListFromDB = departmentDAO.findByActive(true);
        departmentListFromDB.forEach(obj -> Assert.assertTrue(obj.isActive()));
    }

    @Test
    public void findByName() {
        departmentDAO.create(secondDepartment);
        Assert.assertEquals(departmentDAO.findByName("Отдел Python разработки").getName(),
                secondDepartment.getName());
    }

    @Test
    public void find() {
        departmentDAO.create(department);
        Assert.assertEquals(departmentDAO.find(department.getId()), department);
    }

    @Test
    public void findAll() {
        departmentDAO.create(department);
        departmentDAO.create(secondDepartment);
        List<Department> departmentListFromDB = departmentDAO.findAll();
        Assert.assertEquals(departmentListFromDB.get(departmentListFromDB.size() - 1), secondDepartment);
        Assert.assertEquals(departmentListFromDB.get(departmentListFromDB.size() - 2), department);
    }

    @Test
    public void update() {
        departmentDAO.create(department);
        department.setName("Отдел PHP разработки");
        Assert.assertEquals(departmentDAO.update(department), department);
    }

    @Test
    public void inactivateById() {
        departmentDAO.create(department);
        Assert.assertTrue(departmentDAO.inactivateById(department.getId()));
    }

    @Test
    public void activateById() {
        department.setActive(false);
        departmentDAO.create(department);
        Assert.assertTrue(departmentDAO.activateById(department.getId()));
    }

    @Test
    public void inactivate() {
        department.setActive(true);
        departmentDAO.create(department);
        Assert.assertTrue(departmentDAO.inactivate(department));
    }

    @Test
    public void activate() {
        departmentDAO.create(department);
        Assert.assertTrue(departmentDAO.activate(department));
    }

    @Test
    public void removeById() {
        Assert.assertTrue(departmentDAO.removeById(secondDepartment.getId()));
    }

    @Test
    public void remove() {
        Assert.assertTrue(departmentDAO.remove(department));
    }
}
