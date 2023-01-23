package com.epam.rd.java.basic.taxiservice.controller.command;

import com.epam.rd.java.basic.taxiservice.controller.commandResult.CommandResult;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public interface ActionCommand {
    CommandResult execute(HttpServletRequest request) throws UnsupportedEncodingException;
}
