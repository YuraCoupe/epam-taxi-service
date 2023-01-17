package com.epam.rd.java.basic.taxiservice.exception;

public class TripStatusAlreadyExistException extends RuntimeException{

    public TripStatusAlreadyExistException(String message) {
        super (message);
    }
}
