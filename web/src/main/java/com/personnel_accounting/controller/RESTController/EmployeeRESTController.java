package com.personnel_accounting.controller.RESTController;

import com.personnel_accounting.department.DepartmentService;
import com.personnel_accounting.domain.Department;
import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.domain.User;
import com.personnel_accounting.employee.EmployeeService;
import com.personnel_accounting.entity.dto.DepartmentDTO;
import com.personnel_accounting.entity.dto.EmployeeDTO;
import com.personnel_accounting.entity.dto.UserDTO;
import com.personnel_accounting.enums.Role;
import com.personnel_accounting.project.ProjectService;
import com.personnel_accounting.user.UserService;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class EmployeeRESTController {
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final ProjectService projectService;
    private final UserService userService;
    private final ConversionService conversionService;

    public EmployeeRESTController(EmployeeService employeeService, DepartmentService departmentService,
                                  ProjectService projectService, UserService userService,
                                  ConversionService conversionService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.projectService = projectService;
        this.userService = userService;
        this.conversionService = conversionService;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registerEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = conversionService.convert(employeeDTO, Employee.class);
        employee.setUser(conversionService.convert(employeeDTO.getUser(), User.class));
        employee.getUser().setPassword("{bcrypt}" + (new BCryptPasswordEncoder()).encode(employee.getUser().getPassword()));
        employee.setCreateDate(new Date(System.currentTimeMillis()));

        return userService.registerUser(employee.getUser(), employeeDTO.getName(), Role.EMPLOYEE)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping("/api/employee/get_all")
    public ResponseEntity<?> getEmployees() {
        return new ResponseEntity<>(
                employeeService.findAll()
                        .stream().map(employee -> {
                            EmployeeDTO tempEmployee = conversionService.convert(employee, EmployeeDTO.class);
                            tempEmployee.setUser(conversionService.convert(employee.getUser(), UserDTO.class));
                            tempEmployee.getUser().setRole(userService.findRoleByUsername(tempEmployee.getUser().getUsername()));
                            tempEmployee.setDepartment(conversionService.convert(employee.getDepartment(), DepartmentDTO.class));
                            return tempEmployee;
                        }).collect(Collectors.toList())
                        .stream().filter(employeeDTO ->
                                employeeDTO.getUser().getRole() != Role.SUPER_ADMIN
                                        && employeeDTO.getUser().getRole() != Role.ADMIN).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @GetMapping("/api/employee/get_all/admins")
    public ResponseEntity<?> getAllAdmins() {
        return new ResponseEntity<>(
                employeeService.findAll()
                        .stream().map(employee -> {
                            EmployeeDTO tempEmployee = conversionService.convert(employee, EmployeeDTO.class);
                            tempEmployee.setUser(conversionService.convert(employee.getUser(), UserDTO.class));
                            tempEmployee.getUser().setRole(userService.findRoleByUsername(tempEmployee.getUser().getUsername()));
                            return tempEmployee;
                        }).collect(Collectors.toList())
                        .stream().filter(employeeDTO -> employeeDTO.getUser().getRole() == Role.ADMIN).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @GetMapping("/api/employee/get_all/free")
    public ResponseEntity<?> getAllFreeEmployees() {
        return new ResponseEntity<>(
                employeeService.findAll()
                        .stream().map(employee -> {
                            EmployeeDTO tempEmployee = conversionService.convert(employee, EmployeeDTO.class);
                            tempEmployee.setUser(conversionService.convert(employee.getUser(), UserDTO.class));
                            tempEmployee.getUser().setRole(userService.findRoleByUsername(tempEmployee.getUser().getUsername()));
                            tempEmployee.setDepartment(conversionService.convert(employee.getDepartment(), DepartmentDTO.class));
                            return tempEmployee;
                        }).collect(Collectors.toList())
                        .stream().filter(employeeDTO -> employeeDTO.getDepartment() == null).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @GetMapping("/api/employee/get_all/department/{id}")
    public ResponseEntity<?> getAllEmployeeByDepartment(@PathVariable Long id, Authentication authentication) {
        Department department;
        if(id != 0) department = departmentService.find(id);
        else department = employeeService.findByUser(userService.findByUsername(authentication.getName())).getDepartment();
        List<Employee> employees = employeeService.findByDepartment(department);
        return employees.size() != 0
                ? new ResponseEntity<>(employees, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping("/api/employee/get_with_project/department/{id}")
    public ResponseEntity<?> getEmployeesWithProjectByDepartment(@PathVariable Long id, Authentication authentication) {
        Department department;
        if(id != 0) department = departmentService.find(id);
        else department = employeeService.findByUser(userService.findByUsername(authentication.getName())).getDepartment();

        List<Employee> employees = employeeService.getEmployeesWithProjectByDepartment(department);
        return employees.size() != 0
                ? new ResponseEntity<>(employees, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping("/api/employee/get_all/assigned")
    public ResponseEntity<?> getAllAssignedEmployees() {
        return new ResponseEntity<>(
                employeeService.findAll()
                        .stream().map(employee -> {
                            EmployeeDTO tempEmployee = conversionService.convert(employee, EmployeeDTO.class);
                            tempEmployee.setUser(conversionService.convert(employee.getUser(), UserDTO.class));
                            tempEmployee.getUser().setRole(userService.findRoleByUsername(tempEmployee.getUser().getUsername()));
                            tempEmployee.setDepartment(conversionService.convert(employee.getDepartment(), DepartmentDTO.class));
                            return tempEmployee;
                        }).collect(Collectors.toList())
                        .stream().filter(employeeDTO -> employeeDTO.getDepartment() != null).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @PostMapping("/api/employee/assign/department")
    public ResponseEntity<?> assignEmployeeToDepartment(@RequestBody EmployeeDTO employeeDTO){
        Employee employee = conversionService.convert(employeeService.find(employeeDTO.getId()), Employee.class);
        Department department = departmentService.find(employeeDTO.getDepartment().getId());
        employee.setDepartment(department);
        employee = departmentService.assignToDepartment(employee, department);
        return employee.getDepartment().getId().equals(employeeDTO.getDepartment().getId())
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @DeleteMapping("/api/employee/remove/{id}")
    public ResponseEntity<?> removeUser(@PathVariable Long id) {
        return employeeService.removeById(id)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping("/api/employee/edit")
    public ResponseEntity<?> editUser(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = employeeService.find(employeeDTO.getId());
        employee.setUser(userService.changeUserRole(employee.getUser(), employeeDTO.getUser().getRole()));
        employee.setName(employeeDTO.getName());
        employee.setModifiedDate(new Date(System.currentTimeMillis()));
        employeeService.save(employee);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/api/employee/activate/{username}")
    public ResponseEntity<?> activateUser(@PathVariable String username) {
        Employee employee = employeeService.findByUser(userService.findByUsername(username));
        if (!userService.activate(employee.getUser())) return new ResponseEntity<>(HttpStatus.CONFLICT);
        return employeeService.activate(employee)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping("/api/employee/inactivate/{username}")
    public ResponseEntity<?> inactivateUser(@PathVariable String username) {
        Employee employee = employeeService.findByUser(userService.findByUsername(username));
        if (!userService.inactivate(employee.getUser())) return new ResponseEntity<>(HttpStatus.CONFLICT);
        return employeeService.inactivate(employee)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping("/api/employee/get_by_role/{role}")
    public ResponseEntity<?> getEmployeesByRole(@PathVariable Role role) {
        List<EmployeeDTO> employeeDTOList = userService.findByRole(role).stream()
                .map(user -> conversionService.convert(employeeService.findByUser(user), EmployeeDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(employeeDTOList, HttpStatus.OK);
    }
}