package com.web.entity.converter;

import com.core.domain.Employee;
import com.web.entity.dto.EmployeeDTO;
import org.springframework.core.convert.converter.Converter;

public class EmployeeConverter implements Converter<EmployeeDTO, Employee> {

    @Override
    public Employee convert(EmployeeDTO source) {
        Employee employee = new Employee();
        employee.setUser(source.getUser());
        employee.setName(source.getName());
        employee.setDepartment(source.getDepartment());
        employee.setProfile(source.getProfile());
        employee.setActive(source.isActive());
        return employee;
    }
}

