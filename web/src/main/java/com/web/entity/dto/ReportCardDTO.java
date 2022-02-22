package com.web.entity.dto;

import lombok.Data;

import java.sql.Date;
import java.sql.Time;

@Data
public class ReportCardDTO {
    private Long id;
    private Date date;
    private TaskDTO task;
    private EmployeeDTO employee;
    private Time workingTime;
}
