package com.personnel_accounting;

import com.personnel_accounting.configuration.DAOTestConfiguration;
import com.personnel_accounting.department.DepartmentDAO;
import com.personnel_accounting.domain.Department;
import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.domain.EmployeePosition;
import com.personnel_accounting.domain.Position;
import com.personnel_accounting.domain.Profile;
import com.personnel_accounting.domain.Project;
import com.personnel_accounting.domain.User;
import com.personnel_accounting.employee.EmployeeDAO;
import com.personnel_accounting.employee_position.EmployeePositionDAO;
import com.personnel_accounting.position.PositionDAO;
import com.personnel_accounting.profile.ProfileDAO;
import com.personnel_accounting.project.ProjectDAO;
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

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DAOTestConfiguration.class)
public class EmployeePositionDAOTest {

    private static final Logger logger = LogManager.getLogger("EmployeePositionDAOTest logger");

    @Autowired
    private EmployeePositionDAO employeePositionDAO;
    private EmployeePosition employeePosition;
    private EmployeePosition secondEmployeePosition;

    @Autowired
    private DepartmentDAO departmentDAO;
    private Department department;

    @Autowired
    private UserDAO userDAO;
    private User user;

    @Autowired
    private ProfileDAO profileDAO;

    @Autowired
    private PositionDAO positionDAO;
    private Position position;
    private Position secondPosition;

    @Autowired
    private ProjectDAO projectDAO;
    private Project project;

    @Autowired
    private EmployeeDAO employeeDAO;
    private Employee employee;

    @After
    public void deleteEntity() {
        logger.info("START deleteDepartmentEntity");
        try {
            employeePositionDAO.remove(employeePosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            employeePositionDAO.remove(secondEmployeePosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            employeeDAO.remove(employee);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            userDAO.remove(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        try {
            projectDAO.remove(project);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            departmentDAO.remove(department);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Before
    public void entityToPersist() {
        logger.info("START entityToPersist");
        department = departmentDAO.save(new Department("Отдел Java разработки", true));

        project = projectDAO.save(
                new Project("Банковская система", department, true));

        user = userDAO.save(new User("employee", "qwerty", false));
        Profile profile = new Profile("Инженер-программист", "г.Минск ул.Якуба Коласа 89",
                "+375294894561", "qwerty@mail.ru", "Java Python");

        employee = employeeDAO.save(new Employee("Иванов Иван Иванович", false, user,
                profileDAO.save(profile)));

        position = positionDAO.save(new Position("Разработчик"));
        secondPosition = positionDAO.save(new Position("Проектировщик"));

        employeePosition = employeePositionDAO.save(
                new EmployeePosition(false, employee, position, project, department));

        secondEmployeePosition = employeePositionDAO.save(
                new EmployeePosition(false, employee, secondPosition, project, department));

        System.out.println(employeePosition.getPosition().getName() + " - " + employeePosition.getId());
        System.out.println(secondEmployeePosition.getPosition().getName() +
                " - " + secondEmployeePosition.getId());
    }

    @Test
    public void save() {
        logger.info("START save");
        Assert.assertEquals(employeePosition.getEmployee(), employee);
        Assert.assertEquals(employeePosition.getPosition(), position);
        Assert.assertEquals(employeePosition.getProject(), project);
        Assert.assertEquals(employeePosition.getDepartment(), department);

        Assert.assertFalse(employeePosition.isActive());
    }

    @Test
    public void saveList() {
        logger.info("START saveList");

        try {
            employeePositionDAO.remove(employeePosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            employeePositionDAO.remove(secondEmployeePosition);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<EmployeePosition> employeePositions = new ArrayList<>();
        employeePositions.add(new EmployeePosition(false, employee, position, project, department));
        employeePositions.add(new EmployeePosition(false, employee, secondPosition, project, department));

        employeePositions = employeePositionDAO.save(employeePositions);

        secondEmployeePosition = employeePositions.get(employeePositions.size() - 1);

        Assert.assertEquals(secondEmployeePosition.getEmployee(), employee);
        Assert.assertEquals(secondEmployeePosition.getPosition(), secondPosition);
        Assert.assertEquals(secondEmployeePosition.getProject(), project);
        Assert.assertEquals(secondEmployeePosition.getDepartment(), department);
        Assert.assertFalse(secondEmployeePosition.isActive());

        employeePosition = employeePositions.get(employeePositions.size() - 2);
        Assert.assertEquals(employeePosition.getEmployee(), employee);
        Assert.assertEquals(employeePosition.getPosition(), position);
        Assert.assertEquals(employeePosition.getProject(), project);
        Assert.assertEquals(employeePosition.getDepartment(), department);
        Assert.assertFalse(employeePosition.isActive());
    }

    @Test
    public void findByActive() {
        logger.info("START findByActive");
        List<EmployeePosition> employeePositionListFromDB = employeePositionDAO.findByActive(true);
        employeePositionListFromDB.forEach(obj -> Assert.assertFalse(obj.isActive()));
    }

    @Test
    public void findByEmployee() {
        logger.info("START findByEmployee");
        List<EmployeePosition> employeePositionFromDB = employeePositionDAO.findByEmployee(employee);

        EmployeePosition tempEmployeePosition = employeePositionFromDB.get(employeePositionFromDB.size() - 1);
        Assert.assertEquals(tempEmployeePosition.getId(), secondEmployeePosition.getId());
        Assert.assertEquals(tempEmployeePosition.getCreateDate().toString(),
                secondEmployeePosition.getCreateDate().toString());

        tempEmployeePosition = employeePositionFromDB.get(employeePositionFromDB.size() - 2);
        Assert.assertEquals(tempEmployeePosition.getId(), employeePosition.getId());
        Assert.assertEquals(tempEmployeePosition.getCreateDate().toString(),
                secondEmployeePosition.getCreateDate().toString());
    }

    @Test
    public void findByPosition() {
        logger.info("START findByPosition");
        List<EmployeePosition> employeePositionFromDB = employeePositionDAO.findByPosition(position);
        EmployeePosition tempEmployeePosition = employeePositionFromDB.get(employeePositionFromDB.size() - 1);
        Assert.assertEquals(tempEmployeePosition.getId(), employeePosition.getId());
        Assert.assertEquals(tempEmployeePosition.getCreateDate().toString(),
                employeePosition.getCreateDate().toString());
    }

    @Test
    public void findByProject() {
        logger.info("START findByProject");
        List<EmployeePosition> employeePositionFromDB = employeePositionDAO.findByProject(project);
        EmployeePosition tempEmployeePosition = employeePositionFromDB.get(employeePositionFromDB.size() - 1);
        Assert.assertEquals(tempEmployeePosition.getId(), secondEmployeePosition.getId());
        Assert.assertEquals(tempEmployeePosition.getCreateDate().toString(),
                secondEmployeePosition.getCreateDate().toString());

        tempEmployeePosition = employeePositionFromDB.get(employeePositionFromDB.size() - 2);
        Assert.assertEquals(tempEmployeePosition.getId(), employeePosition.getId());
        Assert.assertEquals(tempEmployeePosition.getCreateDate().toString(),
                secondEmployeePosition.getCreateDate().toString());
    }

    @Test
    public void findByDepartment() {
        logger.info("START findByDepartment");
        List<EmployeePosition> employeePositionFromDB = employeePositionDAO.findByDepartment(department);
        EmployeePosition tempEmployeePosition = employeePositionFromDB.get(employeePositionFromDB.size() - 1);
        Assert.assertEquals(tempEmployeePosition.getId(), secondEmployeePosition.getId());
        Assert.assertEquals(tempEmployeePosition.getCreateDate().toString(),
                secondEmployeePosition.getCreateDate().toString());

        tempEmployeePosition = employeePositionFromDB.get(employeePositionFromDB.size() - 2);
        Assert.assertEquals(tempEmployeePosition.getId(), employeePosition.getId());
        Assert.assertEquals(tempEmployeePosition.getCreateDate().toString(),
                secondEmployeePosition.getCreateDate().toString());
    }

    @Test
    public void find() {
        logger.info("START find");
        EmployeePosition tempEmployeePosition = employeePositionDAO.find(employeePosition.getId());
        Assert.assertEquals(tempEmployeePosition.getId(), employeePosition.getId());
        Assert.assertEquals(tempEmployeePosition.getCreateDate().toString(),
                employeePosition.getCreateDate().toString());
    }

    @Test
    public void findAll() {
        logger.info("START findAll");
        List<EmployeePosition> employeePositionFromDB = employeePositionDAO.findAll();
        EmployeePosition tempEmployeePosition = employeePositionFromDB.get(employeePositionFromDB.size() - 1);
        Assert.assertEquals(tempEmployeePosition.getId(), secondEmployeePosition.getId());
        Assert.assertEquals(tempEmployeePosition.getCreateDate().toString(),
                secondEmployeePosition.getCreateDate().toString());

        tempEmployeePosition = employeePositionFromDB.get(employeePositionFromDB.size() - 2);
        Assert.assertEquals(tempEmployeePosition.getId(), employeePosition.getId());
        Assert.assertEquals(tempEmployeePosition.getCreateDate().toString(),
                secondEmployeePosition.getCreateDate().toString());
    }

    @Test
    public void merge() {
        logger.info("START merge");
        employeePosition.setPosition(secondPosition);
        EmployeePosition tempEmployeePosition = employeePositionDAO.merge(employeePosition);
        Assert.assertEquals(tempEmployeePosition.getId(), employeePosition.getId());
        Assert.assertEquals(tempEmployeePosition.getCreateDate().toString(),
                employeePosition.getCreateDate().toString());
    }

    @Test
    public void inactivate() {
        logger.info("START inactivate");
        employeePosition.setActive(true);
        Assert.assertTrue(employeePositionDAO.inactivate(employeePosition));
        Assert.assertFalse(employeePositionDAO.find(employeePosition.getId()).isActive());
    }

    @Test
    public void activate() {
        logger.info("START activate");
        employeePosition.setActive(false);
        Assert.assertTrue(employeePositionDAO.activate(employeePosition));
        Assert.assertTrue(employeePositionDAO.find(employeePosition.getId()).isActive());
    }

    @Test
    public void removeById() {
        logger.info("START removeById");
        Assert.assertTrue(employeePositionDAO.removeById(employeePosition.getId()));
    }

    @Test
    public void remove() {
        logger.info("START remove");
        Assert.assertTrue(employeePositionDAO.remove(employeePosition));
    }
}
