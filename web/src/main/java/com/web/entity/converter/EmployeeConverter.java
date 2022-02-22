package com.web.entity.converter;

import com.core.domain.Employee;
import com.service.employee.EmployeeService;
import com.web.entity.dto.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

public class EmployeeConverter implements Converter<EmployeeDTO, Employee> {
    private EmployeeService employeeService;

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public Employee convert(EmployeeDTO source) {
        Employee employee = new Employee();
        employee.setId(source.getId());
        employee.setUser(source.getUser());
        employee.setName(source.getName());
        employee.setDepartment(source.getDepartment());
        employee.setProfile(source.getProfile());
        employee.setActive(source.isActive());
        return employeeService.update(employee);
    }
}

