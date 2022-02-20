package com.service.test;

import com.core.domain.*;
import com.core.enums.Role;
import com.dao.employee_position.EmployeePositionDAO;
import com.dao.position.PositionDAO;
import com.service.configuration.ServiceConfiguration;
import com.service.department.DepartmentService;
import com.service.employee.EmployeeService;
import com.service.project.ProjectService;
import com.service.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
@ContextConfiguration(classes = ServiceConfiguration.class)
public class ProjectServiceTest {

    private static final Logger logger = LogManager.getLogger("ProjectServiceTest logger");

    @Autowired
    private ProjectService projectService;
    private Project project;

    @Autowired
    private DepartmentService departmentService;
    private Department department;

    @Autowired
    private EmployeeService employeeService;
    private Employee employee;

    @Autowired
    private UserService userService;
    private User user;

    @Autowired
    private EmployeePositionDAO employeePositionDAO;
    private EmployeePosition employeePosition;

    @Autowired
    private PositionDAO positionDAO;
    private Position position;

    @After
    public void deleteProjectEntity() {
        logger.info("START deleteProjectEntity");
        try {
            employeePositionDAO.remove(employeePosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            positionDAO.remove(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            projectService.remove(project);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            employeeService.remove(employee);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            departmentService.remove(department);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            userService.remove(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Before
    public void entityToPersist() {
        logger.info("START entityToPersist");
        department = departmentService.save(
                new Department("Отдел Java Разработки", true));
        project = projectService.save(
                new Project("Банковская система", department, true));

        user = userService.save(new User("employee", "qwerty", false), Role.EMPLOYEE);

        employee = new Employee("Иванов Иван Иванович", false, user);
        employee.setDepartment(department);
        employee = employeeService.save(employee);
        position = positionDAO.save(new Position("Проектировщик"));

        employeePosition = employeePositionDAO.save(
                new EmployeePosition(false, employee, position, project, department));

        System.out.println(project.getName() + " - " + project.getId());
        System.out.println();
        System.out.println(department.getName() + " - " + department.getId());
        System.out.println(employee.getName() + " - " + employee.getId());
        System.out.println("EmployeePosition" + " - " + employee.getId());
    }

    @Test
    public void assignToProject() {
        logger.info("START assignToProject");
        EmployeePosition tempEmployeePosition = projectService.assignToProject(employee, project, position);
        Assert.assertEquals(tempEmployeePosition.getId(), employeePosition.getId());
        Assert.assertEquals(tempEmployeePosition.getCreateDate().toString(),
                employeePosition.getCreateDate().toString());
        employeePosition = tempEmployeePosition;
    }

    @Test
    public void changeEmployeeActiveStatusInProject() {
        logger.info("START changeEmployeeActiveStatusInProject");
        EmployeePosition tempEmployeePosition =
                projectService.changeEmployeeActiveStatusInProject(employee, project, true);
        Assert.assertEquals(tempEmployeePosition.getId(), employeePosition.getId());
        Assert.assertEquals(tempEmployeePosition.getCreateDate().toString(),
                employeePosition.getCreateDate().toString());
        Assert.assertTrue(tempEmployeePosition.isActive());
        employeePosition = tempEmployeePosition;
    }

    @Test
    public void findByProject() {
        logger.info("START findByProject");
        try {
            employeePositionDAO.remove(employeePosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        employeePosition = projectService.assignToProject(employee, project, position);
        List<Employee> employee = projectService.findByProject(project);
        Employee tempEmployee = employee.get(employee.size() - 1);
        Assert.assertEquals(tempEmployee.getName(), "Иванов Иван Иванович");
        Assert.assertFalse(tempEmployee.isActive());
    }
}
