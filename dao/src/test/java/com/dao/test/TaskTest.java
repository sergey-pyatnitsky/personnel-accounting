package com.dao.test;

import com.core.domain.*;
import com.core.enums.Role;
import com.core.enums.TaskStatus;
import com.dao.configuration.DAOConfiguration;
import com.dao.department.DepartmentDAO;
import com.dao.employee.EmployeeDAO;
import com.dao.profile.ProfileDAO;
import com.dao.project.ProjectDAO;
import com.dao.task.TaskDAO;
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
public class TaskTest {

    @Autowired
    private TaskDAO taskDAO;
    private Task task;
    private Task secondTask;

    @Autowired
    private DepartmentDAO departmentDAO;
    private Department department;

    @Autowired
    private UserDAO userDAO;
    private User userAssignee;
    private User userReporter;

    @Autowired
    private ProfileDAO profileDAO;

    @Autowired
    private ProjectDAO projectDAO;
    private Project project;

    @Autowired
    private EmployeeDAO employeeDAO;
    private Employee assignee;
    private Employee reporter;

    @After
    public void deleteDepartmentEntity() {
        try {
            taskDAO.remove(task);
        } catch (Exception e) {
        }
        try {
            taskDAO.remove(secondTask);
        } catch (Exception e) {
        }
        try {
            employeeDAO.remove(assignee);
        } catch (Exception e) {
        }
        try {
            employeeDAO.remove(reporter);
        } catch (Exception e) {
        }
        try {
            userDAO.remove(userAssignee);
        } catch (Exception e) {
        }
        try {
            userDAO.remove(userReporter);
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

        userAssignee = userDAO.save(new User("employee", "qwerty", Role.EMPLOYEE, true));
        Profile profile = new Profile("Инженер-программист", "г.Минск ул.Якуба Коласа 89",
                "+375294894561", "qwerty@mail.ru", "Java Python");

        assignee = employeeDAO.save(new Employee("Иванов Иван Иванович", true, userAssignee,
                profileDAO.update(profile)));

        userReporter = userDAO.save(new User("reporter", "qwerty123", Role.PROJECT_MANAGER, true));
        profile = new Profile("Инженер-программист", "г.Минск ул.Приова 105",
                "+375293494561", "reporter@mail.ru", "Java");

        reporter = employeeDAO.save(new Employee("Игорев Иван Иванович", true, userReporter,
                profileDAO.save(profile)));

        task = taskDAO.save(new Task("Исправить баг", "Ошибка отображения окна",
                project, reporter, assignee, TaskStatus.OPEN));
        secondTask = taskDAO.save(new Task("Исправить баг картинок", "Ошибка отображения",
                project, reporter, assignee, TaskStatus.OPEN));

        System.out.println(task.getName() + " - " + task.getId());
        System.out.println(secondTask.getName() + " - " + secondTask.getId());
    }

    @Test
    public void save() {
        Assert.assertEquals(task.getAssignee(), assignee);
        Assert.assertEquals(task.getReporter(), reporter);
        Assert.assertEquals(task.getName(), "Исправить баг");
        Assert.assertEquals(task.getDescription(), "Ошибка отображения окна");
        Assert.assertEquals(task.getProject(), project);
        Assert.assertEquals(task.getTaskStatus(), TaskStatus.OPEN);
    }

    @Test
    public void findAll() {
        List<Task> taskFromDB = taskDAO.findAll();
        Assert.assertEquals(taskFromDB.get(taskFromDB.size() - 1), secondTask);
        Assert.assertEquals(taskFromDB.get(taskFromDB.size() - 2), task);
    }

    @Test
    public void findByName() {
        Assert.assertEquals(taskDAO.findByName("Исправить баг"), task);
    }

    @Test
    public void findByProject() {
        List<Task> taskFromDB = taskDAO.findByProject(project);
        Assert.assertEquals(taskFromDB.get(taskFromDB.size() - 1), secondTask);
        Assert.assertEquals(taskFromDB.get(taskFromDB.size() - 2), task);
    }

    @Test
    public void findByReporter() {
        List<Task> taskFromDB = taskDAO.findByReporter(reporter);
        Assert.assertEquals(taskFromDB.get(taskFromDB.size() - 1), secondTask);
        Assert.assertEquals(taskFromDB.get(taskFromDB.size() - 2), task);
    }

    @Test
    public void findByAssignee() {
        List<Task> taskFromDB = taskDAO.findByAssignee(assignee);
        Assert.assertEquals(taskFromDB.get(taskFromDB.size() - 1), secondTask);
        Assert.assertEquals(taskFromDB.get(taskFromDB.size() - 2), task);
    }

    @Test
    public void findByStatus() {
        List<Task> taskFromDB = taskDAO.findByStatus(TaskStatus.OPEN);
        Assert.assertEquals(taskFromDB.get(taskFromDB.size() - 1), secondTask);
        Assert.assertEquals(taskFromDB.get(taskFromDB.size() - 2), task);
    }

    @Test
    public void find() {
        Assert.assertEquals(taskDAO.find(task.getId()), task);
    }

    @Test
    public void update() {
        task.setTaskStatus(TaskStatus.CLOSED);
        Assert.assertEquals(taskDAO.update(task), task);
    }

    @Test
    public void removeById() {
        Assert.assertTrue(taskDAO.removeById(task.getId()));
    }

    @Test
    public void remove() {
        Assert.assertTrue(taskDAO.remove(task));
    }
}
