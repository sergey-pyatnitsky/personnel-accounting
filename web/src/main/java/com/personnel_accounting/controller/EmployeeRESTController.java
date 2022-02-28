package com.personnel_accounting.controller;

import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.employee.EmployeeService;
import com.personnel_accounting.entity.AjaxResponseBody;
import com.personnel_accounting.entity.SearchCriteria;
import com.personnel_accounting.entity.dto.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class EmployeeRESTController {
    private EmployeeService employeeService;
    private ConversionService conversionService;

    public EmployeeRESTController(EmployeeService employeeService, ConversionService conversionService) {
        this.employeeService = employeeService;
        this.conversionService = conversionService;
    }

    @PostMapping("/api/telephone-directory/search")
    public ResponseEntity<?> getSearchResultViaAjax(@RequestBody String search, Errors errors) {
        AjaxResponseBody result = new AjaxResponseBody();
        //If error, just return a 400 bad request, along with the error message
        if (errors.hasErrors()) {
            result.setMsg(errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }
        List<EmployeeDTO> employees = employeeService.findByName(search).stream()
                .map(employee -> conversionService.convert(employee, EmployeeDTO.class)).collect(Collectors.toList());
        if (employees.isEmpty()) {
            result.setMsg("no user found!");
        } else {
            result.setMsg("success");
        }
        result.setResult(employees);

        return ResponseEntity.ok(result);
    }
}
