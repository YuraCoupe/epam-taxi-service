package com.epam.rd.java.basic.taxiservice.exception;

public class AddressAlreadyExistException extends RuntimeException{

    public AddressAlreadyExistException(String message) {
        super (message);
    }
}
