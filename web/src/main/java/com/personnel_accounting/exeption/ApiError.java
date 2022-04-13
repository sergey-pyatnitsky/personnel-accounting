package com.personnel_accounting.exeption;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;

public class ApiError{

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date timestamp;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private int status;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String error;

    public ApiError(Date timestamp, int status, String error) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
    }
}
