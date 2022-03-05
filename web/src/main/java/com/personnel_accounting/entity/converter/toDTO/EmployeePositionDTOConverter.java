package com.personnel_accounting.entity.converter.toDTO;

import com.personnel_accounting.domain.EmployeePosition;
import com.personnel_accounting.entity.dto.EmployeePositionDTO;
import org.springframework.core.convert.converter.Converter;

public class EmployeePositionDTOConverter implements Converter<EmployeePosition, EmployeePositionDTO> {

    @Override
    public EmployeePositionDTO convert(EmployeePosition source) {
        EmployeePositionDTO employeePositionDTO = new EmployeePositionDTO();
        employeePositionDTO.setId(source.getId());
        employeePositionDTO.setActive(source.isActive());
        return employeePositionDTO;
    }
}
