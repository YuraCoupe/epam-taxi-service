package com.epam.rd.java.basic.taxiservice.controller.command.clientCommands;

import com.epam.rd.java.basic.taxiservice.controller.command.ActionCommand;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.CommandResult;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public class ViewClientsListCommand implements ActionCommand {

    @Override
    public CommandResult execute(HttpServletRequest request) throws UnsupportedEncodingException {
        return null;
    }
}
