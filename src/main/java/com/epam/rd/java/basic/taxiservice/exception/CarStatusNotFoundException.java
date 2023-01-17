package com.epam.rd.java.basic.taxiservice.exception;

public class CarStatusNotFoundException extends RuntimeException{

    public CarStatusNotFoundException(String message) {
        super(message);
    }
}
