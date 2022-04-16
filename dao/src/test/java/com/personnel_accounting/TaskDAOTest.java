package com.personnel_accounting;

import com.personnel_accounting.domain.*;
import com.personnel_accounting.enums.TaskStatus;
import com.personnel_accounting.configuration.DAOConfiguration;
import com.personnel_accounting.department.DepartmentDAO;
import com.personnel_accounting.employee.EmployeeDAO;
import com.personnel_accounting.profile.ProfileDAO;
import com.personnel_accounting.project.ProjectDAO;
import com.personnel_accounting.task.TaskDAO;
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

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DAOConfiguration.class)
public class TaskDAOTest {

    private static final Logger logger = LogManager.getLogger("TaskDAOTest logger");

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
        logger.info("START deleteDepartmentEntity");
        try {
            taskDAO.remove(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            taskDAO.remove(secondTask);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            employeeDAO.remove(assignee);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            employeeDAO.remove(reporter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            userDAO.remove(userAssignee);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            userDAO.remove(userReporter);
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

        userAssignee = userDAO.save(new User("employee", "qwerty", true));
        Profile profile = new Profile("Инженер-программист", "г.Минск ул.Якуба Коласа 89",
                "+375294894561", "qwerty@mail.ru", "Java Python");

        assignee = employeeDAO.save(new Employee("Иванов Иван Иванович", true, userAssignee,
                profileDAO.save(profile)));

        userReporter = userDAO.save(new User("reporter", "qwerty123", true));
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
        logger.info("START save");
        Assert.assertEquals(task.getAssignee(), assignee);
        Assert.assertEquals(task.getReporter(), reporter);
        Assert.assertEquals(task.getName(), "Исправить баг");
        Assert.assertEquals(task.getDescription(), "Ошибка отображения окна");
        Assert.assertEquals(task.getProject(), project);
        Assert.assertEquals(task.getTaskStatus(), TaskStatus.OPEN);
    }

    @Test
    public void findAll() {
        logger.info("START findAll");
        List<Task> taskFromDB = taskDAO.findAll();

        Task testTask = taskFromDB.get(taskFromDB.size() - 1);
        Assert.assertEquals(testTask.getId(), secondTask.getId());
        Assert.assertEquals(testTask.getName(), secondTask.getName());
        Assert.assertEquals(testTask.getDescription(), secondTask.getDescription());

        testTask = taskFromDB.get(taskFromDB.size() - 2);
        Assert.assertEquals(testTask.getId(), task.getId());
        Assert.assertEquals(testTask.getName(), task.getName());
        Assert.assertEquals(testTask.getDescription(), task.getDescription());
    }

    @Test
    public void findByName() {
        logger.info("START findByName");
        Assert.assertEquals(taskDAO.findByName("Исправить баг").getName(),
                "Исправить баг");
    }

    @Test
    public void findByProject() {
        logger.info("START findByProject");
        List<Task> taskFromDB = taskDAO.findByProject(project);

        Task testTask = taskFromDB.get(taskFromDB.size() - 1);
        Assert.assertEquals(testTask.getId(), secondTask.getId());
        Assert.assertEquals(testTask.getName(), secondTask.getName());
        Assert.assertEquals(testTask.getDescription(), secondTask.getDescription());

        testTask = taskFromDB.get(taskFromDB.size() - 2);
        Assert.assertEquals(testTask.getId(), task.getId());
        Assert.assertEquals(testTask.getName(), task.getName());
        Assert.assertEquals(testTask.getDescription(), task.getDescription());
    }

    @Test
    public void findByReporter() {
        logger.info("START findByReporter");
        List<Task> taskFromDB = taskDAO.findByReporter(reporter);

        Task testTask = taskFromDB.get(taskFromDB.size() - 1);
        Assert.assertEquals(testTask.getId(), secondTask.getId());
        Assert.assertEquals(testTask.getName(), secondTask.getName());
        Assert.assertEquals(testTask.getDescription(), secondTask.getDescription());

        testTask = taskFromDB.get(taskFromDB.size() - 2);
        Assert.assertEquals(testTask.getId(), task.getId());
        Assert.assertEquals(testTask.getName(), task.getName());
        Assert.assertEquals(testTask.getDescription(), task.getDescription());
    }

    @Test
    public void findByAssignee() {
        logger.info("START findByAssignee");
        List<Task> taskFromDB = taskDAO.findByAssignee(assignee);

        Task testTask = taskFromDB.get(taskFromDB.size() - 1);
        Assert.assertEquals(testTask.getId(), secondTask.getId());
        Assert.assertEquals(testTask.getName(), secondTask.getName());
        Assert.assertEquals(testTask.getDescription(), secondTask.getDescription());

        testTask = taskFromDB.get(taskFromDB.size() - 2);
        Assert.assertEquals(testTask.getId(), task.getId());
        Assert.assertEquals(testTask.getName(), task.getName());
        Assert.assertEquals(testTask.getDescription(), task.getDescription());
    }

    @Test
    public void findByStatus() {
        logger.info("START findByStatus");
        List<Task> taskFromDB = taskDAO.findByStatus(TaskStatus.OPEN);

        Task testTask = taskFromDB.get(taskFromDB.size() - 1);
        Assert.assertEquals(testTask.getId(), secondTask.getId());
        Assert.assertEquals(testTask.getName(), secondTask.getName());
        Assert.assertEquals(testTask.getDescription(), secondTask.getDescription());

        testTask = taskFromDB.get(taskFromDB.size() - 2);
        Assert.assertEquals(testTask.getId(), task.getId());
        Assert.assertEquals(testTask.getName(), task.getName());
        Assert.assertEquals(testTask.getDescription(), task.getDescription());
    }

    @Test
    public void find() {
        logger.info("START find");
        Task testTask = taskDAO.find(task.getId());
        Assert.assertEquals(testTask.getId(), task.getId());
        Assert.assertEquals(testTask.getName(), task.getName());
        Assert.assertEquals(testTask.getDescription(), task.getDescription());
    }

    @Test
    public void update() {
        logger.info("START update");
        task.setTaskStatus(TaskStatus.CLOSED);
        Task testTask = taskDAO.merge(task);
        Assert.assertEquals(testTask.getId(), task.getId());
        Assert.assertEquals(testTask.getName(), task.getName());
        Assert.assertEquals(testTask.getDescription(), task.getDescription());
    }

    @Test
    public void removeById() {
        logger.info("START removeById");
        Assert.assertTrue(taskDAO.removeById(task.getId()));
    }

    @Test
    public void remove() {
        logger.info("START remove");
        Assert.assertTrue(taskDAO.remove(task));
    }
}
