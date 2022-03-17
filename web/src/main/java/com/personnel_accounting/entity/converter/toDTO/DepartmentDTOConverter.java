package com.personnel_accounting.entity.converter.toDTO;

import com.personnel_accounting.domain.Department;
import com.personnel_accounting.entity.dto.DepartmentDTO;
import org.springframework.core.convert.converter.Converter;

public class DepartmentDTOConverter implements Converter<Department, DepartmentDTO> {

    @Override
    public DepartmentDTO convert(Department source) {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(source.getId());
        departmentDTO.setName(source.getName());
        departmentDTO.setActive(source.isActive());
        if(source.getStartDate() != null)
            departmentDTO.setStart_date(source.getStartDate().toString());
        if(source.getEndDate() != null)
            departmentDTO.setEnd_date(source.getEndDate().toString());
        return departmentDTO;
    }
}
