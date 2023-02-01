package com.epam.rd.java.basic.taxiservice.controller.command;

import com.epam.rd.java.basic.taxiservice.config.ConfigurationManager;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.CommandResult;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.ForwardResult;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.RedirectResult;

import javax.servlet.http.HttpServletRequest;

public class ViewIndexPageCommand implements ActionCommand {
    @Override
    public CommandResult execute(HttpServletRequest request) {
        String page = ConfigurationManager.getProperty("path.page.index");
        return new ForwardResult(page);
    }
}
