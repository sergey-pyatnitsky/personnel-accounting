package com.personnel_accounting;

import com.personnel_accounting.configuration.ServiceTestConfiguration;
import com.personnel_accounting.department.DepartmentService;
import com.personnel_accounting.domain.Department;
import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.domain.EmployeePosition;
import com.personnel_accounting.domain.Position;
import com.personnel_accounting.domain.Project;
import com.personnel_accounting.domain.User;
import com.personnel_accounting.employee.EmployeeService;
import com.personnel_accounting.employee_position.EmployeePositionDAO;
import com.personnel_accounting.enums.Role;
import com.personnel_accounting.position.PositionDAO;
import com.personnel_accounting.project.ProjectService;
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

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServiceTestConfiguration.class)
public class ProjectServiceTest {

    private static final Logger logger = LogManager.getLogger("ProjectServiceTest logger");

    @Autowired
    private ProjectService projectService;
    private Project project;
    private Project secondProject;

    @Autowired
    private DepartmentService departmentService;
    private Department department;
    private Department secondDepartment;

    @Autowired
    private EmployeeService employeeService;
    private Employee employee;

    @Autowired
    private UserService userService;
    private User user;

    @Autowired
    private EmployeePositionDAO employeePositionDAO;
    private EmployeePosition employeePosition;
    private EmployeePosition secondEmployeePosition;

    @Autowired
    private PositionDAO positionDAO;
    private Position position;
    private Position secondPosition;

    @After
    public void deleteProjectEntity() {
        logger.info("START deleteProjectEntity");
        try {
            employeePositionDAO.remove(employeePosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            employeePositionDAO.remove(secondEmployeePosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            positionDAO.remove(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            positionDAO.remove(secondPosition);
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
            employeeService.remove(employee);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            userService.remove(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            departmentService.remove(department);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            departmentService.remove(secondDepartment);
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
        employeePositionDAO.remove(employeePosition);
        employeePosition = projectService.assignToProject(employee, project, position);

        Assert.assertEquals(employeePosition.getId(), employeePosition.getId());
        Assert.assertEquals(employeePosition.getCreateDate().toString(),
                employeePosition.getCreateDate().toString());

        EmployeePosition tempEmployeePosition = projectService.findProjectEmployeePositions(employee, project).stream()
                .filter(obj -> obj.getId().equals(employeePosition.getId())).findFirst().orElse(null);

        assert tempEmployeePosition != null;
        Assert.assertEquals(tempEmployeePosition.getId(), employeePosition.getId());
        Assert.assertEquals(tempEmployeePosition.getCreateDate().toString(),
                employeePosition.getCreateDate().toString());
    }

    @Test
    public void addProject() {
        logger.info("START addProject");
        secondDepartment = departmentService.save(
                new Department("Отдел C# Разработки", true));
        secondProject = new Project("Банковская система Агропромыва", department, true);
        secondProject = projectService.addProject(secondProject, secondDepartment.getId());
        Assert.assertEquals(projectService.find(secondProject.getId()).getDepartment().getId(), secondDepartment.getId());
    }

    @Test
    public void findDepartmentByUser() {
        logger.info("START findDepartmentByUser");
        Department tempDepartment = projectService.findDepartmentByUser(user);
        Assert.assertEquals(tempDepartment.getName(), "Отдел Java Разработки");
        Assert.assertTrue(tempDepartment.isActive());
    }

    @Test
    public void findProjectEmployeePositions() {
        logger.info("START findProjectEmployeePositions");
        EmployeePosition tempEmployeePosition = projectService.findProjectEmployeePositions(employee, project).stream()
                .filter(obj -> obj.getId().equals(employeePosition.getId())).findFirst().orElse(null);

        assert tempEmployeePosition != null;
        Assert.assertEquals(tempEmployeePosition.getId(), employeePosition.getId());
        Assert.assertEquals(tempEmployeePosition.getCreateDate().toString(),
                employeePosition.getCreateDate().toString());
    }

    @Test
    public void changeEmployeeStateInProject() {
        logger.info("START changeEmployeeStateInProject");
        employeePosition =
                projectService.changeEmployeeStateInProject(employeePosition, true);
        Assert.assertEquals(employeePosition.getId(), employeePosition.getId());
        Assert.assertEquals(employeePosition.getCreateDate().toString(),
                employeePosition.getCreateDate().toString());
        Assert.assertTrue(employeePosition.isActive());

        EmployeePosition tempEmployeePosition = projectService.findProjectEmployeePositions(employee, project).stream()
                .filter(obj -> obj.getId().equals(employeePosition.getId())).findFirst().orElse(null);
        assert tempEmployeePosition != null;
        Assert.assertEquals(tempEmployeePosition.getId(), employeePosition.getId());
        Assert.assertEquals(tempEmployeePosition.getCreateDate().toString(),
                employeePosition.getCreateDate().toString());
        Assert.assertTrue(tempEmployeePosition.isActive());
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

    @Test
    public void addNewPosition() {
        logger.info("START addNewPosition");
        Position position = projectService.addNewPosition(new Position("Архитектор БД"));
        Assert.assertEquals(position.getName(), "Архитектор БД");

        position = positionDAO.findByName("Архитектор БД");
        Assert.assertEquals(position.getName(), "Архитектор БД");

        positionDAO.remove(position);
    }

    @Test
    public void changeEmployeePositionInProject() {
        logger.info("START changeEmployeePositionInProject");
        secondPosition = positionDAO.save(new Position("Архитектор БД"));
        secondEmployeePosition = projectService.changeEmployeePositionInProject(
                employeePosition, secondPosition);

        Assert.assertEquals(secondEmployeePosition.getId(), employeePosition.getId());
        Assert.assertEquals(secondEmployeePosition.getCreateDate().toString(),
                employeePosition.getCreateDate().toString());
        Assert.assertFalse(secondEmployeePosition.isActive());

        Assert.assertEquals(secondEmployeePosition.getPosition().getId(), secondPosition.getId());
        Assert.assertEquals(secondEmployeePosition.getPosition().getName(), secondPosition.getName());

        EmployeePosition tempEmployeePosition = projectService.findProjectEmployeePositions(employee, project).stream()
                .filter(obj -> obj.getId().equals(employeePosition.getId())).findFirst().orElse(null);
        assert tempEmployeePosition != null;
        Assert.assertEquals(tempEmployeePosition.getId(), employeePosition.getId());
        Assert.assertEquals(tempEmployeePosition.getCreateDate().toString(),
                employeePosition.getCreateDate().toString());
        Assert.assertFalse(tempEmployeePosition.isActive());

        Assert.assertEquals(tempEmployeePosition.getPosition().getId(), secondPosition.getId());
        Assert.assertEquals(tempEmployeePosition.getPosition().getName(), secondPosition.getName());
    }
}
