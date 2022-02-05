package com.dao.test;

import com.core.domain.Department;
import com.core.domain.Employee;
import com.core.domain.Profile;
import com.core.domain.User;
import com.core.enums.Role;
import com.dao.configuration.DAOConfiguration;
import com.dao.department.DepartmentDAO;
import com.dao.employee.EmployeeDAO;
import com.dao.profile.ProfileDAO;
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
public class EmployeeTest {

    @Autowired
    private DepartmentDAO departmentDAO;
    private Department department;

    @Autowired
    private UserDAO userDAO;
    private User user;
    private User secondUser;

    @Autowired
    private ProfileDAO profileDAO;

    @Autowired
    private EmployeeDAO employeeDAO;
    private Employee employee;
    private Employee secondEmployee;

    @After
    public void deleteDepartmentEntity() {
        try {
            employeeDAO.remove(employee);
        } catch (Exception e) {
        }
        try {
            employeeDAO.remove(secondEmployee);
        } catch (Exception e) {
        }
        try {
            userDAO.remove(secondUser);
        } catch (Exception e) {
        }
        try {
            userDAO.remove(user);
        } catch (Exception e) {
        }
        try {
            departmentDAO.remove(department);
        } catch (Exception e) {
        }
    }

    @Before
    public void entityToPersist() {
        department = departmentDAO.update(new Department("Отдел Java разработки", true));

        user = userDAO.update(new User("employee", "qwerty", Role.EMPLOYEE, false));
        Profile profile = new Profile("Инженер-программист", "г.Минск ул.Якуба Коласа 89",
                "+375294894561", "qwerty@mail.ru", "Java Python");

        employee = employeeDAO.update(new Employee("Иванов Иван Иванович", false, user,
                profileDAO.update(profile)));

        secondUser = new User("secondEmployee", "qwerty123", Role.EMPLOYEE, false);
        profile = new Profile("Инженер-программист", "г.Минск ул.Якуба Коласа 123",
                "+375294344561", "qwertyqwe@mail.ru", "Java");

        secondEmployee = employeeDAO.update(new Employee("Иванов Игорь Иванович", false, secondUser,
                profileDAO.update(profile)));

        System.out.println(employee.getName() + " - " + employee.getId());
        System.out.println(secondEmployee.getName() + " - " + secondEmployee.getId());
    }

    @Test
    public void save() {
        Assert.assertEquals(employee.getName(), "Иванов Иван Иванович");
        Assert.assertFalse(employee.isActive());

        Assert.assertEquals(employee.getUser().getUsername(), "employee");
        Assert.assertEquals(employee.getUser().getPassword(), "qwerty");
        Assert.assertEquals(employee.getUser().getRole(), Role.EMPLOYEE);
        Assert.assertFalse(employee.getUser().isActive());

        Assert.assertEquals(employee.getProfile().getEducation(), "Инженер-программист");
        Assert.assertEquals(employee.getProfile().getAddress(), "г.Минск ул.Якуба Коласа 89");
        Assert.assertEquals(employee.getProfile().getPhone(), "+375294894561");
        Assert.assertEquals(employee.getProfile().getEmail(), "qwerty@mail.ru");
        Assert.assertEquals(employee.getProfile().getSkills(), "Java Python");
    }

    @Test
    public void findByActive() {
        List<Employee> employeeListFromDB = employeeDAO.findByActive(true);
        employeeListFromDB.forEach(obj -> Assert.assertTrue(obj.isActive()));
    }

    @Test
    public void findByName() {
        List<Employee> employeeFromDB = employeeDAO.findByName("Иванов Игорь Иванович");
        Assert.assertEquals(employeeFromDB.get(employeeFromDB.size() - 1), secondEmployee);
    }

    @Test
    public void find() {
        Assert.assertEquals(employeeDAO.find(employee.getId()), employee);
    }

    @Test
    public void findAll() {
        List<Employee> employeeListFromDB = employeeDAO.findAll();
        Assert.assertEquals(employeeListFromDB.get(employeeListFromDB.size() - 1), secondEmployee);
        Assert.assertEquals(employeeListFromDB.get(employeeListFromDB.size() - 2), employee);
    }

    @Test
    public void findByDepartment() {
        employee.setDepartment(department);
        secondEmployee.setDepartment(department);

        List<Employee> employeeListFromDB = employeeDAO.findByDepartment(department);
        Assert.assertEquals(
                employeeListFromDB.get(employeeListFromDB.size() - 1).getDepartment(), department);
        Assert.assertEquals(
                employeeListFromDB.get(employeeListFromDB.size() - 2).getDepartment(), department);
    }

    @Test
    public void findByUser() {
        Assert.assertEquals(employeeDAO.findByUser(user), employee);
    }

    @Test
    public void update() {
        employee.setName("Григорьев Иван Иванович");
        Assert.assertEquals(employeeDAO.update(employee), employee);
    }

    @Test
    public void inactivateById() {
        employee.setActive(true);
        Assert.assertTrue(employeeDAO.inactivateById(employee.getId()));
    }

    @Test
    public void activateById() {
        Assert.assertTrue(employeeDAO.activateById(employee.getId()));
    }

    @Test
    public void inactivate() {
        employee.setActive(true);
        Assert.assertTrue(employeeDAO.inactivate(employee));
    }

    @Test
    public void activate() {
        Assert.assertTrue(employeeDAO.activate(employee));
    }

    @Test
    public void removeById() {
        Assert.assertTrue(employeeDAO.removeById(secondEmployee.getId()));
    }

    @Test
    public void remove() {
        Assert.assertTrue(employeeDAO.remove(employee));
    }
}
