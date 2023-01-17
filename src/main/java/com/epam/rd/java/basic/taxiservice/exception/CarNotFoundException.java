package com.epam.rd.java.basic.taxiservice.exception;

public class CarNotFoundException extends RuntimeException{

    public CarNotFoundException(String message) {
        super(message);
    }
}
