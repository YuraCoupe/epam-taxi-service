package com.epam.rd.java.basic.taxiservice.controller.command.clientCommands;

import com.epam.rd.java.basic.taxiservice.config.ConfigurationManager;
import com.epam.rd.java.basic.taxiservice.controller.command.ActionCommand;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.CommandResult;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.RedirectResult;
import com.epam.rd.java.basic.taxiservice.model.User;
import com.epam.rd.java.basic.taxiservice.service.TripService;
import com.epam.rd.java.basic.taxiservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;

public class DeleteClientCommand implements ActionCommand {
    final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    @Override
    public CommandResult execute(HttpServletRequest request) throws UnsupportedEncodingException {
        ServletContext ctx = request.getServletContext();
        UserService userService = (UserService) ctx.getAttribute("userService");
        Integer deleteId = Integer.parseInt(request.getParameter("id"));

        User person = userService.findById(deleteId);
        userService.delete(person);

        logger.info("Person {} {} {} was deleted", person.getPhoneNumber(), person.getFirstName(), person.getLastName());

        String page = ConfigurationManager.getProperty("path.page.clients.list");
        return new RedirectResult(page);
    }
}
