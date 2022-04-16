package com.personnel_accounting.entity.converter.domain;

import com.personnel_accounting.domain.ReportCard;
import com.personnel_accounting.entity.dto.ReportCardDTO;
import org.springframework.core.convert.converter.Converter;

public class ReportCardConverter implements Converter<ReportCardDTO, ReportCard> {

    @Override
    public ReportCard convert(ReportCardDTO source) {
        ReportCard reportCard = new ReportCard();
        reportCard.setId(source.getId());
        reportCard.setDate(source.getDate());
        reportCard.setWorkingTime(source.getWorkingTime());
        return reportCard;
    }
}
