package com.personnel_accounting.entity.converter.domain;

import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.entity.dto.EmployeeDTO;
import org.springframework.core.convert.converter.Converter;

public class EmployeeConverter implements Converter<EmployeeDTO, Employee> {

    @Override
    public Employee convert(EmployeeDTO source) {
        Employee employee = new Employee();
        employee.setId(source.getId());
        employee.setName(source.getName());
        employee.setActive(source.isActive());
        return employee;
    }
}

