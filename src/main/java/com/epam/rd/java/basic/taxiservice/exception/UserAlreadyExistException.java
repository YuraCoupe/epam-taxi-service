package com.epam.rd.java.basic.taxiservice.exception;

public class UserAlreadyExistException extends RuntimeException{

    public UserAlreadyExistException(String message) {
        super (message);
    }
}
