package com.epam.rd.java.basic.taxiservice.exception;

public class TripNotFoundException extends RuntimeException{

    public TripNotFoundException(String message) {
        super(message);
    }
}
