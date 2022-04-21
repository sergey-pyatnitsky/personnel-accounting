package com.personnel_accounting.employee;

import com.personnel_accounting.domain.Department;
import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.domain.Profile;
import com.personnel_accounting.domain.Project;
import com.personnel_accounting.domain.ReportCard;
import com.personnel_accounting.domain.Task;
import com.personnel_accounting.domain.User;
import com.personnel_accounting.employee_position.EmployeePositionDAO;
import com.personnel_accounting.enums.TaskStatus;
import com.personnel_accounting.pagination.entity.PagingRequest;
import com.personnel_accounting.profile.ProfileDAO;
import com.personnel_accounting.report_card.ReportCardDAO;
import com.personnel_accounting.task.TaskDAO;
import com.personnel_accounting.utils.ValidationUtil;
import com.personnel_accounting.validation.EmployeeValidator;
import com.personnel_accounting.validation.ProfileValidator;
import com.personnel_accounting.validation.TaskValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeDAO employeeDAO;

    @Autowired
    private TaskDAO taskDAO;

    @Autowired
    private ReportCardDAO reportCardDAO;

    @Autowired
    private ProfileDAO profileDAO;

    @Autowired
    private EmployeePositionDAO employeePositionDAO;

    @Autowired
    private EmployeeValidator employeeValidator;

    @Autowired
    private TaskValidator taskValidator;

    @Autowired
    private ProfileValidator profileValidator;

    @Override
    public ReportCard trackTime(Task task, Time time) {
        return reportCardDAO.save(new ReportCard(new Date(System.currentTimeMillis()), task, task.getAssignee(), time));
    }

    @Override
    public Task changeTaskStatus(Task task) {
        task.setTaskStatus(TaskStatus.values()[task.getTaskStatus().ordinal() + 1]);
        task.setModifiedDate(new Date(System.currentTimeMillis()));
        return taskDAO.save(task);
    }

    @Override
    public Task addTaskInProject(Project project, Task task) {
        task.setProject(project);
        task.setTaskStatus(TaskStatus.OPEN);
        task.setCreateDate(new Date(System.currentTimeMillis()));

        ValidationUtil.validate(task, taskValidator);
        return taskDAO.save(task);
    }

    @Override
    @Transactional
    public Employee addProfileData(Employee employee, Profile profile) {
        employee = employeeDAO.merge(employee);
        employee.getProfile().setSkills(profile.getSkills());
        employee.getProfile().setEmail(profile.getEmail());
        employee.getProfile().setPhone(profile.getPhone());
        employee.getProfile().setAddress(profile.getAddress());
        employee.getProfile().setEducation(profile.getEducation());

        ValidationUtil.validate(employee.getProfile(), profileValidator);
        return employeeDAO.save(employee);
    }

    @Override
    public Employee editEmployeeName(Employee employee, String name) {
        employee.setName(name);
        employee.setModifiedDate(new Date(System.currentTimeMillis()));

        ValidationUtil.validate(employee, employeeValidator);
        return employeeDAO.save(employee);
    }

    @Override
    public Task findTask(Long id) {
        return taskDAO.find(id);
    }

    @Override
    public Task saveTask(Task task) {
        ValidationUtil.validate(task, taskValidator);
        task.setModifiedDate(new Date(System.currentTimeMillis()));
        return taskDAO.save(task);
    }

    @Override
    @Transactional
    public Profile findProfileByEmployee(Employee employee) {
        return profileDAO.find(employeeDAO.find(employee.getId()).getProfile().getId());
    }

    @Override
    public List<Employee> findByNamePart(String namePart) {
        return employeeDAO.findByNamePart(namePart.trim());
    }

    @Override
    public List<Employee> findByPhonePart(String phonePart) {
        return profileDAO.findByPhonePart(phonePart.trim()).stream()
                .map(employeeDAO::findByProfile).collect(Collectors.toList());
    }

    @Override
    public List<Employee> findByEmailPart(String emailPart) {
        return profileDAO.findByEmailPart(emailPart.trim()).stream()
                .map(employeeDAO::findByProfile).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<Employee> getEmployeesWithProjectByDepartment(Department department, PagingRequest pagingRequest) {
        return employeeDAO.findByDepartmentPaginated(department, pagingRequest)
                .stream().filter(employee ->
                        employeePositionDAO.findByEmployee(employee)
                                .stream().filter(employeePosition -> employeePosition.getProject() != null)
                                .findFirst().orElse(null) != null
                ).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<Employee> getEmployeesWithOpenProjectByDepartment(Department department, PagingRequest pagingRequest) {
        return employeeDAO.findByDepartmentPaginated(department, pagingRequest)
                .stream().filter(employee ->
                        employeePositionDAO.findByEmployee(employee)
                                .stream().filter(employeePosition -> employeePosition.getProject() != null
                                        && employeePosition.getProject().getEndDate() == null)
                                .findFirst().orElse(null) != null
                ).collect(Collectors.toList());
    }

    @Override
    public int getEmployeesWithOpenProjectByDepartmentCount(Department department) {
        return (int) employeeDAO.findByDepartment(department)
                .stream().filter(employee ->
                        employeePositionDAO.findByEmployee(employee)
                                .stream().filter(employeePosition -> employeePosition.getProject() != null
                                        && employeePosition.getProject().getEndDate() == null)
                                .findFirst().orElse(null) != null
                ).count();
    }

    @Override
    public Employee find(Long id) {
        return employeeDAO.find(id);
    }

    @Override
    public List<Employee> findAll(PagingRequest pagingRequest) {
        return employeeDAO.findAll(pagingRequest);
    }

    @Override
    public Long getEmployeeCount() {
        return employeeDAO.getEmployeeCount();
    }

    @Override
    public Long getEmployeeByDepartmentCount(Department department) {
        return employeeDAO.getEmployeeByDepartmentCount(department);
    }

    @Override
    public Long getEmployeeByProjectCount(Project project) {
        return employeePositionDAO.getEmployeeByProjectCount(project);
    }

    @Override
    public List<Employee> findByName(String name) {
        return employeeDAO.findByName(name);
    }

    @Override
    public List<Employee> findByActive(boolean isActive) {
        return employeeDAO.findByActive(isActive);
    }

    @Override
    public Employee findByUser(User user) {
        return employeeDAO.findByUser(user);
    }

    @Override
    public List<Employee> findByDepartmentPaginated(Department department, PagingRequest pagingRequest) {
        return employeeDAO.findByDepartmentPaginated(department, pagingRequest);
    }

    @Override
    public List<Employee> findByDepartment(Department department) {
        return employeeDAO.findByDepartment(department);
    }

    @Override
    public Employee save(Employee employee) {
        ValidationUtil.validate(employee, employeeValidator);
        return employeeDAO.save(employee);
    }

    @Override
    public Employee merge(Employee employee) {
        ValidationUtil.validate(employee, employeeValidator);
        return employeeDAO.merge(employee);
    }

    @Override
    public boolean removeById(Long id) {
        Date date = new Date(System.currentTimeMillis());
        Employee employee = employeeDAO.find(id);
        employeePositionDAO.findByEmployee(employee)
                .forEach(employeePosition -> {
                    employeePosition.setActive(false);
                    employeePosition.setModifiedDate(date);
                    employeePosition.setEndDate(date);
                });
        List<Task> tasks = taskDAO.findByAssignee(employee);
        tasks.stream().filter(task -> task.getTaskStatus() == TaskStatus.IN_PROGRESS).collect(Collectors.toList())
                .forEach(task -> {
                    task.setModifiedDate(date);
                    task.setAssignee(null);
                    task.setTaskStatus(TaskStatus.OPEN);
                });
        tasks.stream().filter(task -> task.getTaskStatus() == TaskStatus.DONE).collect(Collectors.toList())
                .forEach(task -> {
                    task.setModifiedDate(date);
                    task.setTaskStatus(TaskStatus.CLOSED);
                    task.setAssignee(null);
                });
        return employeeDAO.removeById(id);
    }

    @Override
    public boolean remove(Employee employee) {
        return employeeDAO.remove(employee);
    }

    @Override
    public boolean inactivate(Employee employee) {
        Date date = new Date(System.currentTimeMillis());
        employeePositionDAO.findByEmployee(employee).forEach(employeePosition -> {
            employeePosition.setModifiedDate(date);
            employeePosition.setActive(false);
        });
        List<Task> tasks = taskDAO.findByAssignee(employee);
        tasks.stream().filter(task -> task.getTaskStatus() == TaskStatus.IN_PROGRESS).collect(Collectors.toList())
                .forEach(task -> {
                    task.setModifiedDate(date);
                    task.setAssignee(null);
                    task.setTaskStatus(TaskStatus.OPEN);
                });
        tasks.stream().filter(task -> task.getTaskStatus() == TaskStatus.DONE).collect(Collectors.toList())
                .forEach(task -> {
                    task.setModifiedDate(date);
                    task.setTaskStatus(TaskStatus.CLOSED);
                    task.setAssignee(null);
                });
        return employeeDAO.inactivate(employee);
    }

    @Override
    public boolean activate(Employee employee) {
        employeePositionDAO.findByEmployee(employee).forEach(employeePosition -> {
            employeePosition.setModifiedDate(new Date(System.currentTimeMillis()));
            employeePosition.setActive(true);
        });
        return employeeDAO.activate(employee);
    }

    @Override
    public ReportCard saveReportCard(ReportCard reportCard) {
        return reportCardDAO.save(reportCard);
    }

    @Override
    public ReportCard mergeReportCard(ReportCard reportCard) {
        return reportCardDAO.merge(reportCard);
    }

    @Override
    public Task createTask(Task task) {
        ValidationUtil.validate(task, taskValidator);
        return taskDAO.save(task);
    }

    @Override
    public Task mergeTask(Task task) {
        return taskDAO.merge(task);
    }
}
