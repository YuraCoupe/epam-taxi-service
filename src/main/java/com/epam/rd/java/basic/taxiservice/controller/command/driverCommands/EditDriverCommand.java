package com.epam.rd.java.basic.taxiservice.controller.command.driverCommands;

import com.epam.rd.java.basic.taxiservice.controller.command.ActionCommand;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.CommandResult;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public class EditDriverCommand implements ActionCommand {

    @Override
    public CommandResult execute(HttpServletRequest request) throws UnsupportedEncodingException {
        return null;
    }
}
