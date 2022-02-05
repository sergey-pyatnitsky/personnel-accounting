package com.dao.test;

import com.core.domain.Department;
import com.dao.configuration.DAOConfiguration;
import com.dao.department.DepartmentDAO;
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
public class DepartmentDAOTest {

    @Autowired
    private DepartmentDAO departmentDAO;
    private Department department;
    private Department secondDepartment;

    @After
    public void deleteDepartmentEntity() {
        try {
            departmentDAO.remove(department);
        } catch (Exception e) {
        }
        try {
            departmentDAO.remove(secondDepartment);
        } catch (Exception e) {
        }
    }

    @Before
    public void entityToPersist() {
        department = departmentDAO.update(new Department("Отдел Java разработки", true));
        secondDepartment = departmentDAO.update(new Department("Отдел Python разработки", true));

        System.out.println(department.getName() + " - " + department.getId());
        System.out.println(secondDepartment.getName() + " - " + secondDepartment.getId());
    }

    @Test
    public void save() {
        Assert.assertEquals(department.getName(), "Отдел Java разработки");
        Assert.assertTrue(department.isActive());
    }

    @Test
    public void findByActive() {
        List<Department> departmentListFromDB = departmentDAO.findByActive(true);
        departmentListFromDB.forEach(obj -> Assert.assertTrue(obj.isActive()));
    }

    @Test
    public void findByName() {
        Assert.assertEquals(departmentDAO.findByName("Отдел Python разработки"), secondDepartment);
    }

    @Test
    public void find() {
        Assert.assertEquals(departmentDAO.find(department.getId()), department);
    }

    @Test
    public void findAll() {
        List<Department> departmentListFromDB = departmentDAO.findAll();
        Assert.assertEquals(departmentListFromDB.get(departmentListFromDB.size() - 1), secondDepartment);
        Assert.assertEquals(departmentListFromDB.get(departmentListFromDB.size() - 2), department);
    }

    @Test
    public void update() {
        department.setName("Отдел PHP разработки");
        Assert.assertEquals(departmentDAO.update(department), department);
    }

    @Test
    public void inactivateById() {
        Assert.assertTrue(departmentDAO.inactivateById(department.getId()));
    }

    @Test
    public void activateById() {
        department.setActive(false);
        Assert.assertTrue(departmentDAO.activateById(department.getId()));
    }

    @Test
    public void inactivate() {
        Assert.assertTrue(departmentDAO.inactivate(department));
    }

    @Test
    public void activate() {
        department.setActive(false);
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
