package com.personnel_accounting.entity.converter.toDTO;

import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.entity.dto.EmployeeDTO;
import org.springframework.core.convert.converter.Converter;

public class EmployeeDTOConverter implements Converter<Employee, EmployeeDTO> {

    @Override
    public EmployeeDTO convert(Employee source) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(source.getId());
        employeeDTO.setName(source.getName());
        employeeDTO.setActive(source.isActive());
        return employeeDTO;
    }
}
