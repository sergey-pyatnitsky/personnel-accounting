package com.personnel_accounting.exeption;

public class IncorrectDataException extends RuntimeException{

    public IncorrectDataException(String message) {
        super(message);
    }
}
