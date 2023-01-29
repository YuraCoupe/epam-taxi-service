package com.epam.rd.java.basic.taxiservice.controller.command;

import com.epam.rd.java.basic.taxiservice.config.ConfigurationManager;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.CommandResult;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.RedirectResult;

import javax.servlet.http.HttpServletRequest;

public class ViewLoginPageCommand implements ActionCommand {
    @Override
    public CommandResult execute(HttpServletRequest request) {
        String page = ConfigurationManager.getProperty("path.page.login");
        return new RedirectResult(page);
    }
}
