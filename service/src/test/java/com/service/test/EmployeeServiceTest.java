package com.service.test;

import com.core.domain.*;
import com.core.enums.Role;
import com.core.enums.TaskStatus;
import com.dao.task.TaskDAO;
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

import java.sql.Date;
import java.sql.Time;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServiceConfiguration.class)
public class EmployeeServiceTest {

    private static final Logger logger = LogManager.getLogger("EmployeeServiceTest logger");

    @Autowired
    private ProjectService projectService;
    private Project project;

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
    private TaskDAO taskDAO;
    private Task task;

    @After
    public void deleteEntity() {
        logger.info("START deleteEntity");
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
            employeeService.remove(secondEmployee);
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
        try {
            userService.remove(secondUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            taskDAO.remove(task);
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

        user = userService.save(new User("employee", "qwerty", Role.EMPLOYEE, false));

        employee = new Employee("Иванов Иван Иванович", false, user);
        employee.setDepartment(department);
        employee = employeeService.save(employee);

        secondUser = userService.save(new User("reporter", "qwerty123", Role.PROJECT_MANAGER, true));

        secondEmployee = employeeService.save(new Employee("Игорев Иван Иванович", true, secondUser));

        task = taskDAO.save(new Task("Исправить баг", "Ошибка отображения окна",
                project, employee, secondEmployee, TaskStatus.OPEN));

        System.out.println(project.getName() + " - " + project.getId());
        System.out.println();
        System.out.println(department.getName() + " - " + department.getId());
        System.out.println(employee.getName() + " - " + employee.getId());
        System.out.println(secondEmployee.getName() + " - " + secondEmployee.getId());
        System.out.println(task.getName() + " - " + task.getId());
    }

    @Test
    public void addProfileData() {
        logger.info("START addProfileData");
        Profile profile = new Profile("Инженер-программист", "г.Минск ул.Якуба Коласа 89",
                "+375294894561", "qwerty@mail.ru", "Java Python");
        Employee tempEmployee = employeeService.addProfileData(employee, profile);
        Assert.assertEquals(tempEmployee.getName(), "Иванов Иван Иванович");
        Assert.assertFalse(tempEmployee.isActive());
    }

    @Test
    public void assigneeTime() {
        logger.info("START assigneeTime");
        Date date = new Date(System.currentTimeMillis());
        Time time = new Time(2, 30, 0);
        ReportCard reportCard = employeeService.assigneeTime(date, time, task, employee);
        Assert.assertEquals(reportCard.getWorkingTime(), time);
        Assert.assertEquals(reportCard.getDate().toString(), reportCard.getDate().toString());
        Assert.assertEquals(reportCard.getCreateDate().toString(), reportCard.getCreateDate().toString());
    }

    @Test
    public void addTaskInProject() {
        logger.info("START addTaskInProject");
        try {
            taskDAO.remove(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Task taskFromDB = employeeService.addTaskInProject(project, new Task("Исправить баг", "Ошибка отображения окна",
                employee, secondEmployee, TaskStatus.OPEN));
        Assert.assertEquals(taskFromDB.getName(), task.getName());
        Assert.assertEquals(taskFromDB.getDescription(), task.getDescription());
        Assert.assertEquals(taskFromDB.getTaskStatus(), task.getTaskStatus());
        task = taskFromDB;
    }

    @Test
    public void changeTaskStatus() {
        logger.info("START changeTaskStatus");
        Task taskFromDB = employeeService.changeTaskStatus(task, TaskStatus.CLOSED);
        Assert.assertEquals(taskFromDB.getName(), task.getName());
        Assert.assertEquals(taskFromDB.getDescription(), task.getDescription());
        Assert.assertEquals(taskFromDB.getTaskStatus(), TaskStatus.CLOSED);
        task = taskFromDB;
    }
}
