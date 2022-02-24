package com.service.employee;

import com.core.domain.*;
import com.core.enums.TaskStatus;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public interface EmployeeService {
    ReportCard trackTime(ReportCard reportCard);
    Task changeTaskStatus(Task task, TaskStatus taskStatus);
    Task addTaskInProject(Project project, Task task);
    Employee addProfileData(Employee employee, Profile profile);
    Profile findProfileByEmployee(Employee employee);

    ReportCard saveReportCard(ReportCard reportCard);
    ReportCard mergeReportCard(ReportCard reportCard);

    Task createTask(Task task);
    Task mergeTask(Task task);

    Employee find(Long id);
    List<Employee> findAll();
    List<Employee> findByName(String name);
    List<Employee> findByActive(boolean isActive);
    Employee findByUser(User user);
    List<Employee> findByDepartment(Department department);

    Employee save(Employee employee);
    Employee update(Employee employee);
    boolean removeById(Long id);
    boolean remove(Employee employee);

    boolean inactivate(Employee employee);
    boolean activate(Employee employee);
}
