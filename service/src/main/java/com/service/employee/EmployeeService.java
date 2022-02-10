package com.service.employee;

import com.core.domain.*;
import com.core.enums.TaskStatus;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public interface EmployeeService {
    Employee addProfileData(Employee employee, Profile profile);
    ReportCard assigneeTime(Date date, Time workingTime, Task task, Employee assignee);
    Task addTaskInProject(Project project, Task task);
    Task changeTaskStatus(Task task, TaskStatus taskStatus);

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
