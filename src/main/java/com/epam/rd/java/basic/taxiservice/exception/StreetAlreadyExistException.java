package com.epam.rd.java.basic.taxiservice.exception;

public class StreetAlreadyExistException extends RuntimeException{

    public StreetAlreadyExistException(String message) {
        super (message);
    }
}
