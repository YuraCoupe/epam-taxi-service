package com.epam.rd.java.basic.taxiservice.exception;

public class TripStatusNotFoundException extends RuntimeException{

    public TripStatusNotFoundException(String message) {
        super(message);
    }
}
