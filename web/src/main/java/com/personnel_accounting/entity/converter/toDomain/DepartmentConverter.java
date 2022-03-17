package com.personnel_accounting.entity.converter.toDomain;

import com.personnel_accounting.domain.Department;
import com.personnel_accounting.entity.dto.DepartmentDTO;
import org.springframework.core.convert.converter.Converter;

import java.sql.Date;

public class DepartmentConverter implements Converter<DepartmentDTO, Department> {

    @Override
    public Department convert(DepartmentDTO source) {
        Department department = new Department();
        department.setId(source.getId());
        department.setName(source.getName());
        department.setActive(source.isActive());
        if (source.getStart_date() != null)
            department.setStartDate(Date.valueOf(source.getStart_date()));
        if (source.getEnd_date() != null)
            department.setEndDate(Date.valueOf(source.getEnd_date()));
        return department;
    }
}
