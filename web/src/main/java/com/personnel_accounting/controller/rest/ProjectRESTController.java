package com.personnel_accounting.controller.rest;

import com.personnel_accounting.department.DepartmentService;
import com.personnel_accounting.domain.*;
import com.personnel_accounting.employee.EmployeeService;
import com.personnel_accounting.entity.dto.DepartmentDTO;
import com.personnel_accounting.entity.dto.EmployeePositionDTO;
import com.personnel_accounting.entity.dto.ProjectDTO;
import com.personnel_accounting.enums.Role;
import com.personnel_accounting.exception.ActiveStatusDataException;
import com.personnel_accounting.exception.ExistingDataException;
import com.personnel_accounting.exception.OperationExecutionException;
import com.personnel_accounting.pagination.entity.Page;
import com.personnel_accounting.pagination.entity.PagingRequest;
import com.personnel_accounting.project.ProjectService;
import com.personnel_accounting.user.UserService;
import com.personnel_accounting.utils.AuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/project")
public class ProjectRESTController {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private MessageSource messageSource;

    @PostMapping("/add")
    public ResponseEntity<?> addProject(@Valid @RequestBody ProjectDTO projectDTO, Authentication authentication) {
        User user = userService.find(AuthenticationUtil.getUsernameFromAuthentication(authentication));
        if (userService.getAuthorityByUsername(user.getUsername()).getRole() == Role.DEPARTMENT_HEAD)
            projectDTO.getDepartment().setId(projectService.findDepartmentByUser(user).getId());
        employeeService.findByUser(user);
        Project project = conversionService.convert(projectDTO, Project.class);
        project = projectService.addProject(project, projectDTO.getDepartment().getId());
        return new ResponseEntity<>(conversionService.convert(project, ProjectDTO.class), HttpStatus.OK);
    }

    @PostMapping("/get_all/open")
    public ResponseEntity<?> getAllOpenProjects(@RequestBody PagingRequest pagingRequest) {
        return new ResponseEntity<>(getPage(projectService.findAllOpen(pagingRequest)
                        .stream().map(project -> {
                            ProjectDTO projectDTO = conversionService.convert(project, ProjectDTO.class);
                            projectDTO.setDepartment(conversionService.convert(project.getDepartment(), DepartmentDTO.class));
                            return projectDTO;
                        }).collect(Collectors.toList()),
                pagingRequest.getDraw(), projectService.getAllOpenProjectCount().intValue()), HttpStatus.OK);
    }

    @PostMapping("/by_employee/open/{id}")
    public ResponseEntity<?> getAllOpenProjectsByEmployee(@RequestBody PagingRequest pagingRequest,
                                                          @PathVariable Long id, Authentication authentication) {
        Employee employee;
        if (id != 0) employee = employeeService.find(id);
        else employee = employeeService.findByUser(
                userService.findByUsername(AuthenticationUtil.getUsernameFromAuthentication(authentication)));
        return new ResponseEntity<>(getPage(projectService.findByEmployeePaginated(pagingRequest, employee)
                        .stream().map(employeePosition ->
                                conversionService.convert(employeePosition.getProject(), ProjectDTO.class))
                        .collect(Collectors.toList()),
                pagingRequest.getDraw(), projectService.getByEmployeeCount(employee).intValue()), HttpStatus.OK);
    }

    @PostMapping("/by_department/open/{id}")
    public ResponseEntity<?> getAllOpenProjectsByDepartment(@RequestBody PagingRequest pagingRequest,
                                                            @PathVariable Long id, Authentication authentication) {
        Department department;
        if (id != 0) department = departmentService.find(id);
        else
            department = employeeService.findByUser(
                    userService.findByUsername(AuthenticationUtil.getUsernameFromAuthentication(authentication))).getDepartment();
        return new ResponseEntity<>(getPage(departmentService.findOpenProjectsByDepartmentPaginated(pagingRequest, department)
                        .stream().map(project -> conversionService.convert(project, ProjectDTO.class)).collect(Collectors.toList()),
                pagingRequest.getDraw(), departmentService.getOpenProjectsByDepartmentCount(department).intValue()), HttpStatus.OK);
    }

    @PostMapping("/get_all/closed")
    public ResponseEntity<?> getAllClosedProjects(@RequestBody PagingRequest pagingRequest) {
        return new ResponseEntity<>(getPage(projectService.findAllClosed(pagingRequest)
                        .stream().map(project -> {
                            ProjectDTO projectDTO = conversionService.convert(project, ProjectDTO.class);
                            projectDTO.setDepartment(conversionService.convert(project.getDepartment(), DepartmentDTO.class));
                            return projectDTO;
                        }).collect(Collectors.toList()),
                pagingRequest.getDraw(), projectService.getAllClosedProjectCount().intValue()), HttpStatus.OK);
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<?> activateProject(@PathVariable Long id) {
        if (!projectService.activate(projectService.find(id)))
            throw new OperationExecutionException(
                    messageSource.getMessage("project.error.activate", null, LocaleContextHolder.getLocale()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/inactivate/{id}")
    public ResponseEntity<?> inactivateProject(@PathVariable Long id) {
        if (!projectService.inactivate(projectService.find(id)))
            throw new OperationExecutionException(
                    messageSource.getMessage("project.error.deactivate", null, LocaleContextHolder.getLocale()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editProject(@PathVariable Long id, @Valid @RequestBody ProjectDTO projectDTO) {
        Project project = projectService.find(id);
        project.setName(projectDTO.getName());
        project.setModifiedDate(new Date(System.currentTimeMillis()));
        project = projectService.save(project);
        if (!Objects.equals(project.getDepartment().getId(), projectDTO.getDepartment().getId())
                && projectDTO.getDepartment().getId() != null) {
            project = projectService.assignProjectToDepartmentId(project, projectDTO.getDepartment().getId());
            if (!Objects.equals(project.getDepartment().getId(), projectDTO.getDepartment().getId())
                    && projectDTO.getDepartment().getId() != null)
                throw new ExistingDataException(
                        messageSource.getMessage("project.error.transfer", null, null));
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/close/{id}")
    public ResponseEntity<?> closeProject(@PathVariable Long id) {
        Project project = projectService.find(id);
        if (project.isActive()) throw new ActiveStatusDataException(
                messageSource.getMessage("project.error.close.active", null, null));
        if (!projectService.closeProject(projectService.find(id)))
            throw new ExistingDataException(messageSource.getMessage("project.error.close.task", null, null));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/assign/employee")
    public ResponseEntity<?> assignEmployeeToProject(@Valid @RequestBody EmployeePositionDTO employeePositionDTO) {
        if (projectService.assignToProject(
                employeeService.find(employeePositionDTO.getEmployee().getId()),
                projectService.find(employeePositionDTO.getProject().getId()),
                departmentService.findPosition(employeePositionDTO.getPosition().getId())).getId() == null)
            throw new OperationExecutionException(messageSource.getMessage("project.error.assign.user", null, null));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/cancel/employee")
    public ResponseEntity<?> cancelEmployeeFromProject(@Valid @RequestBody EmployeePositionDTO employeePositionDTO) {
        EmployeePosition employeePosition = projectService.findEmployeePositions(
                        employeeService.find(employeePositionDTO.getEmployee().getId()))
                .stream().filter(obj ->
                        obj.getEmployee().getId().equals(employeePositionDTO.getEmployee().getId())
                                && obj.getProject().getId().equals(employeePositionDTO.getProject().getId())).findFirst().get();
        if (projectService.changeEmployeeStateInProject(employeePosition, false).isActive())
            throw new OperationExecutionException(messageSource.getMessage("project.error.remove.user", null, null));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Page<ProjectDTO> getPage(List<ProjectDTO> list, int draw, int count) {
        Page<ProjectDTO> page = new Page<>(list);
        page.setRecordsTotal(count);
        page.setRecordsFiltered(count);
        page.setDraw(draw);
        return page;
    }
}
