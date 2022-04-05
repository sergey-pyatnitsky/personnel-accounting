package com.personnel_accounting.controller.RESTController;

import com.personnel_accounting.domain.Task;
import com.personnel_accounting.employee.EmployeeService;
import com.personnel_accounting.entity.dto.DepartmentDTO;
import com.personnel_accounting.entity.dto.EmployeeDTO;
import com.personnel_accounting.entity.dto.ProjectDTO;
import com.personnel_accounting.entity.dto.TaskDTO;
import com.personnel_accounting.enums.TaskStatus;
import com.personnel_accounting.project.ProjectService;
import com.personnel_accounting.user.UserService;
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

import java.sql.Time;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TaskRESTController {
    private final EmployeeService employeeService;
    private final UserService userService;
    private final ProjectService projectService;
    private final ConversionService conversionService;

    public TaskRESTController(EmployeeService employeeService, UserService userService,
                              ProjectService projectService, ConversionService conversionService) {
        this.employeeService = employeeService;
        this.userService = userService;
        this.projectService = projectService;
        this.conversionService = conversionService;
    }

    @PostMapping("/api/task/add")
    public ResponseEntity<?> addTask(@RequestBody TaskDTO taskDTO, Authentication authentication) {
        Task task = conversionService.convert(taskDTO, Task.class);
        task.setAssignee(employeeService.find(taskDTO.getAssignee().getId()));
        task.setReporter(employeeService.findByUser(userService.findByUsername(authentication.getName())));
        employeeService.addTaskInProject(projectService.find(taskDTO.getProject().getId()), task);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/task/get_all/project/{id}/by_status/{status}")
    public ResponseEntity<?> getAllTasksByStatus(@PathVariable TaskStatus status, @PathVariable Long id) {
        List<TaskDTO> tasks = projectService.findTaskInProjectByStatus(projectService.find(id), status)
                .stream().map(obj -> {
                    TaskDTO taskDTO = conversionService.convert(obj, TaskDTO.class);
                    taskDTO.setProject(conversionService.convert(obj.getProject(), ProjectDTO.class));
                    taskDTO.setAssignee(conversionService.convert(obj.getAssignee(), EmployeeDTO.class));
                    return taskDTO;
                }).collect(Collectors.toList());

        return tasks.size() != 0
                ? new ResponseEntity<>(tasks, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.CONFLICT);
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
        return tasks.size() != 0
                ? new ResponseEntity<>(tasks, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping("/api/task/edit/{id}")
    public ResponseEntity<?> editTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
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
        if (time != null)
            employeeService.trackTime(task, Time.valueOf(time.replaceAll("\"", "") + ":00"));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
