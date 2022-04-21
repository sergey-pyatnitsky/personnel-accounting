package com.personnel_accounting;

import com.personnel_accounting.configuration.ServiceTestConfiguration;
import com.personnel_accounting.department.DepartmentService;
import com.personnel_accounting.domain.Department;
import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.domain.Profile;
import com.personnel_accounting.domain.Project;
import com.personnel_accounting.domain.ReportCard;
import com.personnel_accounting.domain.Task;
import com.personnel_accounting.domain.User;
import com.personnel_accounting.employee.EmployeeService;
import com.personnel_accounting.employee_position.EmployeePositionDAO;
import com.personnel_accounting.enums.Role;
import com.personnel_accounting.enums.TaskStatus;
import com.personnel_accounting.project.ProjectService;
import com.personnel_accounting.report_card.ReportCardDAO;
import com.personnel_accounting.task.TaskDAO;
import com.personnel_accounting.user.UserService;
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

import java.sql.Time;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServiceTestConfiguration.class)
public class EmployeeServiceTest {

    private static final Logger logger = LogManager.getLogger("EmployeeServiceTest logger");

    @Autowired
    private ProjectService projectService;
    private Project project;

    @Autowired
    private DepartmentService departmentService;
    private Department department;

    @Autowired
    private EmployeePositionDAO employeePositionDAO;

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

    @Autowired
    private ReportCardDAO reportCardDAO;
    private ReportCard reportCard;

    @After
    public void deleteEntity() {
        logger.info("START deleteEntity");
        try {
            reportCardDAO.remove(reportCard);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            taskDAO.remove(task);
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
    }

    @Before
    public void entityToPersist() {
        logger.info("START entityToPersist");
        department = departmentService.save(
                new Department("Отдел Java Разработки", true));
        project = projectService.save(
                new Project("Банковская система", department, true));

        user = userService.save(new User("employee", "@123Qwerty", false), Role.EMPLOYEE);

        employee = new Employee("Иванов Иван Иванович", false, user);
        employee.setDepartment(department);
        employee = employeeService.save(employee);

        secondUser = userService.save(new User("reporter", "@123Qwerty", true), Role.PROJECT_MANAGER);

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
                "+375(29)489-45-61", "qwerty@mail.ru", "Java Python");
        employee = employeeService.addProfileData(employee, profile);
        Assert.assertEquals(employee.getName(), "Иванов Иван Иванович");
        Assert.assertFalse(employee.isActive());

        Profile tempProfile = employeeService.findProfileByEmployee(employee);
        Assert.assertEquals(tempProfile.getSkills(), "Java Python");
        Assert.assertEquals(tempProfile.getEmail(), "qwerty@mail.ru");
        Assert.assertEquals(tempProfile.getPhone(), "+375(29)489-45-61");
        Assert.assertEquals(tempProfile.getAddress(), "г.Минск ул.Якуба Коласа 89");
        Assert.assertEquals(tempProfile.getEducation(), "Инженер-программист");
    }

    @Test
    public void findProfileByEmployee() {
        logger.info("START findProfileByEmployee");
        Profile profile = new Profile("Инженер-программист", "г.Минск ул.Якуба Коласа 89",
                "+375(29)489-45-61", "qwerty@mail.ru", "Java Python");
        employee = employeeService.addProfileData(employee, profile);
        Profile tempProfile = employeeService.findProfileByEmployee(employee);
        Assert.assertEquals(tempProfile.getSkills(), "Java Python");
        Assert.assertEquals(tempProfile.getEmail(), "qwerty@mail.ru");
        Assert.assertEquals(tempProfile.getPhone(), "+375(29)489-45-61");
        Assert.assertEquals(tempProfile.getAddress(), "г.Минск ул.Якуба Коласа 89");
        Assert.assertEquals(tempProfile.getEducation(), "Инженер-программист");
    }

    @Test
    public void addTaskInProject() {
        logger.info("START addTaskInProject");
        try {
            taskDAO.remove(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
        task = employeeService.addTaskInProject(project, new Task("Исправить баг", "Ошибка отображения окна",
                employee, secondEmployee, TaskStatus.OPEN));
        Assert.assertEquals(task.getName(), "Исправить баг");
        Assert.assertEquals(task.getDescription(), "Ошибка отображения окна");
        Assert.assertEquals(task.getTaskStatus(), TaskStatus.OPEN);
        Assert.assertEquals(task.getProject(), project);
    }

    @Test
    public void changeTaskStatus() {
        logger.info("START changeTaskStatus");
        task = employeeService.changeTaskStatus(task);
        Assert.assertEquals(task.getName(), "Исправить баг");
        Assert.assertEquals(task.getDescription(), "Ошибка отображения окна");
        Assert.assertEquals(task.getTaskStatus(), TaskStatus.IN_PROGRESS);
        Assert.assertEquals(task.getProject(), project);

        task = taskDAO.find(task.getId());
        Assert.assertEquals(task.getName(), "Исправить баг");
        Assert.assertEquals(task.getDescription(), "Ошибка отображения окна");
        Assert.assertEquals(task.getTaskStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    public void activate() {
        logger.info("START activate");
        Assert.assertTrue(employeeService.activate(employee));
        employeePositionDAO.findByEmployee(employee).forEach(obj -> Assert.assertTrue(obj.isActive()));

        Assert.assertTrue(employeeService.find(employee.getId()).isActive());
    }

    @Test
    public void inactivate() {
        logger.info("START inactivate");
        Assert.assertTrue(employeeService.inactivate(employee));
        employeePositionDAO.findByEmployee(employee).forEach(obj -> Assert.assertFalse(obj.isActive()));

        Assert.assertFalse(employeeService.find(employee.getId()).isActive());
    }

    @Test
    public void trackTime() {
        logger.info("START trackTime");
        reportCard = employeeService.trackTime(task, new Time(23, 25, 00));
        Assert.assertEquals(reportCard.getWorkingTime().getHours(), 23);
        Assert.assertEquals(reportCard.getWorkingTime().getMinutes(), 25);

        Task tempTask = reportCard.getTask();
        Assert.assertEquals(tempTask.getName(), "Исправить баг");
        Assert.assertEquals(tempTask.getDescription(), "Ошибка отображения окна");
        Assert.assertEquals(tempTask.getTaskStatus(), TaskStatus.OPEN);
        Assert.assertEquals(tempTask.getProject(), project);
    }
}
