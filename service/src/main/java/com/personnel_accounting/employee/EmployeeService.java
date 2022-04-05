package com.personnel_accounting.employee;

import com.personnel_accounting.domain.Department;
import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.domain.Profile;
import com.personnel_accounting.domain.Project;
import com.personnel_accounting.domain.ReportCard;
import com.personnel_accounting.domain.Task;
import com.personnel_accounting.domain.User;

import java.sql.Time;
import java.util.List;

public interface EmployeeService {
    List<Employee> getEmployeesWithProjectByDepartment(Department department);

    ReportCard trackTime(Task task, Time time);
    Task changeTaskStatus(Task task);
    Task addTaskInProject(Project project, Task task);
    Employee addProfileData(Employee employee, Profile profile);
    Profile findProfileByEmployee(Employee employee);

    List<Employee> findByNamePart(String namePart);//TODO test
    List<Employee> findByPhonePart(String phonePart);//TODO test
    List<Employee> findByEmailPart(String emailPart);//TODO test

    ReportCard saveReportCard(ReportCard reportCard);
    ReportCard mergeReportCard(ReportCard reportCard);

    Task findTask(Long id);
    Task saveTask(Task task);
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
