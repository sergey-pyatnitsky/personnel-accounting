package com.personnel_accounting.controller;

import com.personnel_accounting.employee.EmployeeService;
import com.personnel_accounting.entity.dto.EmployeeDTO;
import com.personnel_accounting.entity.dto.ProfileDTO;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class EmployeeRESTController {
    private final EmployeeService employeeService;
    private final ConversionService conversionService;

    public EmployeeRESTController(EmployeeService employeeService, ConversionService conversionService) {
        this.employeeService = employeeService;
        this.conversionService = conversionService;
    }

    @PostMapping("/api/telephone-directory/search")
    public ResponseEntity<?> getSearchResultViaAjax(@RequestBody EmployeeDTO employeeDTO, Errors errors) {
        List<EmployeeDTO> employees = null;
        if (employeeDTO.getName() != null) {
            employees = employeeService.findByNamePart(employeeDTO.getName()).stream()
                    .map(employee -> {
                        EmployeeDTO tempEmployee = conversionService.convert(employee, EmployeeDTO.class);
                        tempEmployee.setProfile(conversionService.convert(
                                employeeService.findProfileByEmployee(employee), ProfileDTO.class));
                        return tempEmployee;
                    }).collect(Collectors.toList());
        } else if (employeeDTO.getProfile().getPhone() != null) {
            employees = employeeService.findByPhonePart(employeeDTO.getProfile().getPhone()).stream()
                    .map(employee -> {
                        EmployeeDTO tempEmployee = conversionService.convert(employee, EmployeeDTO.class);
                        tempEmployee.setProfile(conversionService.convert(
                                employeeService.findProfileByEmployee(employee), ProfileDTO.class));
                        return tempEmployee;
                    }).collect(Collectors.toList());
        } else if (employeeDTO.getProfile().getEmail() != null) {
            employees = employeeService.findByEmailPart(employeeDTO.getProfile().getEmail()).stream()
                    .map(employee -> {
                        EmployeeDTO tempEmployee = conversionService.convert(employee, EmployeeDTO.class);
                        tempEmployee.setProfile(conversionService.convert(
                                employeeService.findProfileByEmployee(employee), ProfileDTO.class));
                        return tempEmployee;
                    }).collect(Collectors.toList());
        }
        return employees != null && !employees.isEmpty() ? new ResponseEntity<>(employees, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
