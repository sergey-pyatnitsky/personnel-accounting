package com.personnel_accounting.entity.converter.toDomain;

import com.personnel_accounting.domain.Position;
import com.personnel_accounting.entity.dto.PositionDTO;
import org.springframework.core.convert.converter.Converter;

public class PositionConverter implements Converter<PositionDTO, Position> {

    @Override
    public Position convert(PositionDTO source) {
        Position position = new Position();
        position.setId(source.getId());
        position.setName(source.getName());
        return position;
    }
}
