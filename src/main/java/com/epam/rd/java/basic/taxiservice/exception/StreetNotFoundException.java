package com.epam.rd.java.basic.taxiservice.exception;

public class StreetNotFoundException extends RuntimeException{

    public StreetNotFoundException(String message) {
        super(message);
    }
}
