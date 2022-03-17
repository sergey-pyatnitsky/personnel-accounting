package com.personnel_accounting.entity.converter.toDomain;

import com.personnel_accounting.domain.Department;
import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.domain.EmployeePosition;
import com.personnel_accounting.domain.Position;
import com.personnel_accounting.domain.Project;
import com.personnel_accounting.entity.dto.EmployeePositionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

import java.sql.Date;

public class EmployeePositionConverter
        implements Converter<EmployeePositionDTO, EmployeePosition> {

    @Override
    public EmployeePosition convert(EmployeePositionDTO source) {
        EmployeePosition employeePosition = new EmployeePosition();
        employeePosition.setId(source.getId());
        employeePosition.setActive(source.isActive());
        if (source.getStart_date() != null)
            employeePosition.setStartDate(Date.valueOf(source.getStart_date()));
        if (source.getEnd_date() != null)
            employeePosition.setEndDate(Date.valueOf(source.getEnd_date()));
        return employeePosition;
    }
}
