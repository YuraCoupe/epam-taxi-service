package com.epam.rd.java.basic.taxiservice.exception;

public class AddressNotFoundException extends RuntimeException{

    public AddressNotFoundException(String message) {
        super(message);
    }
}
