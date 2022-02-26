package com.personnel_accounting.entity.converter;

import com.personnel_accounting.domain.Position;
import com.personnel_accounting.department.DepartmentService;
import com.personnel_accounting.entity.dto.PositionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

public class PositionConverter implements Converter<PositionDTO, Position> {
    private DepartmentService departmentService;

    @Autowired
    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Override
    public Position convert(PositionDTO source) {
        Position position = new Position();
        position.setId(source.getId());
        position.setName(source.getName());
        return departmentService.mergePosition(position);
    }
}
