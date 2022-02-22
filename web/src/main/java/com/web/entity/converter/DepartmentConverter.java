package com.web.entity.converter;

import com.core.domain.Department;
import com.service.department.DepartmentService;
import com.web.entity.dto.DepartmentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

public class DepartmentConverter implements Converter<DepartmentDTO, Department> {
    private DepartmentService departmentService;

    @Autowired
    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Override
    public Department convert(DepartmentDTO source) {
        Department department = new Department();
        department.setId(source.getId());
        department.setName(source.getName());
        department.setActive(source.isActive());
        return departmentService.update(department);
    }
}
