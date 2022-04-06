package com.personnel_accounting.controller.restController;

import com.personnel_accounting.department.DepartmentService;
import com.personnel_accounting.domain.Department;
import com.personnel_accounting.domain.EmployeePosition;
import com.personnel_accounting.domain.Project;
import com.personnel_accounting.domain.User;
import com.personnel_accounting.employee.EmployeeService;
import com.personnel_accounting.entity.dto.DepartmentDTO;
import com.personnel_accounting.entity.dto.EmployeePositionDTO;
import com.personnel_accounting.entity.dto.ProjectDTO;
import com.personnel_accounting.enums.Role;
import com.personnel_accounting.exeption.ActiveStatusDataException;
import com.personnel_accounting.exeption.ExistingDataException;
import com.personnel_accounting.exeption.NoSuchDataException;
import com.personnel_accounting.exeption.OperationExecutionException;
import com.personnel_accounting.project.ProjectService;
import com.personnel_accounting.user.UserService;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class ProjectRESTController {
    private final ProjectService projectService;
    private final UserService userService;
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final ConversionService conversionService;

    public ProjectRESTController(ProjectService projectService, UserService userService,
                                 EmployeeService employeeService, DepartmentService departmentService,
                                 ConversionService conversionService) {
        this.projectService = projectService;
        this.userService = userService;
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.conversionService = conversionService;
    }

    @PostMapping("/api/project/add")
    public ResponseEntity<?> addProject(@RequestBody ProjectDTO projectDTO, Authentication authentication) {
        User user = userService.find(authentication.getName());
        if (userService.getAuthorityByUsername(user.getUsername()).getRole() == Role.DEPARTMENT_HEAD)
            projectDTO.getDepartment().setId(projectService.findDepartmentByUser(user).getId());
        employeeService.findByUser(user);
        Project project = conversionService.convert(projectDTO, Project.class);
        project = projectService.addProject(project, projectDTO.getDepartment().getId());
        return new ResponseEntity<>(conversionService.convert(project, ProjectDTO.class), HttpStatus.OK);
    }

    @GetMapping("/api/project/get_all/open")
    public ResponseEntity<?> getAllOpenProjects() {
        return new ResponseEntity<>(
                projectService.findAll()
                        .stream().filter(project -> project.getEndDate() == null).collect(Collectors.toList())
                        .stream().map(project -> {
                            ProjectDTO projectDTO = conversionService.convert(project, ProjectDTO.class);
                            projectDTO.setDepartment(conversionService.convert(project.getDepartment(), DepartmentDTO.class));
                            return projectDTO;
                        }).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @GetMapping("/api/project/by_employee/open/{id}")
    public ResponseEntity<?> getAllOpenProjectsByEmployee(@PathVariable Long id) {
        return new ResponseEntity<>(
                projectService.findEmployeePositions(employeeService.find(id))
                        .stream().filter(employeePosition ->
                                employeePosition.isActive() && employeePosition.getEndDate() == null)
                        .collect(Collectors.toList())
                        .stream().map(employeePosition ->
                                conversionService.convert(employeePosition.getProject(), ProjectDTO.class))
                        .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @GetMapping("/api/project/by_department/open/{id}")
    public ResponseEntity<?> getAllOpenProjectsByDepartment(@PathVariable Long id, Authentication authentication) {
        Department department;
        if (id != 0) department = departmentService.find(id);
        else
            department = employeeService.findByUser(userService.findByUsername(authentication.getName())).getDepartment();
        List<ProjectDTO> projects = departmentService.findProjects(department)
                .stream().filter(Project::isActive).collect(Collectors.toList())
                .stream().map(project -> conversionService.convert(project, ProjectDTO.class)).collect(Collectors.toList());
        if(projects.size() == 0) throw new NoSuchDataException("В данном отделе отсутствуют открытые проекты");
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/api/project/get_all/closed")
    public ResponseEntity<?> getAllClosedProjects() {
        return new ResponseEntity<>(
                projectService.findAll()
                        .stream().filter(project -> project.getEndDate() != null).collect(Collectors.toList())
                        .stream().map(project -> {
                            ProjectDTO projectDTO = conversionService.convert(project, ProjectDTO.class);
                            projectDTO.setDepartment(conversionService.convert(project.getDepartment(), DepartmentDTO.class));
                            return projectDTO;
                        }).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @PutMapping("/api/project/activate/{id}")
    public ResponseEntity<?> activateProject(@PathVariable Long id) {
        if(!projectService.activate(projectService.find(id)))
            throw new OperationExecutionException("Ошибка активации проекта");
        return new ResponseEntity<>(new Date(System.currentTimeMillis()).toString(), HttpStatus.OK);
    }

    @PutMapping("/api/project/inactivate/{id}")
    public ResponseEntity<?> inactivateProject(@PathVariable Long id) {
        if(!projectService.inactivate(projectService.find(id)))
            throw new OperationExecutionException("Ошибка деактивации проекта");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/api/project/edit/{id}")
    public ResponseEntity<?> editProject(@PathVariable Long id, @RequestBody ProjectDTO projectDTO) {
        Project project = projectService.find(id);
        project.setName(projectDTO.getName());
        project.setModifiedDate(new Date(System.currentTimeMillis()));
        project = projectService.save(project);
        if (!Objects.equals(project.getDepartment().getId(), projectDTO.getDepartment().getId())
                && projectDTO.getDepartment().getId() != null) {
            project = projectService.assignProjectToDepartmentId(project, projectDTO.getDepartment().getId());
            if (!Objects.equals(project.getDepartment().getId(), projectDTO.getDepartment().getId())
                    && projectDTO.getDepartment().getId() != null)
                throw new ExistingDataException("Данный проект невозможно перенести в данный отдел");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/api/project/close/{id}")
    public ResponseEntity<?> closeProject(@PathVariable Long id) {
        Project project = projectService.find(id);
        if (project.isActive()) throw new ActiveStatusDataException("Данный проект активен и недоступен для закрытия!");
        if(!projectService.closeProject(projectService.find(id)))
            throw new ExistingDataException(" В данном проекте существуют незакрытые задачи и недоступен для закрытия!");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/project/assign/employee")
    public ResponseEntity<?> assignEmployeeToProject(@RequestBody EmployeePositionDTO employeePositionDTO) {
        if(projectService.assignToProject(
                employeeService.find(employeePositionDTO.getEmployee().getId()),
                projectService.find(employeePositionDTO.getProject().getId()),
                departmentService.findPosition(employeePositionDTO.getPosition().getId())).getId() == null)
            throw new OperationExecutionException("Ошибка назначения сотрудника на проект");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/project/cancel/employee")
    public ResponseEntity<?> cancelEmployeeFromProject(@RequestBody EmployeePositionDTO employeePositionDTO) {
        EmployeePosition employeePosition = projectService.findEmployeePositions(
                        employeeService.find(employeePositionDTO.getEmployee().getId()))
                .stream().filter(obj ->
                        obj.getEmployee().getId().equals(employeePositionDTO.getEmployee().getId())
                                && obj.getProject().getId().equals(employeePositionDTO.getProject().getId())).findFirst().get();
        if(projectService.changeEmployeeStateInProject(employeePosition, false).isActive())
            throw new OperationExecutionException("Ошибка снятия сотрудника с проекта");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
