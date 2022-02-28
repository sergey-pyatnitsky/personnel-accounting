package com.personnel_accounting.entity.converter.toDTO;

import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.entity.dto.DepartmentDTO;
import com.personnel_accounting.entity.dto.EmployeeDTO;
import com.personnel_accounting.entity.dto.ProfileDTO;
import com.personnel_accounting.entity.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

public class EmployeeDTOConverter implements Converter<Employee, EmployeeDTO> {
    private ConversionService conversionService;

    @Autowired
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public EmployeeDTO convert(Employee source) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(source.getId());
        employeeDTO.setUser(conversionService.convert(source.getUser(), UserDTO.class));
        employeeDTO.setName(source.getName());
        employeeDTO.setDepartment(conversionService.convert(source.getDepartment(), DepartmentDTO.class));
        employeeDTO.setProfile(conversionService.convert(source.getProfile(), ProfileDTO.class));
        employeeDTO.setActive(source.isActive());
        return employeeDTO;
    }
}
