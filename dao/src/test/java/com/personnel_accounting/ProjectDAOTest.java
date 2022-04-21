package com.personnel_accounting;

import com.personnel_accounting.configuration.DAOTestConfiguration;
import com.personnel_accounting.department.DepartmentDAO;
import com.personnel_accounting.domain.Department;
import com.personnel_accounting.domain.Project;
import com.personnel_accounting.pagination.entity.Column;
import com.personnel_accounting.pagination.entity.Direction;
import com.personnel_accounting.pagination.entity.Order;
import com.personnel_accounting.pagination.entity.PagingRequest;
import com.personnel_accounting.pagination.entity.Search;
import com.personnel_accounting.project.ProjectDAO;
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
@ContextConfiguration(classes = DAOTestConfiguration.class)
public class ProjectDAOTest {

    private static final Logger logger = LogManager.getLogger("ProjectDAOTest logger");

    @Autowired
    private ProjectDAO projectDAO;
    private static Project project;
    private static Project secondProject;

    @Autowired
    private DepartmentDAO departmentDAO;
    private static Department department;

    @After
    public void deleteProjectEntity() {
        logger.info("START deleteProjectEntity");
        try {
            projectDAO.remove(project);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            projectDAO.remove(secondProject);
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
        department = departmentDAO.save(
                new Department("Отдел Java Разработки", true));
        project = projectDAO.save(
                new Project("Банковская система", department, true));
        secondProject = projectDAO.save(
                new Project("Система учёта товаров на складе", department, true));

        System.out.println(project.getName() + " - " + project.getId());
        System.out.println(secondProject.getName() + " - " + project.getId());
        System.out.println();
        System.out.println(department.getName() + " - " + department.getId());
    }

    @Test
    public void findByDepartment() {
        logger.info("START findByDepartment");
        List<Project> projectListFromDB = projectDAO.findByDepartment(department);

        Project tempProject = projectListFromDB.get(projectListFromDB.size() - 1);
        Assert.assertEquals(tempProject.getName(), "Система учёта товаров на складе");
        Assert.assertTrue(tempProject.isActive());

        tempProject = projectListFromDB.get(projectListFromDB.size() - 2);
        Assert.assertEquals(tempProject.getName(), "Банковская система");
        Assert.assertTrue(tempProject.isActive());
    }

    @Test
    public void save() {
        logger.info("START save");
        Assert.assertEquals(project.getName(), "Банковская система");
        Assert.assertTrue(project.isActive());
    }

    @Test
    public void saveList(){
        logger.info("START saveList");
        deleteProjectEntity();
        List<Project> projectsFromDB = projectDAO.save(entityToListSave());
        Project tempProject = projectsFromDB.get(projectsFromDB.size() - 2);
        Assert.assertEquals(project.getName(), "Банковская система");
        Assert.assertTrue(project.isActive());
    }

    private List<Project> entityToListSave(){
        logger.info("START entityToListSave");
        List<Project> projects = new ArrayList<>();
        department = departmentDAO.save(
                new Department("Отдел Java Разработки", true));
        project = new Project("Банковская система", department, true);
        secondProject = new Project("Система учёта товаров на складе", department, true);
        projects.add(project);
        projects.add(secondProject);
        return projects;
    }

    @Test
    public void findByActive() {
        logger.info("START findByActive");
        List<Project> projectListFromDB = projectDAO.findByActive(true);
        projectListFromDB.forEach(obj -> Assert.assertTrue(obj.isActive()));
    }

    @Test
    public void findByName() {
        logger.info("START findByName");
        List<Project> projectFromDB = projectDAO.findByName("Банковская система");
        Assert.assertEquals(projectFromDB.get(projectFromDB.size() - 1).getName(), "Банковская система");
    }

    @Test
    public void find() {
        logger.info("START find");
        Project tempProject = projectDAO.find(project.getId());

        Assert.assertEquals(tempProject.getName(), "Банковская система");
        Assert.assertTrue(tempProject.isActive());
    }

    private PagingRequest getPagingRequestGetAll(Direction direction){
        PagingRequest pagingRequest = new PagingRequest();
        pagingRequest.setDraw(1);

        List<Column> columns = new ArrayList<>();
        columns.add(new Column("id", true, true, new Search("", "false")));
        columns.add(new Column("name", true, true, new Search("", "false")));
        columns.add(new Column("department", true, true, new Search("", "false")));
        columns.add(new Column("isActive", true, true, new Search("", "false")));
        columns.add(new Column("startDate", true, true, new Search("", "false")));
        columns.add(new Column("endDate", true, true, new Search("", "false")));
        pagingRequest.setColumns(columns);

        List<Order> orders = new ArrayList<>();
        orders.add(new Order(0, direction));
        pagingRequest.setOrder(orders);
        pagingRequest.setStart(0);
        pagingRequest.setLength(10);
        pagingRequest.setSearch(new Search("", "false"));

        return pagingRequest;
    }

    @Test
    public void findAll() {
        logger.info("START findAll");
        List<Project> projectListFromDB = projectDAO.findAll(getPagingRequestGetAll(Direction.asc));

        Project tempProject = projectListFromDB.get(projectListFromDB.size() - 1);
        Assert.assertEquals(tempProject.getName(), "Система учёта товаров на складе");
        Assert.assertTrue(tempProject.isActive());

        tempProject = projectListFromDB.get(projectListFromDB.size() - 2);
        Assert.assertEquals(tempProject.getName(), "Банковская система");
        Assert.assertTrue(tempProject.isActive());
    }

    @Test
    public void merge() {
        logger.info("START merge");
        project.setName("Банковская система белагопромбанка");
        Assert.assertEquals(projectDAO.merge(project).getName(), "Банковская система белагопромбанка");
    }

    @Test
    public void inactivateById() {
        logger.info("START inactivateById");
        Assert.assertTrue(projectDAO.inactivateById(project.getId()));
        Assert.assertFalse(projectDAO.find(project.getId()).isActive());
    }

    @Test
    public void activateById() {
        logger.info("START activateById");
        project.setActive(false);
        Assert.assertTrue(projectDAO.activateById(project.getId()));
        Assert.assertTrue(projectDAO.find(project.getId()).isActive());
    }

    @Test
    public void inactivate() {
        logger.info("START inactivate");
        Assert.assertTrue(projectDAO.inactivate(project));
        Assert.assertFalse(projectDAO.find(project.getId()).isActive());
    }

    @Test
    public void activate() {
        logger.info("START activate");
        project.setActive(false);
        Assert.assertTrue(projectDAO.activate(project));
        Assert.assertTrue(projectDAO.find(project.getId()).isActive());
    }

    @Test
    public void removeById() {
        logger.info("START removeById");
        Assert.assertTrue(projectDAO.removeById(secondProject.getId()));
    }

    @Test
    public void remove() {
        logger.info("START remove");
        Assert.assertTrue(projectDAO.remove(project));
    }
}
