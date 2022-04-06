package com.personnel_accounting.exeption;

public class ExistingDataException extends RuntimeException{

    public ExistingDataException(String message) {
        super(message);
    }
}
