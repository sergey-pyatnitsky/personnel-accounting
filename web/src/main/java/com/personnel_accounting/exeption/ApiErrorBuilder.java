package com.personnel_accounting.exeption;

import java.sql.Date;

public class ApiErrorBuilder {
    private Date timestamp;
    private int status;
    private String error;

    private ApiErrorBuilder() {
    }

    public static ApiErrorBuilder anApiError() {
        return new ApiErrorBuilder();
    }

    public ApiErrorBuilder withTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public ApiErrorBuilder withStatus(int status) {
        this.status = status;
        return this;
    }

    public ApiErrorBuilder withError(String error) {
        this.error = error;
        return this;
    }

    public ApiError build() {
        return new ApiError(timestamp, status, error);
    }
}
