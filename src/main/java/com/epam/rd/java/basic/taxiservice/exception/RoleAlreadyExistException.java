package com.epam.rd.java.basic.taxiservice.exception;

public class RoleAlreadyExistException extends RuntimeException{

    public RoleAlreadyExistException(String message) {
        super (message);
    }
}
