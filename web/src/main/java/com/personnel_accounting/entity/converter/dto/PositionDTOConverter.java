package com.personnel_accounting.entity.converter.dto;

import com.personnel_accounting.domain.Position;
import com.personnel_accounting.entity.dto.PositionDTO;
import org.springframework.core.convert.converter.Converter;

public class PositionDTOConverter implements Converter<Position, PositionDTO> {

    @Override
    public PositionDTO convert(Position source) {
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setId(source.getId());
        positionDTO.setName(source.getName());
        return positionDTO;
    }
}
