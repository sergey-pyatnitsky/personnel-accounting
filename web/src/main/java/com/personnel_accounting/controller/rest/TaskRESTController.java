package com.personnel_accounting.controller.rest;

import com.personnel_accounting.domain.Project;
import com.personnel_accounting.domain.Task;
import com.personnel_accounting.employee.EmployeeService;
import com.personnel_accounting.entity.dto.DepartmentDTO;
import com.personnel_accounting.entity.dto.EmployeeDTO;
import com.personnel_accounting.entity.dto.ProjectDTO;
import com.personnel_accounting.entity.dto.TaskDTO;
import com.personnel_accounting.enums.TaskStatus;
import com.personnel_accounting.exception.NoSuchDataException;
import com.personnel_accounting.pagination.entity.Page;
import com.personnel_accounting.pagination.entity.PagingRequest;
import com.personnel_accounting.project.ProjectService;
import com.personnel_accounting.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.sql.Time;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TaskRESTController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private MessageSource messageSource;

    @PostMapping("/api/task/add")
    public ResponseEntity<?> addTask(@Valid @RequestBody TaskDTO taskDTO, Authentication authentication) {
        Task task = conversionService.convert(taskDTO, Task.class);
        task.setAssignee(employeeService.find(taskDTO.getAssignee().getId()));
        task.setReporter(employeeService.findByUser(userService.findByUsername(authentication.getName())));
        return new ResponseEntity<>(conversionService.convert(employeeService.addTaskInProject(
                projectService.find(taskDTO.getProject().getId()), task), TaskDTO.class),
                HttpStatus.OK);
    }

    @PostMapping("/api/task/get_all/project/{id}/by_status/{status}")
    public ResponseEntity<?> getAllTasksByStatus(@RequestBody PagingRequest pagingRequest,
                                                 @PathVariable TaskStatus status, @PathVariable Long id) {
        Project project = projectService.find(id);
        return new ResponseEntity<>(getPage(projectService.findTaskInProjectByStatus(pagingRequest, project, status)
                        .stream().map(obj -> {
                            TaskDTO taskDTO = conversionService.convert(obj, TaskDTO.class);
                            taskDTO.setProject(conversionService.convert(obj.getProject(), ProjectDTO.class));
                            taskDTO.setAssignee(conversionService.convert(obj.getAssignee(), EmployeeDTO.class));
                            return taskDTO;
                        }).collect(Collectors.toList()),
                pagingRequest.getDraw(), projectService.getTaskByStatusCount(project, status).intValue()),
                HttpStatus.OK);
    }

    @GetMapping("/api/task/get_all/project/{project_id}/by_status/{status}/employee")
    public ResponseEntity<?> getAllTasksByEmployeeInProjectWithStatus(
            @PathVariable TaskStatus status, @PathVariable Long project_id, Authentication authentication) {
        List<TaskDTO> tasks = projectService.findTasksByEmployeeInProjectWithStatus(
                        employeeService.findByUser(userService.findByUsername(authentication.getName())),
                        projectService.find(project_id), status)
                .stream().map(obj -> {
                    TaskDTO taskDTO = conversionService.convert(obj, TaskDTO.class);
                    taskDTO.setProject(conversionService.convert(obj.getProject(), ProjectDTO.class));
                    taskDTO.getProject().setDepartment(
                            conversionService.convert(obj.getProject().getDepartment(), DepartmentDTO.class));
                    return taskDTO;
                }).collect(Collectors.toList());
        if (tasks.size() == 0) throw new NoSuchDataException(
                messageSource.getMessage("task.error.existing", null, null));
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PutMapping("/api/task/edit/{id}")
    public ResponseEntity<?> editTask(@PathVariable Long id, @Valid @RequestBody TaskDTO taskDTO) {
        Task task = employeeService.findTask(id);
        task.setName(taskDTO.getName());
        task.setDescription(taskDTO.getDescription());
        task.setTaskStatus(taskDTO.getStatus());
        employeeService.saveTask(task);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/api/task/edit/status/{id}")
    public ResponseEntity<?> editTaskStatus(@PathVariable Long id, @RequestBody String time) {
        Task task = employeeService.findTask(id);
        employeeService.changeTaskStatus(task);
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
