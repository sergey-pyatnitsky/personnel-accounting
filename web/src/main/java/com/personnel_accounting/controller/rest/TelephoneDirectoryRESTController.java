package com.personnel_accounting.controller.rest;

import com.personnel_accounting.employee.EmployeeService;
import com.personnel_accounting.entity.dto.EmployeeDTO;
import com.personnel_accounting.entity.dto.ProfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TelephoneDirectoryRESTController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ConversionService conversionService;

    @PostMapping("/api/employee/telephone-directory/search/by_name/{name}")
    public ResponseEntity<?> getSearchResultByName(@PathVariable String name) {
        List<EmployeeDTO> employees = employeeService.findByNamePart(name).stream()
                    .map(employee -> {
                        EmployeeDTO tempEmployee = conversionService.convert(employee, EmployeeDTO.class);
                        tempEmployee.setProfile(conversionService.convert(
                                employeeService.findProfileByEmployee(employee), ProfileDTO.class));
                        return tempEmployee;
                    }).collect(Collectors.toList());
        return employees != null && !employees.isEmpty()
                ? new ResponseEntity<>(employees, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/api/employee/telephone-directory/search/by_phone/{phone}")
    public ResponseEntity<?> getSearchResultByPhone(@PathVariable String phone) {
        List<EmployeeDTO> employees = employeeService.findByPhonePart(phone).stream()
                .map(employee -> {
                    EmployeeDTO tempEmployee = conversionService.convert(employee, EmployeeDTO.class);
                    tempEmployee.setProfile(conversionService.convert(
                            employeeService.findProfileByEmployee(employee), ProfileDTO.class));
                    return tempEmployee;
                }).collect(Collectors.toList());
        return employees != null && !employees.isEmpty()
                ? new ResponseEntity<>(employees, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/api/employee/telephone-directory/search/by_email/{email}")
    public ResponseEntity<?> getSearchResultByEmail(@PathVariable String email) {
        List<EmployeeDTO> employees = employeeService.findByEmailPart(email).stream()
                .map(employee -> {
                    EmployeeDTO tempEmployee = conversionService.convert(employee, EmployeeDTO.class);
                    tempEmployee.setProfile(conversionService.convert(
                            employeeService.findProfileByEmployee(employee), ProfileDTO.class));
                    return tempEmployee;
                }).collect(Collectors.toList());
        return employees != null && !employees.isEmpty()
                ? new ResponseEntity<>(employees, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
