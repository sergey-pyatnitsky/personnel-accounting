package com.personnel_accounting.entity.converter.toDomain;

import com.personnel_accounting.domain.Department;
import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.domain.Profile;
import com.personnel_accounting.domain.User;
import com.personnel_accounting.entity.dto.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

public class EmployeeConverter implements Converter<EmployeeDTO, Employee> {
    private ConversionService conversionService;

    @Autowired
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public Employee convert(EmployeeDTO source) {
        Employee employee = new Employee();
        employee.setId(source.getId());
        employee.setUser(conversionService.convert(source.getUser(), User.class));
        employee.setName(source.getName());
        employee.setDepartment(conversionService.convert(source.getDepartment(), Department.class));
        employee.setProfile(conversionService.convert(source.getProfile(), Profile.class));
        employee.setActive(source.isActive());
        return employee;
    }
}

