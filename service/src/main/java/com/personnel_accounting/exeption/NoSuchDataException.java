package com.personnel_accounting.exeption;

public class NoSuchDataException extends RuntimeException{

    public NoSuchDataException(String message) {
        super(message);
    }
}
