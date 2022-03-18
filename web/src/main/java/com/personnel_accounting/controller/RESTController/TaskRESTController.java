package com.personnel_accounting.controller.RESTController;

import com.personnel_accounting.domain.Task;
import com.personnel_accounting.employee.EmployeeService;
import com.personnel_accounting.entity.dto.TaskDTO;
import com.personnel_accounting.project.ProjectService;
import com.personnel_accounting.user.UserService;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskRESTController {
    private final EmployeeService employeeService;
    private final UserService userService;
    private final ProjectService projectService;
    private final ConversionService conversionService;

    public TaskRESTController(EmployeeService employeeService, UserService userService, ProjectService projectService, ConversionService conversionService) {
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

    /*@GetMapping("/api/task/get_all")
    public ResponseEntity<?> getAllTasks() {
        return new ResponseEntity<>(
                departmentService.findAll().stream()
                        .map(department -> conversionService.convert(department, DepartmentDTO.class))
                        .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @PutMapping("/api/department/activate/{id}")
    public ResponseEntity<?> activateDepartment(@PathVariable Long id) {
        Department department = departmentService.find(id);
        return departmentService.activate(department)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping("/api/department/inactivate/{id}")
    public ResponseEntity<?> inactivateDepartment(@PathVariable Long id) {
        Department department = departmentService.find(id);
        return departmentService.inactivate(department)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping("/api/department/edit/{id}")
    public ResponseEntity<?> editDepartment(@PathVariable Long id, @RequestBody DepartmentDTO departmentDTO) {
        Department department = departmentService.find(id);
        department.setName(departmentDTO.getName());
        department.setModifiedDate(new Date(System.currentTimeMillis()));
        departmentService.save(department);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/api/department/remove/{id}") //FIXME трабл с удалеием
    public ResponseEntity<?> removeDepartment(@PathVariable Long id) {
        return departmentService.removeById(id)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.CONFLICT);
    }*/
}
