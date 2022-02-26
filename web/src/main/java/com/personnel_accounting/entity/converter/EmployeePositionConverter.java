package com.personnel_accounting.entity.converter;

import com.personnel_accounting.domain.*;
import com.personnel_accounting.department.DepartmentService;
import com.personnel_accounting.entity.dto.EmployeePositionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

public class EmployeePositionConverter
        implements Converter<EmployeePositionDTO, EmployeePosition> {
    private ConversionService conversionService;
    private DepartmentService departmentService;

    @Autowired
    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Autowired
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public EmployeePosition convert(EmployeePositionDTO source) {
        EmployeePosition employeePosition = new EmployeePosition();
        employeePosition.setId(source.getId());
        employeePosition.setEmployee(
                conversionService.convert(source.getEmployee(), Employee.class));
        employeePosition.setPosition(
                conversionService.convert(source.getPosition(), Position.class));
        employeePosition.setProject(
                conversionService.convert(source.getProject(), Project.class));
        employeePosition.setDepartment(
                conversionService.convert(source.getDepartment(), Department.class));
        employeePosition.setActive(source.isActive());
        return departmentService.mergeEmployeePosition(employeePosition);
    }
}
