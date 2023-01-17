package com.epam.rd.java.basic.taxiservice.exception;

public class CarStatusAlreadyExistException extends RuntimeException{

    public CarStatusAlreadyExistException(String message) {
        super (message);
    }
}
