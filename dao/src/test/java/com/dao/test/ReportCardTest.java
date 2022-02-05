package com.dao.test;

import com.core.domain.*;
import com.core.enums.Role;
import com.core.enums.TaskStatus;
import com.dao.configuration.DAOConfiguration;
import com.dao.department.DepartmentDAO;
import com.dao.employee.EmployeeDAO;
import com.dao.profile.ProfileDAO;
import com.dao.project.ProjectDAO;
import com.dao.report_card.ReportCardDAO;
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
import java.sql.Date;
import java.sql.Time;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DAOConfiguration.class)
@Transactional
public class ReportCardTest {

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
        try {
            reportCardDAO.remove(reportCard);
        } catch (Exception e) {
        }
        try {
            reportCardDAO.remove(secondReportCard);
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
        Assert.assertEquals(reportCard.getTask(), task);
        Assert.assertEquals(reportCard.getDate(), date);
        Assert.assertEquals(reportCard.getEmployee(), assignee);
        Assert.assertEquals(reportCard.getWorkingTime(), new Time(2, 30, 0));
    }

    @Test
    public void findAll() {
        List<ReportCard> repostCardFromDB = reportCardDAO.findAll();
        Assert.assertEquals(repostCardFromDB.get(repostCardFromDB.size() - 1), secondReportCard);
        Assert.assertEquals(repostCardFromDB.get(repostCardFromDB.size() - 2), reportCard);
    }

    @Test
    public void findByDate() {
        List<ReportCard> repostCardFromDB = reportCardDAO.findByDate(date);
        Assert.assertEquals(repostCardFromDB.get(repostCardFromDB.size() - 1), secondReportCard);
        Assert.assertEquals(repostCardFromDB.get(repostCardFromDB.size() - 2), reportCard);
    }

    @Test
    public void findByTask() {
        Assert.assertEquals(reportCardDAO.findByTask(task), reportCard);
    }

    @Test
    public void findByEmployee() {
        List<ReportCard> repostCardFromDB = reportCardDAO.findByEmployee(assignee);
        Assert.assertEquals(repostCardFromDB.get(repostCardFromDB.size() - 1), secondReportCard);
        Assert.assertEquals(repostCardFromDB.get(repostCardFromDB.size() - 2), reportCard);
    }

    @Test
    public void find() {
        Assert.assertEquals(reportCardDAO.find(reportCard.getId()), reportCard);
    }

    @Test
    public void update() {
        reportCard.setTask(secondTask);
        Assert.assertEquals(reportCardDAO.update(reportCard), reportCard);
    }

    @Test
    public void removeById() {
        Assert.assertTrue(reportCardDAO.removeById(reportCard.getId()));
    }

    @Test
    public void remove() {
        Assert.assertTrue(reportCardDAO.remove(reportCard));
    }
}
