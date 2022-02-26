package com.personnel_accounting;

import com.personnel_accounting.domain.Department;
import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.domain.Profile;
import com.personnel_accounting.domain.User;
import com.personnel_accounting.configuration.DAOConfiguration;
import com.personnel_accounting.department.DepartmentDAO;
import com.personnel_accounting.employee.EmployeeDAO;
import com.personnel_accounting.profile.ProfileDAO;
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
@ContextConfiguration(classes = DAOConfiguration.class)
public class EmployeeDAOTest {

    private static final Logger logger = LogManager.getLogger("EmployeeDAOTest logger");

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
    public void deleteEntity() {
        logger.info("START deleteEntity");
        try {
            employeeDAO.remove(employee);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            employeeDAO.remove(secondEmployee);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            userDAO.remove(secondUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            userDAO.remove(user);
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

        user = userDAO.save(new User("employee", "qwerty", false));
        Profile profile = new Profile("Инженер-программист", "г.Минск ул.Якуба Коласа 89",
                "+375294894561", "qwerty@mail.ru", "Java Python");

        employee = new Employee("Иванов Иван Иванович", false, user, profileDAO.save(profile));
        employee.setDepartment(department);
        employee = employeeDAO.save(employee);

        secondUser = userDAO.save(new User("secondEmployee", "qwerty123", false));
        profile = new Profile("Инженер-программист", "г.Минск ул.Якуба Коласа 123",
                "+375294344561", "qwertyqwe@mail.ru", "Java");

        secondEmployee = new Employee("Иванов Игорь Иванович", false, secondUser, profileDAO.save(profile));
        secondEmployee.setDepartment(department);
        secondEmployee = employeeDAO.save(secondEmployee);

        System.out.println(employee.getName() + " - " + employee.getId());
        System.out.println(secondEmployee.getName() + " - " + secondEmployee.getId());
    }

    @Test
    public void save() {
        logger.info("START save");
        Assert.assertEquals(employee.getName(), "Иванов Иван Иванович");
        Assert.assertFalse(employee.isActive());

        Assert.assertEquals(employee.getUser().getUsername(), "employee");
        Assert.assertEquals(employee.getUser().getPassword(), "qwerty");
        Assert.assertFalse(employee.getUser().isActive());

        Assert.assertEquals(employee.getProfile().getEducation(), "Инженер-программист");
        Assert.assertEquals(employee.getProfile().getAddress(), "г.Минск ул.Якуба Коласа 89");
        Assert.assertEquals(employee.getProfile().getPhone(), "+375294894561");
        Assert.assertEquals(employee.getProfile().getEmail(), "qwerty@mail.ru");
        Assert.assertEquals(employee.getProfile().getSkills(), "Java Python");
    }

    @Test
    public void saveList() {
        logger.info("START saveList");
        deleteEntity();
        List<Employee> employeesFromDB = employeeDAO.save(entityToListSave());
        Employee tempEmployee = employeesFromDB.get(employeesFromDB.size() - 2);


        Assert.assertEquals(tempEmployee.getName(), "Иванов Иван Иванович");
        Assert.assertFalse(tempEmployee.isActive());

        Assert.assertEquals(tempEmployee.getUser().getUsername(), "employee");
        Assert.assertEquals(tempEmployee.getUser().getPassword(), "qwerty");
        Assert.assertFalse(tempEmployee.getUser().isActive());

        Assert.assertEquals(tempEmployee.getProfile().getEducation(), "Инженер-программист");
        Assert.assertEquals(tempEmployee.getProfile().getAddress(), "г.Минск ул.Якуба Коласа 89");
        Assert.assertEquals(tempEmployee.getProfile().getPhone(), "+375294894561");
        Assert.assertEquals(tempEmployee.getProfile().getEmail(), "qwerty@mail.ru");
        Assert.assertEquals(tempEmployee.getProfile().getSkills(), "Java Python");
    }

    private List<Employee> entityToListSave(){

        List<Employee> employees = new ArrayList<>();
        department = departmentDAO.save(new Department("Отдел Java разработки", true));

        user = userDAO.save(new User("employee", "qwerty", false));
        Profile profile = new Profile("Инженер-программист", "г.Минск ул.Якуба Коласа 89",
                "+375294894561", "qwerty@mail.ru", "Java Python");

        employee = new Employee("Иванов Иван Иванович", false, user, profileDAO.save(profile));
        employee.setDepartment(department);

        employees.add(employee);

        secondUser = userDAO.save(new User("secondEmployee", "qwerty123", false));
        profile = new Profile("Инженер-программист", "г.Минск ул.Якуба Коласа 123",
                "+375294344561", "qwertyqwe@mail.ru", "Java");

        secondEmployee = new Employee("Иванов Игорь Иванович", false, secondUser, profileDAO.save(profile));
        employees.add(secondEmployee);
        secondEmployee = employeeDAO.save(secondEmployee);
        return employees;
    }

    @Test
    public void findByActive() {
        logger.info("START findByActive");
        List<Employee> employeeListFromDB = employeeDAO.findByActive(true);
        employeeListFromDB.forEach(obj -> Assert.assertTrue(obj.isActive()));
    }

    @Test
    public void findByName() {
        logger.info("START findByName");
        List<Employee> employeeFromDB = employeeDAO.findByName("Иванов Игорь Иванович");
        Assert.assertEquals(employeeFromDB.get(employeeFromDB.size() - 1).getName(), "Иванов Игорь Иванович");
    }

    @Test
    public void find() {
        logger.info("START find");
        Employee tempEmployee = employeeDAO.find(employee.getId());
        Assert.assertEquals(tempEmployee.getName(), "Иванов Иван Иванович");
        Assert.assertFalse(tempEmployee.isActive());
    }

    @Test
    public void findAll() {
        logger.info("START findAll");
        List<Employee> employeeListFromDB = employeeDAO.findAll();

        Employee tempEmployee = employeeListFromDB.get(employeeListFromDB.size() - 2);

        Assert.assertEquals(tempEmployee.getName(), "Иванов Иван Иванович");
        Assert.assertFalse(tempEmployee.isActive());

        tempEmployee = employeeListFromDB.get(employeeListFromDB.size() - 1);

        Assert.assertEquals(tempEmployee.getName(), "Иванов Игорь Иванович");
        Assert.assertFalse(tempEmployee.isActive());
    }

    @Test
    public void findByDepartment() {
        logger.info("START findByDepartment");
        List<Employee> employeeListFromDB = employeeDAO.findByDepartment(department);

        Employee tempEmployee = employeeListFromDB.get(employeeListFromDB.size() - 2);

        Assert.assertEquals(tempEmployee.getName(), "Иванов Иван Иванович");
        Assert.assertFalse(tempEmployee.isActive());

        tempEmployee = employeeListFromDB.get(employeeListFromDB.size() - 1);

        Assert.assertEquals(tempEmployee.getName(), "Иванов Игорь Иванович");
        Assert.assertFalse(tempEmployee.isActive());
    }

    @Test
    public void findByUser() {
        logger.info("START findByUser");
        Employee tempEmployee = employeeDAO.findByUser(user);
        Assert.assertEquals(tempEmployee.getName(), "Иванов Иван Иванович");
        Assert.assertFalse(tempEmployee.isActive());
    }

    @Test
    public void update() {
        logger.info("START update");
        employee.setName("Григорьев Иван Иванович");
        Assert.assertEquals(employeeDAO.update(employee).getName(), "Григорьев Иван Иванович");
    }

    @Test
    public void inactivateById() {
        logger.info("START inactivateById");
        employee.setActive(true);
        Assert.assertTrue(employeeDAO.inactivateById(employee.getId()));
    }

    @Test
    public void activateById() {
        logger.info("START activateById");
        Assert.assertTrue(employeeDAO.activateById(employee.getId()));
    }

    @Test
    public void inactivate() {
        logger.info("START inactivate");
        employee.setActive(true);
        Assert.assertTrue(employeeDAO.inactivate(employee));
    }

    @Test
    public void activate() {
        logger.info("START activate");
        Assert.assertTrue(employeeDAO.activate(employee));
    }

    @Test
    public void removeById() {
        logger.info("START removeById");
        Assert.assertTrue(employeeDAO.removeById(secondEmployee.getId()));
    }

    @Test
    public void remove() {
        logger.info("START remove");
        Assert.assertTrue(employeeDAO.remove(employee));
    }
}
