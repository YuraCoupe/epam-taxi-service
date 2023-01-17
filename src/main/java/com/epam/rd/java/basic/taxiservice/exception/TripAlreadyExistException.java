package com.epam.rd.java.basic.taxiservice.exception;

public class TripAlreadyExistException extends RuntimeException{

    public TripAlreadyExistException(String message) {
        super (message);
    }
}
