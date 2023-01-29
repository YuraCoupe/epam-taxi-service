package com.epam.rd.java.basic.taxiservice.controller.command.clientCommands;

import com.epam.rd.java.basic.taxiservice.config.ConfigurationManager;
import com.epam.rd.java.basic.taxiservice.controller.command.ActionCommand;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.CommandResult;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.ForwardResult;
import com.epam.rd.java.basic.taxiservice.model.User;
import com.epam.rd.java.basic.taxiservice.service.UserService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class ViewClientsListCommand implements ActionCommand {

    @Override
    public CommandResult execute(HttpServletRequest request) throws UnsupportedEncodingException {
        ServletContext ctx = request.getServletContext();
        UserService userService = (UserService) ctx.getAttribute("userService");

        int totalNumber = userService.getTotalNumber();
        int pageNumber = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
        int limit = 5;
        int offset = (pageNumber - 1) * limit;
        int pageCount = totalNumber / limit + 1;

        List<User> users = userService.findAllWithOffsetAndLimit(offset, limit);
        request.setAttribute("users", users);
        request.setAttribute("page", pageNumber);
        request.setAttribute("pageCount", pageCount);


        String page = ConfigurationManager.getProperty("path.page.clients.list");
        return new ForwardResult(page);
    }
}
