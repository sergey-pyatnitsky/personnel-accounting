package com.personnel_accounting;

import com.personnel_accounting.configuration.ServiceTestConfiguration;
import com.personnel_accounting.domain.Department;
import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.domain.Project;
import com.personnel_accounting.domain.User;
import com.personnel_accounting.enums.Role;
import com.personnel_accounting.configuration.ServiceConfiguration;
import com.personnel_accounting.department.DepartmentService;
import com.personnel_accounting.employee.EmployeeService;
import com.personnel_accounting.project.ProjectService;
import com.personnel_accounting.user.UserService;
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
@ContextConfiguration(classes = ServiceTestConfiguration.class)
public class DepartmentServiceTest {
    private static final Logger logger = LogManager.getLogger("DepartmentServiceTest logger");

    @Autowired
    private DepartmentService departmentService;
    private Department department;

    @Autowired
    private EmployeeService employeeService;
    private Employee employee;
    private Employee secondEmployee;

    @Autowired
    private UserService userService;
    private User user;
    private User secondUser;

    @Autowired
    private ProjectService projectService;
    private Project project;
    private Project secondProject;

    @After
    public void deleteDepartmentEntity() {
        logger.info("START deleteDepartmentEntity");
        try {
            employeeService.remove(employee);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            employeeService.remove(secondEmployee);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            userService.remove(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            userService.remove(secondUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            projectService.remove(project);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            projectService.remove(secondProject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            departmentService.remove(department);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Before
    public void entityToPersist() {
        logger.info("START entityToPersist");
        department = departmentService.save(new Department("Отдел Java разработки", true));

        user = userService.save(new User("employee", "@123Qwerty", false), Role.EMPLOYEE);

        employee = new Employee("Иванов Иван Иванович", false, user);
        employee.setDepartment(department);
        employee = employeeService.save(employee);

        secondUser = userService.save(new User("secondEmployee", "@123Qwerty", false), Role.EMPLOYEE);

        secondEmployee = new Employee("Иванов Игорь Иванович", false, secondUser);
        secondEmployee.setDepartment(department);
        secondEmployee = employeeService.save(secondEmployee);

        project = projectService.save(
                new Project("Банковская система", department, true));
        secondProject = projectService.save(
                new Project("Система учёта товаров на складе", department, true));

        System.out.println(department.getName() + " - " + department.getId());
        System.out.println(employee.getName() + " - " + employee.getId());
        System.out.println(secondEmployee.getName() + " - " + secondEmployee.getId());
        System.out.println(project.getName() + " - " + project.getId());
        System.out.println(secondProject.getName() + " - " + project.getId());
    }

    @Test
    public void findProjects() {
        logger.info("START findProjects");
        List<Project> projects = departmentService.findProjects(department);

        Project tempProject = projects.get(projects.size() - 1);
        Assert.assertEquals(tempProject.getName(), "Система учёта товаров на складе");
        Assert.assertTrue(tempProject.isActive());

        tempProject = projects.get(projects.size() - 2);
        Assert.assertEquals(tempProject.getName(), "Банковская система");
        Assert.assertTrue(tempProject.isActive());
    }

    @Test
    public void findEmployees() {
        logger.info("START findEmployees");
        List<Employee> employees = departmentService.findEmployees(department);
        Employee employee = employees.get(employees.size() - 1);
        Assert.assertEquals(employee.getName(), "Иванов Игорь Иванович");
        Assert.assertFalse(employee.isActive());

        employee = employees.get(employees.size() - 2);
        Assert.assertEquals(employee.getName(), "Иванов Иван Иванович");
        Assert.assertFalse(employee.isActive());
    }

    @Test
    public void assignToDepartment() {
        logger.info("START assignToDepartment");
        employee.setDepartment(null);
        employeeService.save(employee);

        employee = departmentService.assignToDepartment(employee, department);
        Assert.assertEquals(employee.getName(), "Иванов Иван Иванович");
        Assert.assertFalse(employee.isActive());

        employee = employeeService.findByDepartment(department).stream()
                .filter(obj -> obj.getId().equals(employee.getId())).findFirst().orElse(null);
        assert employee != null;
        Assert.assertEquals(employee.getName(), "Иванов Иван Иванович");
        Assert.assertFalse(employee.isActive());

        projectService.findEmployeePositions(employee).stream().filter(obj -> !obj.getEmployee().equals(employee))
                .forEach(obj -> Assert.assertFalse(obj.isActive()));
    }

    @Test
    public void changeDepartmentState() {
        logger.info("START changeDepartmentState");
        department = departmentService.changeDepartmentState(department, false);
        Assert.assertEquals(department.getName(), "Отдел Java разработки");
        Assert.assertFalse(department.isActive());

        employeeService.findByDepartment(department).forEach(obj -> Assert.assertFalse(obj.isActive()));
        projectService.findByDepartment(department).forEach(obj-> Assert.assertFalse(obj.isActive()));
    }

}
