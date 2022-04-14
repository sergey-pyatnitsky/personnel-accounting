package com.personnel_accounting.employee;

import com.personnel_accounting.domain.*;
import com.personnel_accounting.enums.Role;
import com.personnel_accounting.pagination.entity.PagingRequest;

import java.sql.Time;
import java.util.List;

public interface EmployeeService {
    List<Employee> getEmployeesWithProjectByDepartment(Department department, PagingRequest pagingRequest);

    ReportCard trackTime(Task task, Time time);
    Task changeTaskStatus(Task task);
    Task addTaskInProject(Project project, Task task);
    Employee addProfileData(Employee employee, Profile profile);
    Employee editEmployeeName(Employee employee, String name);
    Profile findProfileByEmployee(Employee employee);

    List<Employee> findByNamePart(String namePart);//TODO test
    List<Employee> findByPhonePart(String phonePart);//TODO test
    List<Employee> findByEmailPart(String emailPart);//TODO test

    ReportCard saveReportCard(ReportCard reportCard);
    ReportCard mergeReportCard(ReportCard reportCard);

    Long getEmployeeCount();

    Task findTask(Long id);
    Task saveTask(Task task);
    Task createTask(Task task);
    Task mergeTask(Task task);

    Employee find(Long id);
    List<Employee> findAll(PagingRequest pagingRequest);
    List<Employee> findByName(String name);
    List<Employee> findByActive(boolean isActive);
    Employee findByUser(User user);
    List<Employee> findByDepartment(Department department);
    List<Employee> getEmployeesWithOpenProjectByDepartment(Department department, PagingRequest pagingRequest);
    List<Employee> findByDepartmentPaginated(Department department, PagingRequest pagingRequest);

    Employee save(Employee employee);
    Employee update(Employee employee);
    boolean removeById(Long id);
    boolean remove(Employee employee);

    boolean inactivate(Employee employee);
    boolean activate(Employee employee);
}
