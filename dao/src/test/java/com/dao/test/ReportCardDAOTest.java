package com.dao.test;

import com.core.domain.*;
import com.core.enums.Role;
import com.core.enums.TaskStatus;
import com.dao.configuration.DAOConfiguration;
import com.dao.department.DepartmentDAO;
import com.dao.project.employee.EmployeeDAO;
import com.dao.profile.ProfileDAO;
import com.dao.project.ProjectDAO;
import com.dao.report_card.ReportCardDAO;
import com.dao.task.TaskDAO;
import com.dao.user.UserDAO;
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

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DAOConfiguration.class)
public class ReportCardDAOTest {

    private static final Logger logger = LogManager.getLogger("ReportCardDAOTest logger");

    @Autowired
    private ReportCardDAO reportCardDAO;
    private ReportCard reportCard;
    private ReportCard secondReportCard;

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

    private Date date;

    @After
    public void deleteDepartmentEntity() {
        logger.info("START deleteDepartmentEntity");
        try {
            reportCardDAO.remove(reportCard);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            reportCardDAO.remove(secondReportCard);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        userAssignee = userDAO.save(new User("employee", "qwerty", Role.EMPLOYEE, true));
        Profile profile = new Profile("Инженер-программист", "г.Минск ул.Якуба Коласа 89",
                "+375294894561", "qwerty@mail.ru", "Java Python");

        assignee = employeeDAO.save(new Employee("Иванов Иван Иванович", true, userAssignee,
                profileDAO.save(profile)));

        userReporter = userDAO.save(new User("reporter", "qwerty123", Role.PROJECT_MANAGER, true));
        profile = new Profile("Инженер-программист", "г.Минск ул.Приова 105",
                "+375293494561", "reporter@mail.ru", "Java");

        reporter = employeeDAO.save(new Employee("Игорев Иван Иванович", true, userReporter,
                profileDAO.save(profile)));

        task = taskDAO.save(new Task("Исправить баг", "Ошибка отображения окна",
                project, reporter, assignee, TaskStatus.OPEN));
        secondTask = taskDAO.save(new Task("Исправить баг картинок", "Ошибка отображения",
                project, reporter, assignee, TaskStatus.OPEN));

        date = new Date(System.currentTimeMillis());

        reportCard = reportCardDAO.save(new ReportCard(date,
                task, assignee, new Time(2, 30, 0)));
        secondReportCard = reportCardDAO.save(new ReportCard(date,
                secondTask, assignee, new Time(4, 35, 0)));

        System.out.println(reportCard.getTask().getName() + " - " + reportCard.getId());
        System.out.println(secondReportCard.getTask().getName() + " - " + secondReportCard.getId());
    }

    @Test
    public void save() {
        logger.info("START save");
        Assert.assertEquals(reportCard.getTask(), task);
        Assert.assertEquals(reportCard.getDate(), date);
        Assert.assertEquals(reportCard.getEmployee(), assignee);
        Assert.assertEquals(reportCard.getWorkingTime(), new Time(2, 30, 0));
    }

    @Test
    public void findAll() {
        logger.info("START findAll");
        List<ReportCard> repostCardFromDB = reportCardDAO.findAll();

        ReportCard tempReportCard = repostCardFromDB.get(repostCardFromDB.size() - 1);
        Assert.assertEquals(tempReportCard.getId(), secondReportCard.getId());
        Assert.assertEquals(tempReportCard.getWorkingTime(), secondReportCard.getWorkingTime());
        Assert.assertEquals(tempReportCard.getDate().toString(), secondReportCard.getDate().toString());
        Assert.assertEquals(tempReportCard.getCreateDate().toString(), secondReportCard.getCreateDate().toString());

        tempReportCard = repostCardFromDB.get(repostCardFromDB.size() - 2);
        Assert.assertEquals(tempReportCard.getId(), reportCard.getId());
        Assert.assertEquals(tempReportCard.getWorkingTime(), reportCard.getWorkingTime());
        Assert.assertEquals(tempReportCard.getDate().toString(), reportCard.getDate().toString());
        Assert.assertEquals(tempReportCard.getCreateDate().toString(), reportCard.getCreateDate().toString());
    }

    @Test
    public void findByDate() {
        logger.info("START findByDate");
        List<ReportCard> repostCardFromDB = reportCardDAO.findByDate(date);

        ReportCard tempReportCard = repostCardFromDB.get(repostCardFromDB.size() - 1);
        Assert.assertEquals(tempReportCard.getId(), secondReportCard.getId());
        Assert.assertEquals(tempReportCard.getWorkingTime(), secondReportCard.getWorkingTime());
        Assert.assertEquals(tempReportCard.getDate().toString(), secondReportCard.getDate().toString());
        Assert.assertEquals(tempReportCard.getCreateDate().toString(), secondReportCard.getCreateDate().toString());

        tempReportCard = repostCardFromDB.get(repostCardFromDB.size() - 2);
        Assert.assertEquals(tempReportCard.getId(), reportCard.getId());
        Assert.assertEquals(tempReportCard.getWorkingTime(), reportCard.getWorkingTime());
        Assert.assertEquals(tempReportCard.getDate().toString(), reportCard.getDate().toString());
        Assert.assertEquals(tempReportCard.getCreateDate().toString(), reportCard.getCreateDate().toString());
    }

    @Test
    public void findByTask() {
        logger.info("START findByTask");
        ReportCard tempReportCard = reportCardDAO.findByTask(task);
        Assert.assertEquals(tempReportCard.getId(), reportCard.getId());
        Assert.assertEquals(tempReportCard.getWorkingTime(), reportCard.getWorkingTime());
        Assert.assertEquals(tempReportCard.getDate().toString(), reportCard.getDate().toString());
        Assert.assertEquals(tempReportCard.getCreateDate().toString(), reportCard.getCreateDate().toString());
    }

    @Test
    public void findByEmployee() {
        logger.info("START findByEmployee");
        List<ReportCard> repostCardFromDB = reportCardDAO.findByEmployee(assignee);

        ReportCard tempReportCard = repostCardFromDB.get(repostCardFromDB.size() - 1);
        Assert.assertEquals(tempReportCard.getId(), secondReportCard.getId());
        Assert.assertEquals(tempReportCard.getWorkingTime(), secondReportCard.getWorkingTime());
        Assert.assertEquals(tempReportCard.getDate().toString(), secondReportCard.getDate().toString());
        Assert.assertEquals(tempReportCard.getCreateDate().toString(), secondReportCard.getCreateDate().toString());

        tempReportCard = repostCardFromDB.get(repostCardFromDB.size() - 2);
        Assert.assertEquals(tempReportCard.getId(), reportCard.getId());
        Assert.assertEquals(tempReportCard.getWorkingTime(), reportCard.getWorkingTime());
        Assert.assertEquals(tempReportCard.getDate().toString(), reportCard.getDate().toString());
        Assert.assertEquals(tempReportCard.getCreateDate().toString(), reportCard.getCreateDate().toString());
    }

    @Test
    public void find() {
        logger.info("START find");
        ReportCard tempReportCard = reportCardDAO.find(reportCard.getId());
        Assert.assertEquals(tempReportCard.getId(), reportCard.getId());
        Assert.assertEquals(tempReportCard.getWorkingTime(), reportCard.getWorkingTime());
        Assert.assertEquals(tempReportCard.getDate().toString(), reportCard.getDate().toString());
        Assert.assertEquals(tempReportCard.getCreateDate().toString(), reportCard.getCreateDate().toString());
    }

    @Test
    public void update() {
        logger.info("START update");
        reportCard.setTask(secondTask);
        ReportCard tempReportCard = reportCardDAO.update(reportCard);
        Assert.assertEquals(tempReportCard.getId(), reportCard.getId());
        Assert.assertEquals(tempReportCard.getWorkingTime(), reportCard.getWorkingTime());
        Assert.assertEquals(tempReportCard.getDate().toString(), reportCard.getDate().toString());
        Assert.assertEquals(tempReportCard.getCreateDate().toString(), reportCard.getCreateDate().toString());
    }

    @Test
    public void removeById() {
        logger.info("START removeById");
        Assert.assertTrue(reportCardDAO.removeById(reportCard.getId()));
    }

    @Test
    public void remove() {
        logger.info("START remove");
        Assert.assertTrue(reportCardDAO.remove(reportCard));
    }
}
