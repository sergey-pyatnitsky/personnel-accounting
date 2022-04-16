package com.personnel_accounting;

import com.personnel_accounting.domain.Department;
import com.personnel_accounting.configuration.DAOConfiguration;
import com.personnel_accounting.department.DepartmentDAO;
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

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DAOConfiguration.class)
public class DepartmentDAOTest {

    private static final Logger logger = LogManager.getLogger("DepartmentDAOTest logger");

    @Autowired
    private DepartmentDAO departmentDAO;
    private Department department;
    private Department secondDepartment;

    @After
    public void deleteDepartmentEntity() {
        logger.info("START deleteDepartmentEntity");
        try {
            departmentDAO.remove(department);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            departmentDAO.remove(secondDepartment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Before
    public void entityToPersist() {
        logger.info("START entityToPersist");
        department = departmentDAO.save(new Department("Отдел Java разработки", true));
        secondDepartment = departmentDAO.save(new Department("Отдел Python разработки", true));

        System.out.println(department.getName() + " - " + department.getId());
        System.out.println(secondDepartment.getName() + " - " + secondDepartment.getId());
    }

    @Test
    public void save() {
        logger.info("START save");
        Assert.assertEquals(department.getName(), "Отдел Java разработки");
        Assert.assertTrue(department.isActive());
    }

    @Test
    public void saveList(){
        logger.info("START saveList");
        deleteDepartmentEntity();

        List<Department> departments = new ArrayList<>();
        departments.add(new Department("Отдел Java разработки", true));
        departments.add(new Department("Отдел Python разработки", true));

        departments = departmentDAO.save(departments);

        Department tempDepartment = departments.get(departments.size() - 1);
        Assert.assertEquals("Отдел Python разработки", tempDepartment.getName());
        Assert.assertTrue(tempDepartment.isActive());
        try {
            departmentDAO.remove(tempDepartment);
        } catch (Exception e) {
            e.printStackTrace();
        }

        tempDepartment = departments.get(departments.size() - 2);
        Assert.assertEquals("Отдел Java разработки", tempDepartment.getName());
        Assert.assertTrue(tempDepartment.isActive());
        try {
            departmentDAO.remove(tempDepartment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findByActive() {
        logger.info("START findByActive");
        List<Department> departmentListFromDB = departmentDAO.findByActive(true);
        departmentListFromDB.forEach(obj -> Assert.assertTrue(obj.isActive()));
    }

    //FIXME
    /*@Test
    public void findByName() {
        logger.info("START findByName");
        Assert.assertEquals(departmentDAO.findByName("Отдел Python разработки").getName(),
                "Отдел Python разработки");
    }*/

    @Test
    public void find() {
        logger.info("START find");
        Assert.assertEquals(departmentDAO.find(department.getId()).getName(), "Отдел Java разработки");
        Assert.assertTrue(departmentDAO.find(department.getId()).isActive());
    }

    /*@Test
    public void findAll() {
        logger.info("START findAll");
        List<Department> departmentListFromDB = departmentDAO.findAll();

        Assert.assertEquals(departmentListFromDB.get(departmentListFromDB.size() - 2).getName(),
                "Отдел Java разработки");
        Assert.assertTrue(departmentListFromDB.get(departmentListFromDB.size() - 2).isActive());

        Assert.assertEquals(departmentListFromDB.get(departmentListFromDB.size() - 1).getName(),
                "Отдел Python разработки");
        Assert.assertTrue(departmentListFromDB.get(departmentListFromDB.size() - 1).isActive());
    }*/

    @Test
    public void update() {
        logger.info("START update");
        department.setName("Отдел PHP разработки");
        Assert.assertEquals(departmentDAO.merge(department), department);
    }

    @Test
    public void inactivateById() {
        logger.info("START inactivateById");
        Assert.assertTrue(departmentDAO.inactivateById(department.getId()));
    }

    @Test
    public void activateById() {
        logger.info("START activateById");
        department.setActive(false);
        Assert.assertTrue(departmentDAO.activateById(department.getId()));
    }

    @Test
    public void inactivate() {
        logger.info("START inactivate");
        Assert.assertTrue(departmentDAO.inactivate(department));
    }

    @Test
    public void activate() {
        logger.info("START activate");
        department.setActive(false);
        Assert.assertTrue(departmentDAO.activate(department));
    }

    @Test
    public void removeById() {
        logger.info("START removeById");
        Assert.assertTrue(departmentDAO.removeById(secondDepartment.getId()));
    }

    @Test
    public void remove() {
        logger.info("START remove");
        Assert.assertTrue(departmentDAO.remove(department));
    }
}
