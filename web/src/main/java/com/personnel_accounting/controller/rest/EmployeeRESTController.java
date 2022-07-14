package com.personnel_accounting.controller.rest;

import com.personnel_accounting.department.DepartmentService;
import com.personnel_accounting.domain.Department;
import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.domain.Project;
import com.personnel_accounting.domain.User;
import com.personnel_accounting.employee.EmployeeService;
import com.personnel_accounting.entity.dto.DepartmentDTO;
import com.personnel_accounting.entity.dto.EmployeeDTO;
import com.personnel_accounting.entity.dto.UserDTO;
import com.personnel_accounting.enums.Role;
import com.personnel_accounting.exception.ExistingDataException;
import com.personnel_accounting.exception.OperationExecutionException;
import com.personnel_accounting.pagination.entity.Page;
import com.personnel_accounting.pagination.entity.PagingRequest;
import com.personnel_accounting.project.ProjectService;
import com.personnel_accounting.user.UserService;
import com.personnel_accounting.utils.AuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class EmployeeRESTController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private MessageSource messageSource;

    @PostMapping("/registration")
    public ResponseEntity<?> registerEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        Employee employee = conversionService.convert(employeeDTO, Employee.class);
        employee.setUser(conversionService.convert(employeeDTO.getUser(), User.class));
        employee.setCreateDate(new Date(System.currentTimeMillis()));
        if (!userService.registerUser(employee.getUser(),
                "{bcrypt}" + (new BCryptPasswordEncoder()).encode(employee.getUser().getPassword()),
                employeeDTO.getName(), Role.EMPLOYEE, employeeDTO.getProfile().getEmail()))
            throw new ExistingDataException(messageSource.getMessage("user.error.existing", null, null));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/employee/get_all")
    public ResponseEntity<?> getEmployees(@RequestBody PagingRequest pagingRequest) {
        return new ResponseEntity<>(getPage(employeeService.findAll(pagingRequest)
                        .stream().map(employee -> {
                            EmployeeDTO tempEmployee = conversionService.convert(employee, EmployeeDTO.class);
                            tempEmployee.setUser(conversionService.convert(employee.getUser(), UserDTO.class));
                            tempEmployee.setDepartment(conversionService.convert(employee.getDepartment(), DepartmentDTO.class));
                            return tempEmployee;
                        }).collect(Collectors.toList()),
                pagingRequest.getDraw(), employeeService.getEmployeeCount().intValue()), HttpStatus.OK);
    }

    @PostMapping("/api/employee/get_all/active")
    public ResponseEntity<?> getAllActiveEmployees(@RequestBody PagingRequest pagingRequest) {
        return new ResponseEntity<>(getPage(employeeService.findAllActiveEmployee(pagingRequest)
                        .stream().map(employee -> {
                            EmployeeDTO tempEmployee = conversionService.convert(employee, EmployeeDTO.class);
                            tempEmployee.setUser(conversionService.convert(employee.getUser(), UserDTO.class));
                            tempEmployee.setDepartment(conversionService.convert(employee.getDepartment(), DepartmentDTO.class));
                            return tempEmployee;
                        }).collect(Collectors.toList()),
                pagingRequest.getDraw(), employeeService.getActiveEmployeeCount().intValue()), HttpStatus.OK);
    }

    @PostMapping("/api/employee/get_all/dismissed")
    public ResponseEntity<?> getDismissedEmployees(@RequestBody PagingRequest pagingRequest) {
        return new ResponseEntity<>(getPage(employeeService.findAllDismissed(pagingRequest)
                        .stream().map(employee -> {
                            EmployeeDTO tempEmployee = conversionService.convert(employee, EmployeeDTO.class);
                            tempEmployee.setUser(conversionService.convert(employee.getUser(), UserDTO.class));
                            tempEmployee.setDepartment(conversionService.convert(employee.getDepartment(), DepartmentDTO.class));
                            return tempEmployee;
                        }).collect(Collectors.toList()),
                pagingRequest.getDraw(), employeeService.getDismissedEmployeeCount().intValue()), HttpStatus.OK);
    }

    @PostMapping("/api/employee/get_all/admins")
    public ResponseEntity<?> getAllActiveAdmins(@RequestBody PagingRequest pagingRequest) {
        return new ResponseEntity<>(getPage(employeeService.findAllActiveAdmins(pagingRequest)
                        .stream().map(employee -> {
                            EmployeeDTO tempEmployee = conversionService.convert(employee, EmployeeDTO.class);
                            tempEmployee.setUser(conversionService.convert(employee.getUser(), UserDTO.class));
                            tempEmployee.getUser().getAuthority().setRole(
                                    userService.findRoleByUsername(tempEmployee.getUser().getUsername()));
                            return tempEmployee;
                        }).collect(Collectors.toList()),
                pagingRequest.getDraw(), employeeService.getActiveAdminCount().intValue()), HttpStatus.OK);
    }

    @PostMapping("/api/employee/get_all/free")
    public ResponseEntity<?> getAllFreeEmployees(@RequestBody PagingRequest pagingRequest) {
        return new ResponseEntity<>(getPage(employeeService.findAllFreeAndActiveEmployees(pagingRequest)
                        .stream().map(employee -> {
                            EmployeeDTO tempEmployee = conversionService.convert(employee, EmployeeDTO.class);
                            tempEmployee.setUser(conversionService.convert(employee.getUser(), UserDTO.class));
                            tempEmployee.getUser().getAuthority().setRole(
                                    userService.findRoleByUsername(tempEmployee.getUser().getUsername()));
                            tempEmployee.setDepartment(conversionService.convert(employee.getDepartment(), DepartmentDTO.class));
                            return tempEmployee;
                        }).collect(Collectors.toList()),
                pagingRequest.getDraw(), employeeService.getFreeAndActiveEmployeesCount().intValue()), HttpStatus.OK);
    }

    @PostMapping("/api/employee/get_all/department/{id}")
    public ResponseEntity<?> getAllEmployeeByDepartment(@RequestBody PagingRequest pagingRequest,
                                                        @PathVariable Long id, Authentication authentication) {
        Department department;
        if (id != 0) department = departmentService.find(id);
        else
            department = employeeService.findByUser(userService.findByUsername(
                    AuthenticationUtil.getUsernameFromAuthentication(authentication))).getDepartment();
        return new ResponseEntity<>(getPage(employeeService.findByDepartmentPaginated(department, pagingRequest).stream().map(obj -> {
                    EmployeeDTO employeeDTO = conversionService.convert(obj, EmployeeDTO.class);
                    employeeDTO.setDepartment(conversionService.convert(obj.getDepartment(), DepartmentDTO.class));
                    employeeDTO.setUser(conversionService.convert(obj.getUser(), UserDTO.class));
                    employeeDTO.getUser().getAuthority().setRole(userService.findRoleByUsername(employeeDTO.getUser().getUsername()));
                    return employeeDTO;
                }).collect(Collectors.toList()),
                pagingRequest.getDraw(), employeeService.getEmployeeByDepartmentCount(department).intValue()), HttpStatus.OK);
    }

    @PostMapping("/api/employee/get_all/by_project/{id}")
    public ResponseEntity<?> getAllEmployeeByProject(@RequestBody PagingRequest pagingRequest, @PathVariable Long id) {
        Project project = projectService.find(id);
        return new ResponseEntity<>(getPage(projectService.findByProject(project)
                        .stream().filter(Employee::isActive).collect(Collectors.toList())
                        .stream().map(employee -> {
                            EmployeeDTO employeeDTO = conversionService.convert(employee, EmployeeDTO.class);
                            employeeDTO.setUser(conversionService.convert(employee.getUser(), UserDTO.class));
                            return employeeDTO;
                        }).collect(Collectors.toList()),
                pagingRequest.getDraw(), employeeService.getEmployeeByProjectCount(project).intValue()), HttpStatus.OK);
    }

    @PostMapping("/api/employee/get_with_project/department/{id}")
    public ResponseEntity<?> getEmployeesWithProjectByDepartment(@RequestBody PagingRequest pagingRequest,
                                                                 @PathVariable Long id, Authentication authentication) {
        Department department;
        if (id != 0) department = departmentService.find(id);
        else
            department = employeeService.findByUser(userService.findByUsername(
                    AuthenticationUtil.getUsernameFromAuthentication(authentication))).getDepartment();
        return new ResponseEntity<>(getPage(employeeService.getEmployeesWithOpenProjectByDepartment(department, pagingRequest)
                        .stream().map(obj -> {
                            EmployeeDTO employeeDTO = conversionService.convert(obj, EmployeeDTO.class);
                            employeeDTO.setDepartment(conversionService.convert(obj.getDepartment(), DepartmentDTO.class));
                            employeeDTO.setUser(conversionService.convert(obj.getUser(), UserDTO.class));
                            employeeDTO.getUser().getAuthority().setRole(userService.findRoleByUsername(employeeDTO.getUser().getUsername()));
                            return employeeDTO;
                        }).collect(Collectors.toList()),
                pagingRequest.getDraw(), employeeService.getEmployeesWithOpenProjectByDepartmentCount(department)), HttpStatus.OK);
    }

    @PostMapping("/api/employee/get_all/assigned")
    public ResponseEntity<?> getAllAssignedEmployees(@RequestBody PagingRequest pagingRequest) {
        return new ResponseEntity<>(getPage(employeeService.findAllAssignedAndActiveEmployees(pagingRequest)
                        .stream().map(employee -> {
                            EmployeeDTO tempEmployee = conversionService.convert(employee, EmployeeDTO.class);
                            tempEmployee.setUser(conversionService.convert(employee.getUser(), UserDTO.class));
                            tempEmployee.getUser().getAuthority().setRole(
                                    userService.findRoleByUsername(tempEmployee.getUser().getUsername()));
                            tempEmployee.setDepartment(conversionService.convert(employee.getDepartment(), DepartmentDTO.class));
                            return tempEmployee;
                        }).collect(Collectors.toList()),
                pagingRequest.getDraw(), employeeService.getAssignedAndActiveEmployeesCount().intValue()), HttpStatus.OK);
    }

    @PostMapping("/api/employee/assign/department")
    public ResponseEntity<?> assignEmployeeToDepartment(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = conversionService.convert(employeeService.find(employeeDTO.getId()), Employee.class);
        Department department = departmentService.find(employeeDTO.getDepartment().getId());
        employee.setDepartment(department);
        employee = departmentService.assignToDepartment(employee, department);
        if (!employee.getDepartment().getId().equals(employeeDTO.getDepartment().getId()))
            throw new OperationExecutionException(messageSource.getMessage("user.error.assign", null, null));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/api/employee/remove/{id}")
    public ResponseEntity<?> removeUser(@PathVariable Long id) {
        if (!employeeService.removeWithInactivation(employeeService.find(id)))
            throw new OperationExecutionException(messageSource.getMessage("user.error.remove", null, null));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/api/employee/edit")
    public ResponseEntity<?> editEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        Employee employee = employeeService.find(employeeDTO.getId());
        employee = employeeService.editEmployeeName(employee, employeeDTO.getName());
        userService.changeUserRole(employee.getUser(), employeeDTO.getUser().getAuthority().getRole());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/api/employee/activate/{username}")
    public ResponseEntity<?> activateUser(@PathVariable String username) {
        Employee employee = employeeService.findByUser(userService.findByUsername(username));
        if (!userService.activate(employee.getUser()) || !employeeService.activate(employee))
            throw new OperationExecutionException(messageSource.getMessage("user.error.activate", null, null));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/api/employee/inactivate/{username}")
    public ResponseEntity<?> inactivateUser(@PathVariable String username) {
        Employee employee = employeeService.findByUser(userService.findByUsername(username));
        if (!userService.inactivate(employee.getUser()) || !employeeService.inactivate(employee))
            throw new OperationExecutionException(messageSource.getMessage("user.error.deactivate", null, null));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/employee/get_by_role/{role}")
    public ResponseEntity<?> getEmployeesByRole(@PathVariable Role role) {
        List<EmployeeDTO> employeeDTOList = userService.findByRole(role).stream()
                .map(user -> conversionService.convert(employeeService.findByUser(user), EmployeeDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(employeeDTOList, HttpStatus.OK);
    }

    private Page<?> getPage(List<?> list, int draw, int count) {
        Page<?> page = new Page<>(list);
        page.setRecordsTotal(count);
        page.setRecordsFiltered(count);
        page.setDraw(draw);
        return page;
    }
}
