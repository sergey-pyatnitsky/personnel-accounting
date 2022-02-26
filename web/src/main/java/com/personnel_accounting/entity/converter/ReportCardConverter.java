package com.personnel_accounting.entity.converter;

import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.domain.ReportCard;
import com.personnel_accounting.domain.Task;
import com.personnel_accounting.employee.EmployeeService;
import com.personnel_accounting.entity.dto.ReportCardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

public class ReportCardConverter implements Converter<ReportCardDTO, ReportCard> {
    private ConversionService conversionService;
    private EmployeeService employeeService;

    @Autowired
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public ReportCard convert(ReportCardDTO source) {
        ReportCard reportCard = new ReportCard();
        reportCard.setId(source.getId());
        reportCard.setDate(source.getDate());
        reportCard.setTask(
                conversionService.convert(source.getTask(), Task.class));
        reportCard.setEmployee(
                conversionService.convert(source.getEmployee(), Employee.class));
        reportCard.setWorkingTime(source.getWorkingTime());
        return employeeService.mergeReportCard(reportCard);
    }
}
