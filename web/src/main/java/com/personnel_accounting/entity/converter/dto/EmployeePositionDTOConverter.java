package com.personnel_accounting.entity.converter.dto;

import com.personnel_accounting.domain.EmployeePosition;
import com.personnel_accounting.entity.dto.EmployeePositionDTO;
import org.springframework.core.convert.converter.Converter;

public class EmployeePositionDTOConverter implements Converter<EmployeePosition, EmployeePositionDTO> {

    @Override
    public EmployeePositionDTO convert(EmployeePosition source) {
        EmployeePositionDTO employeePositionDTO = new EmployeePositionDTO();
        employeePositionDTO.setId(source.getId());
        employeePositionDTO.setActive(source.isActive());
        if(source.getStartDate() != null)
            employeePositionDTO.setStart_date(source.getStartDate().toString());
        if(source.getEndDate() != null)
            employeePositionDTO.setEnd_date(source.getEndDate().toString());
        return employeePositionDTO;
    }
}
