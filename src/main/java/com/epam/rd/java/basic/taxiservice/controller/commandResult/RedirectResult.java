package com.epam.rd.java.basic.taxiservice.controller.commandResult;

public class RedirectResult implements CommandResult {
    private final String redirectResource;
    public RedirectResult(String resource) {
        this.redirectResource = resource;
    }
    public String getResource() {
        return redirectResource;
    }
}
