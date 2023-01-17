package com.epam.rd.java.basic.taxiservice.exception;

public class CarAlreadyExistException extends RuntimeException{

    public CarAlreadyExistException(String message) {
        super (message);
    }
}
