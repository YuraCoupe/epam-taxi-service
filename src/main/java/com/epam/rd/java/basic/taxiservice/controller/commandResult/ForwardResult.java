package com.epam.rd.java.basic.taxiservice.controller.commandResult;

public class ForwardResult implements CommandResult {
    private final String forwardResource;
    public ForwardResult(String resource) {
        this.forwardResource = resource;
    }
    public String getResource() {
        return forwardResource;
    }
}
