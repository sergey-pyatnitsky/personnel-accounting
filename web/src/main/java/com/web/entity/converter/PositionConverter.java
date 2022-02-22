package com.web.entity.converter;

import com.core.domain.Position;
import com.service.department.DepartmentService;
import com.web.entity.dto.PositionDTO;
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
