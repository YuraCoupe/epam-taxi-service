package com.epam.rd.java.basic.taxiservice.controller.command.clientCommands;

import com.epam.rd.java.basic.taxiservice.config.ConfigurationManager;
import com.epam.rd.java.basic.taxiservice.controller.command.ActionCommand;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.CommandResult;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.ForwardResult;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.RedirectResult;
import com.epam.rd.java.basic.taxiservice.model.ErrorMessage;
import com.epam.rd.java.basic.taxiservice.model.Role;
import com.epam.rd.java.basic.taxiservice.model.User;
import com.epam.rd.java.basic.taxiservice.service.RoleService;
import com.epam.rd.java.basic.taxiservice.service.UserService;
import com.epam.rd.java.basic.taxiservice.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;

public class SaveClientCommand implements ActionCommand {
    final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    @Override
    public CommandResult execute(HttpServletRequest request) throws UnsupportedEncodingException {
        ServletContext ctx = request.getServletContext();
        UserValidator validator = (UserValidator) ctx.getAttribute("userValidator");
        UserService userService = (UserService) ctx.getAttribute("userService");
        RoleService roleService = (RoleService) ctx.getAttribute("roleService");
        PasswordEncoder passwordEncoder = (PasswordEncoder) ctx.getAttribute("passwordEncoder");

        Integer userId;
        User user = new User();

        ErrorMessage errorMessage = validator.validate(request);
        if (!errorMessage.getErrors().isEmpty()) {
            request.setAttribute("errorMessage", errorMessage);
            String page = ConfigurationManager.getProperty("path.uri.clients.new");
            return new ForwardResult(page);
        }
        String phoneNumber = request.getParameter("phoneNumber");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String password = request.getParameter("password");
        String roleIdString = request.getParameter("roleId");

        user.setPhoneNumber(phoneNumber);
        user.setFirstName(firstName);
        user.setLastName(lastName);

        user.setPassword(passwordEncoder.encode(password));

        if (roleIdString == null) {
            user.setRole(roleService.getClientRole());
        } else {
            Integer roleId = Integer.parseInt(request.getParameter("roleId"));
            Role role = roleService.findById(roleId);
            user.setRole(role);
        }
        userId = userService.save(user);

        logger.info("New person {} {} {} was saved", user.getPhoneNumber(), user.getFirstName(), user.getLastName());

        String page = ConfigurationManager.getProperty("path.uri.clients.view") + "?id=" + userId;
        return new RedirectResult(page);
    }
}


