package com.personnel_accounting.entity.converter.toDTO;

import com.personnel_accounting.domain.ReportCard;
import com.personnel_accounting.entity.dto.EmployeeDTO;
import com.personnel_accounting.entity.dto.ReportCardDTO;
import com.personnel_accounting.entity.dto.TaskDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

public class ReportCardDTOConverter implements Converter<ReportCard, ReportCardDTO> {
    private ConversionService conversionService;

    @Autowired
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public ReportCardDTO convert(ReportCard source) {
        ReportCardDTO reportCardDTO = new ReportCardDTO();
        reportCardDTO.setId(source.getId());
        reportCardDTO.setDate(source.getDate());
        reportCardDTO.setTask(
                conversionService.convert(source.getTask(), TaskDTO.class));
        reportCardDTO.setEmployee(
                conversionService.convert(source.getEmployee(), EmployeeDTO.class));
        reportCardDTO.setWorkingTime(source.getWorkingTime());
        return reportCardDTO;
    }
}
