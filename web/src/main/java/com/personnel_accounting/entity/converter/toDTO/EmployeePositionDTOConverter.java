package com.personnel_accounting.entity.converter.toDTO;

import com.personnel_accounting.domain.EmployeePosition;
import com.personnel_accounting.entity.dto.DepartmentDTO;
import com.personnel_accounting.entity.dto.EmployeeDTO;
import com.personnel_accounting.entity.dto.EmployeePositionDTO;
import com.personnel_accounting.entity.dto.PositionDTO;
import com.personnel_accounting.entity.dto.ProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

public class EmployeePositionDTOConverter implements Converter<EmployeePosition, EmployeePositionDTO> {
    private ConversionService conversionService;

    @Autowired
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public EmployeePositionDTO convert(EmployeePosition source) {
        EmployeePositionDTO employeePositionDTO = new EmployeePositionDTO();
        employeePositionDTO.setId(source.getId());
        employeePositionDTO.setEmployee(
                conversionService.convert(source.getEmployee(), EmployeeDTO.class));
        employeePositionDTO.setPosition(
                conversionService.convert(source.getPosition(), PositionDTO.class));
        employeePositionDTO.setProject(
                conversionService.convert(source.getProject(), ProjectDTO.class));
        employeePositionDTO.setDepartment(
                conversionService.convert(source.getDepartment(), DepartmentDTO.class));
        employeePositionDTO.setActive(source.isActive());
        return employeePositionDTO;
    }
}
