package com.personnel_accounting.entity.converter.dto;

import com.personnel_accounting.domain.ReportCard;
import com.personnel_accounting.entity.dto.ReportCardDTO;
import org.springframework.core.convert.converter.Converter;

public class ReportCardDTOConverter implements Converter<ReportCard, ReportCardDTO> {

    @Override
    public ReportCardDTO convert(ReportCard source) {
        ReportCardDTO reportCardDTO = new ReportCardDTO();
        reportCardDTO.setId(source.getId());
        reportCardDTO.setDate(source.getDate());
        reportCardDTO.setWorkingTime(source.getWorkingTime());
        return reportCardDTO;
    }
}
