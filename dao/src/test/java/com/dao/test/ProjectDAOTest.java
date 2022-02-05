package com.dao.test;

import com.core.domain.Department;
import com.core.domain.Project;
import com.dao.configuration.DAOConfiguration;
import com.dao.department.DepartmentDAO;
import com.dao.project.ProjectDAO;
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
public class ProjectDAOTest {

    @Autowired
    private ProjectDAO projectDAO;
    private static Project project;
    private static Project secondProject;

    @Autowired
    private DepartmentDAO departmentDAO;
    private static Department department;

    @After
    public void deleteProjectEntity() {
        try {
            projectDAO.remove(project);
        } catch (Exception e) {
        }
        try {
            projectDAO.remove(secondProject);
        } catch (Exception e) {
        }
        try {
            departmentDAO.remove(department);
        } catch (Exception e) {
        }
    }

    @Before
    public void entityToPersist() {
        department = departmentDAO.update(
                new Department("Отдел Java Разработки", true));
        project = projectDAO.update(
                new Project("Банковская система", department, true));
        secondProject = projectDAO.update(
                new Project("Система учёта товаров на складе", department, true));

        System.out.println(project.getName() + " - " + project.getId());
        System.out.println(secondProject.getName() + " - " + project.getId());
        System.out.println();
        System.out.println(department.getName() + " - " + department.getId());
    }

    @Test
    public void findByDepartment() {
        List<Project> projectListFromDB = projectDAO.findByDepartment(department);
        projectListFromDB.forEach(obj -> Assert.assertEquals(obj.getDepartment(), department));
    }

    @Test
    public void save() {
        Assert.assertEquals(project.getName(), "Банковская система");
        Assert.assertEquals(project.getDepartment(), department);
        Assert.assertTrue(project.isActive());
    }

    @Test
    public void findByActive() {
        List<Project> projectListFromDB = projectDAO.findByActive(true);
        projectListFromDB.forEach(obj -> Assert.assertTrue(obj.isActive()));
    }

    @Test
    public void findByName() {
        List<Project> projectFromDB = projectDAO.findByName("Банковская система");
        Assert.assertEquals(projectFromDB.get(projectFromDB.size() - 1), project);
    }

    @Test
    public void find() {
        Assert.assertEquals(projectDAO.find(project.getId()), project);
    }

    @Test
    public void findAll() {
        List<Project> projectListFromDB = projectDAO.findAll();
        Assert.assertEquals(projectListFromDB.get(projectListFromDB.size() - 1), secondProject);
        Assert.assertEquals(projectListFromDB.get(projectListFromDB.size() - 2), project);
    }

    @Test
    public void update() {
        project.setName("Банковская система белагопромбанка");
        Assert.assertEquals(projectDAO.update(project), project);
    }

    @Test
    public void inactivateById() {
        Assert.assertTrue(projectDAO.inactivateById(project.getId()));
    }

    @Test
    public void activateById() {
        project.setActive(false);
        Assert.assertTrue(projectDAO.activateById(project.getId()));
    }

    @Test
    public void inactivate() {
        Assert.assertTrue(projectDAO.inactivate(project));
    }

    @Test
    public void activate() {
        project.setActive(false);
        Assert.assertTrue(projectDAO.activate(project));
    }

    @Test
    public void removeById() {
        Assert.assertTrue(projectDAO.removeById(secondProject.getId()));
    }

    @Test
    public void remove() {
        Assert.assertTrue(projectDAO.remove(project));
    }
}
