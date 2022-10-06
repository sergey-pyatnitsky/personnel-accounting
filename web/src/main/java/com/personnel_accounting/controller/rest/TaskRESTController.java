package com.personnel_accounting.controller.rest;

import com.personnel_accounting.department.DepartmentService;
import com.personnel_accounting.domain.*;
import com.personnel_accounting.employee.EmployeeService;
import com.personnel_accounting.entity.dto.*;
import com.personnel_accounting.enums.TaskStatus;
import com.personnel_accounting.pagination.entity.Page;
import com.personnel_accounting.pagination.entity.PagingRequest;
import com.personnel_accounting.project.ProjectService;
import com.personnel_accounting.user.UserService;
import com.personnel_accounting.utils.AuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Time;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/task")
public class TaskRESTController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ConversionService conversionService;

    @PostMapping("/add")
    public ResponseEntity<?> addTask(@Valid @RequestBody TaskDTO taskDTO, Authentication authentication) {
        Task task = conversionService.convert(taskDTO, Task.class);
        task.setAssignee(employeeService.find(taskDTO.getAssignee().getId()));
        task.setReporter(employeeService.findByUser(
                userService.findByUsername(AuthenticationUtil.getUsernameFromAuthentication(authentication))));
        return new ResponseEntity<>(conversionService.convert(employeeService.addTaskInProject(
                projectService.find(taskDTO.getProject().getId()), task), TaskDTO.class),
                HttpStatus.OK);
    }

    @PostMapping("/get_all/by_status/{status}")
    public ResponseEntity<?> getAllTaskByStatusPaginated(@RequestBody PagingRequest pagingRequest,
                                                         @PathVariable TaskStatus status, Authentication authentication) {
        User user = userService.findByUsername(AuthenticationUtil.getUsernameFromAuthentication(authentication));
        return new ResponseEntity<>(getPage(projectService.findTaskByStatus(pagingRequest, status, user)
                        .stream().map(obj -> {
                            TaskDTO taskDTO = conversionService.convert(obj, TaskDTO.class);
                            taskDTO.setProject(conversionService.convert(obj.getProject(), ProjectDTO.class));
                            taskDTO.setAssignee(conversionService.convert(obj.getAssignee(), EmployeeDTO.class));
                            taskDTO.getAssignee().setProfile(conversionService.convert(obj.getAssignee().getProfile(), ProfileDTO.class));
                            taskDTO.setDepartment(conversionService.convert(obj.getProject().getDepartment(), DepartmentDTO.class));
                            return taskDTO;
                        }).collect(Collectors.toList()),
                pagingRequest.getDraw(), projectService.getTaskByStatusCount(status, user).intValue()),
                HttpStatus.OK);
    }

    @PostMapping("/get_all/by_status/{status}/employee")
    public ResponseEntity<?> getAllTaskByStatusAndEmployeePaginated(@RequestBody PagingRequest pagingRequest,
                                                                    @PathVariable TaskStatus status, Authentication authentication) {
        Employee employee = employeeService.findByUser(
                userService.findByUsername(AuthenticationUtil.getUsernameFromAuthentication(authentication)));
        return new ResponseEntity<>(getPage(projectService.findTaskByStatusAndEmployee(pagingRequest, employee, status)
                        .stream().map(obj -> {
                            TaskDTO taskDTO = conversionService.convert(obj, TaskDTO.class);
                            taskDTO.setProject(conversionService.convert(obj.getProject(), ProjectDTO.class));
                            taskDTO.setAssignee(conversionService.convert(obj.getAssignee(), EmployeeDTO.class));
                            taskDTO.getAssignee().setProfile(conversionService.convert(obj.getAssignee().getProfile(), ProfileDTO.class));
                            taskDTO.setDepartment(conversionService.convert(obj.getProject().getDepartment(), DepartmentDTO.class));
                            return taskDTO;
                        }).collect(Collectors.toList()),
                pagingRequest.getDraw(), projectService.getTaskByStatusAndEmployeeCount(employee, status).intValue()),
                HttpStatus.OK);
    }

    @PostMapping("/get_all/by_department/{id}/by_status/{status}")
    public ResponseEntity<?> getAllTaskInDepartmentsByStatusPaginated(@RequestBody PagingRequest pagingRequest,
                                                                      @PathVariable Long id, @PathVariable TaskStatus status) {
        Department department = departmentService.find(id);
        return new ResponseEntity<>(getPage(projectService.findTaskInDepartmentByStatus(pagingRequest, department, status)
                        .stream().map(obj -> {
                            TaskDTO taskDTO = conversionService.convert(obj, TaskDTO.class);
                            taskDTO.setProject(conversionService.convert(obj.getProject(), ProjectDTO.class));
                            taskDTO.setAssignee(conversionService.convert(obj.getAssignee(), EmployeeDTO.class));
                            taskDTO.getAssignee().setProfile(conversionService.convert(obj.getAssignee().getProfile(), ProfileDTO.class));
                            taskDTO.setDepartment(conversionService.convert(obj.getProject().getDepartment(), DepartmentDTO.class));
                            return taskDTO;
                        }).collect(Collectors.toList()),
                pagingRequest.getDraw(), projectService.getTaskByStatusInDepartmentCount(department, status).intValue()),
                HttpStatus.OK);
    }

    @PostMapping("/get_all/by_department/{id}/by_status/{status}/employee")
    public ResponseEntity<?> getAllTaskInDepartmentsByStatusAndEmployeePaginated(
            @RequestBody PagingRequest pagingRequest, @PathVariable Long id,
            @PathVariable TaskStatus status, Authentication authentication) {
        Department department = departmentService.find(id);
        Employee employee = employeeService.findByUser(
                userService.findByUsername(AuthenticationUtil.getUsernameFromAuthentication(authentication)));
        return new ResponseEntity<>(getPage(projectService.findTaskInDepartmentByStatusAndEmployee(pagingRequest, department, employee, status)
                        .stream().map(obj -> {
                            TaskDTO taskDTO = conversionService.convert(obj, TaskDTO.class);
                            taskDTO.setProject(conversionService.convert(obj.getProject(), ProjectDTO.class));
                            taskDTO.setAssignee(conversionService.convert(obj.getAssignee(), EmployeeDTO.class));
                            taskDTO.getAssignee().setProfile(conversionService.convert(obj.getAssignee().getProfile(), ProfileDTO.class));
                            taskDTO.setDepartment(conversionService.convert(obj.getProject().getDepartment(), DepartmentDTO.class));
                            return taskDTO;
                        }).collect(Collectors.toList()),
                pagingRequest.getDraw(), projectService.getTaskByStatusAndEmployeeInDepartmentCount(department, employee, status).intValue()),
                HttpStatus.OK);
    }

    @PostMapping("/get_all/project/{id}/by_status/{status}")
    public ResponseEntity<?> getAllTasksInProjectByStatusPaginated(@RequestBody PagingRequest pagingRequest,
                                                                   @PathVariable TaskStatus status, @PathVariable Long id) {
        Project project = projectService.find(id);
        return new ResponseEntity<>(getPage(projectService.findTaskInProjectByStatus(pagingRequest, project, status)
                        .stream().map(obj -> {
                            TaskDTO taskDTO = conversionService.convert(obj, TaskDTO.class);
                            taskDTO.setProject(conversionService.convert(obj.getProject(), ProjectDTO.class));
                            taskDTO.setAssignee(conversionService.convert(obj.getAssignee(), EmployeeDTO.class));
                            taskDTO.getAssignee().setProfile(conversionService.convert(obj.getAssignee().getProfile(), ProfileDTO.class));
                            taskDTO.setDepartment(conversionService.convert(obj.getProject().getDepartment(), DepartmentDTO.class));
                            return taskDTO;
                        }).collect(Collectors.toList()),
                pagingRequest.getDraw(), projectService.getTaskByStatusInProjectCount(project, status).intValue()),
                HttpStatus.OK);
    }

    @PostMapping("/get_all/project/{id}/by_status/{status}/employee")
    public ResponseEntity<?> getAllTasksByEmployeeInProjectWithStatusPaginated(@RequestBody PagingRequest pagingRequest,
                                                                               @PathVariable TaskStatus status, @PathVariable Long id,
                                                                               Authentication authentication) {
        Project project = projectService.find(id);
        Employee employee = employeeService.findByUser(
                userService.findByUsername(AuthenticationUtil.getUsernameFromAuthentication(authentication)));
        return new ResponseEntity<>(getPage(projectService.findTasksByEmployeeInProjectWithStatus(pagingRequest, employee, project, status)
                        .stream().map(obj -> {
                            TaskDTO taskDTO = conversionService.convert(obj, TaskDTO.class);
                            taskDTO.setProject(conversionService.convert(obj.getProject(), ProjectDTO.class));
                            taskDTO.setAssignee(conversionService.convert(obj.getAssignee(), EmployeeDTO.class));
                            taskDTO.getAssignee().setProfile(conversionService.convert(obj.getAssignee().getProfile(), ProfileDTO.class));
                            taskDTO.setDepartment(conversionService.convert(obj.getProject().getDepartment(), DepartmentDTO.class));
                            return taskDTO;
                        }).collect(Collectors.toList()),
                pagingRequest.getDraw(), projectService.getTasksByEmployeeInProjectWithStatusCount(employee, project, status).intValue()),
                HttpStatus.OK);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editTask(@PathVariable Long id, @Valid @RequestBody TaskDTO taskDTO) {
        Task task = employeeService.findTask(id);
        task.setName(taskDTO.getName());
        task.setDescription(taskDTO.getDescription());
        task.setTaskStatus(taskDTO.getStatus());
        employeeService.saveTask(task);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/edit/status/{id}")
    public ResponseEntity<?> editTaskStatus(@PathVariable Long id, @RequestBody String time) {
        Task task = employeeService.findTask(id);
        if(!task.getTaskStatus().equals(TaskStatus.CLOSED))employeeService.changeTaskStatus(task);
        if (!time.equals("null"))
            employeeService.trackTime(task, Time.valueOf(time.replaceAll("\"", "") + ":00"));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Page<?> getPage(List<?> list, int draw, int count) {
        Page<?> page = new Page<>(list);
        page.setRecordsTotal(count);
        page.setRecordsFiltered(count);
        page.setDraw(draw);
        return page;
    }
}
