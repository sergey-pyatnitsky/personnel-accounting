package com.service.employee;

import com.core.domain.*;
import com.core.enums.TaskStatus;
import com.dao.profile.ProfileDAO;
import com.dao.project.employee.EmployeeDAO;
import com.dao.report_card.ReportCardDAO;
import com.dao.task.TaskDAO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeDAO employeeDAO;
    private final TaskDAO taskDAO;
    private final ReportCardDAO reportCardDAO;
    private final ProfileDAO profileDAO;

    public EmployeeServiceImpl(EmployeeDAO employeeDAO, TaskDAO taskDAO, ReportCardDAO reportCardDAO, ProfileDAO profileDAO) {
        this.employeeDAO = employeeDAO;
        this.taskDAO = taskDAO;
        this.reportCardDAO = reportCardDAO;
        this.profileDAO = profileDAO;
    }

    @Override
    @Transactional
    public ReportCard assigneeTime(Date date, Time workingTime, Task task, Employee assignee) {
        return reportCardDAO.save(new ReportCard(date,
                task, assignee, workingTime));
    }

    @Override
    @Transactional
    public Task addTaskInProject(Project project, Task task) {
        task.setProject(project);
        return taskDAO.save(task);
    }

    @Override
    @Transactional
    public Task changeTaskStatus(Task task, TaskStatus taskStatus) {
        task.setTaskStatus(taskStatus);
        return taskDAO.save(task);
    }

    @Override
    @Transactional
    public Employee addProfileData(Employee employee, Profile profile) {
        profile = profileDAO.save(profile);
        employee.setProfile(profile);
        return employeeDAO.save(employee);
    }

    @Override
    @Transactional
    public Employee find(Long id) {
        return employeeDAO.find(id);
    }

    @Override
    @Transactional
    public List<Employee> findAll() {
        return employeeDAO.findAll();
    }

    @Override
    @Transactional
    public List<Employee> findByName(String name) {
        return employeeDAO.findByName(name);
    }

    @Override
    @Transactional
    public List<Employee> findByActive(boolean isActive) {
        return employeeDAO.findByActive(isActive);
    }

    @Override
    @Transactional
    public Employee findByUser(User user) {
        return employeeDAO.findByUser(user);
    }

    @Override
    @Transactional
    public List<Employee> findByDepartment(Department department) {
        return employeeDAO.findByDepartment(department);
    }

    @Override
    @Transactional
    public Employee save(Employee employee) {
        return employeeDAO.save(employee);
    }

    @Override
    @Transactional
    public Employee update(Employee employee) {
        return employeeDAO.update(employee);
    }

    @Override
    @Transactional
    public boolean removeById(Long id) {
        return employeeDAO.removeById(id);
    }

    @Override
    @Transactional
    public boolean remove(Employee employee) {
        return employeeDAO.remove(employee);
    }

    @Override
    @Transactional
    public boolean inactivate(Employee employee) {
        return employeeDAO.inactivate(employee);
    }

    @Override
    @Transactional
    public boolean activate(Employee employee) {
        return employeeDAO.activate(employee);
    }
}
