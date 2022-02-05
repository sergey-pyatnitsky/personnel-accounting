package com.dao.test;

import com.core.domain.*;
import com.core.enums.Role;
import com.dao.configuration.DAOConfiguration;
import com.dao.department.DepartmentDAO;
import com.dao.employee.EmployeeDAO;
import com.dao.employee_position.EmployeePositionDAO;
import com.dao.position.PositionDAO;
import com.dao.profile.ProfileDAO;
import com.dao.project.ProjectDAO;
import com.dao.user.UserDAO;
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
public class EmployeePositionTest {

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
    public void deleteDepartmentEntity() {
        try {
            employeePositionDAO.remove(employeePosition);
        } catch (Exception e) {
        }
        try {
            employeePositionDAO.remove(secondEmployeePosition);
        } catch (Exception e) {
        }
        try {
            employeeDAO.remove(employee);
        } catch (Exception e) {
        }
        try {
            userDAO.remove(user);
        } catch (Exception e) {
        }
        try {
            positionDAO.remove(position);
        } catch (Exception e) {
        }
        try {
            positionDAO.remove(secondPosition);
        } catch (Exception e) {
        }
        try {
            departmentDAO.remove(department);
        } catch (Exception e) {
        }
        try {
            projectDAO.remove(project);
        } catch (Exception e) {
        }
    }

    @Before
    public void entityToPersist() {
        department = departmentDAO.save(new Department("Отдел Java разработки", true));

        project = projectDAO.save(
                new Project("Банковская система", department, true));

        user = userDAO.save(new User("employee", "qwerty", Role.EMPLOYEE, false));
        Profile profile = new Profile("Инженер-программист", "г.Минск ул.Якуба Коласа 89",
                "+375294894561", "qwerty@mail.ru", "Java Python");

        employee = employeeDAO.save(new Employee("Иванов Иван Иванович", false, user,
                profileDAO.update(profile)));

        position = positionDAO.save(new Position("Разработчик"));
        secondPosition = positionDAO.update(new Position("Проектировщик"));

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
        Assert.assertEquals(employeePosition.getEmployee(), employee);
        Assert.assertEquals(employeePosition.getPosition(), position);
        Assert.assertEquals(employeePosition.getProject(), project);
        Assert.assertEquals(employeePosition.getDepartment(), department);

        Assert.assertFalse(employeePosition.isActive());
    }

    @Test
    public void findByActive() {
        List<EmployeePosition> employeePositionListFromDB = employeePositionDAO.findByActive(true);
        employeePositionListFromDB.forEach(obj -> Assert.assertFalse(obj.isActive()));
    }

    @Test
    public void findByEmployee() {
        List<EmployeePosition> employeePositionFromDB = employeePositionDAO.findByEmployee(employee);
        Assert.assertEquals(employeePositionFromDB.get(employeePositionFromDB.size() - 1), secondEmployeePosition);
        Assert.assertEquals(employeePositionFromDB.get(employeePositionFromDB.size() - 2), employeePosition);
    }

    @Test
    public void findByPosition() {
        List<EmployeePosition> employeePositionFromDB = employeePositionDAO.findByPosition(position);
        Assert.assertEquals(employeePositionFromDB.get(employeePositionFromDB.size() - 1), employeePosition);
    }

    @Test
    public void findByProject() {
        List<EmployeePosition> employeePositionFromDB = employeePositionDAO.findByProject(project);
        Assert.assertEquals(employeePositionFromDB.get(employeePositionFromDB.size() - 1), secondEmployeePosition);
        Assert.assertEquals(employeePositionFromDB.get(employeePositionFromDB.size() - 2), employeePosition);
    }

    @Test
    public void findByDepartment() {
        List<EmployeePosition> employeePositionFromDB = employeePositionDAO.findByDepartment(department);
        Assert.assertEquals(employeePositionFromDB.get(employeePositionFromDB.size() - 1), secondEmployeePosition);
        Assert.assertEquals(employeePositionFromDB.get(employeePositionFromDB.size() - 2), employeePosition);
    }

    @Test
    public void find() {
        Assert.assertEquals(employeePositionDAO.find(employeePosition.getId()), employeePosition);
    }

    @Test
    public void findAll() {
        List<EmployeePosition> employeePositionFromDB = employeePositionDAO.findAll();
        Assert.assertEquals(employeePositionFromDB.get(employeePositionFromDB.size() - 1), secondEmployeePosition);
        Assert.assertEquals(employeePositionFromDB.get(employeePositionFromDB.size() - 2), employeePosition);
    }

    @Test
    public void update() {
        employeePosition.setPosition(secondPosition);
        Assert.assertEquals(employeePositionDAO.update(employeePosition), employeePosition);
    }

    @Test
    public void removeById() {
        Assert.assertTrue(employeePositionDAO.removeById(employeePosition.getId()));
    }

    @Test
    public void remove() {
        Assert.assertTrue(employeePositionDAO.remove(employeePosition));
    }
}
