package com.epam.rd.java.basic.taxiservice.controller.command.clientCommands;

import com.epam.rd.java.basic.taxiservice.config.ConfigurationManager;
import com.epam.rd.java.basic.taxiservice.controller.command.ActionCommand;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.CommandResult;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.ForwardResult;
import com.epam.rd.java.basic.taxiservice.model.Role;
import com.epam.rd.java.basic.taxiservice.model.User;
import com.epam.rd.java.basic.taxiservice.service.RoleService;
import com.epam.rd.java.basic.taxiservice.service.UserService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class NewClientCommand implements ActionCommand {

    @Override
    public CommandResult execute(HttpServletRequest request) throws UnsupportedEncodingException {
        ServletContext ctx = request.getServletContext();
        UserService userService = (UserService) ctx.getAttribute("userService");
        RoleService roleService = (RoleService) ctx.getAttribute("roleService");
        request.setAttribute("user", new User());
        List<Role> roles = roleService.findAll();
        request.setAttribute("roles", roles);
        String page = ConfigurationManager.getProperty("path.page.clients.new");
        return new ForwardResult(page);
    }
}
