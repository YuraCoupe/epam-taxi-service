package com.epam.rd.java.basic.taxiservice.controller.command.clientCommands;

import com.epam.rd.java.basic.taxiservice.config.ConfigurationManager;
import com.epam.rd.java.basic.taxiservice.controller.command.ActionCommand;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.CommandResult;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.ForwardResult;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.RedirectResult;
import com.epam.rd.java.basic.taxiservice.model.Car.Car;
import com.epam.rd.java.basic.taxiservice.model.Car.CarCategory;
import com.epam.rd.java.basic.taxiservice.model.Car.CarModel;
import com.epam.rd.java.basic.taxiservice.model.Car.CarStatus;
import com.epam.rd.java.basic.taxiservice.model.ErrorMessage;
import com.epam.rd.java.basic.taxiservice.model.Role;
import com.epam.rd.java.basic.taxiservice.model.Trip;
import com.epam.rd.java.basic.taxiservice.model.User;
import com.epam.rd.java.basic.taxiservice.service.*;
import com.epam.rd.java.basic.taxiservice.validator.CarValidator;
import com.epam.rd.java.basic.taxiservice.validator.UserValidator;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class UpdateClientCommand implements ActionCommand {
    @Override
    public CommandResult execute(HttpServletRequest request) {
        ServletContext ctx = request.getServletContext();
        UserValidator validator = (UserValidator) ctx.getAttribute("userValidator");
        UserService userService = (UserService) ctx.getAttribute("userService");
        RoleService roleService = (RoleService) ctx.getAttribute("roleService");
        PasswordEncoder passwordEncoder = (PasswordEncoder) ctx.getAttribute("passwordEncoder");

        ErrorMessage errorMessage = validator.validate(request);
        if (!errorMessage.getErrors().isEmpty()) {
            request.setAttribute("errorMessage", errorMessage);
            String page = ConfigurationManager.getProperty("uri.page.clients.edit");
            return new ForwardResult(page);
        }

        Integer userId = null;
        User user = new User();

        userId = Integer.parseInt(request.getParameter("userId"));
        user.setId(userId);

        String phoneNumber = request.getParameter("phoneNumber");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String password = request.getParameter("password");
        String roleIdString = request.getParameter("roleId");

        user.setPhoneNumber(phoneNumber);
        user.setFirstName(firstName);
        user.setLastName(lastName);

        if (password.equals("********")) {
            User oldUser = userService.findById(user.getId());
            user.setPassword(oldUser.getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(password));
        }
        if (roleIdString == null) {
            user.setRole(roleService.getClientRole());
        } else {
            Integer roleId  = Integer.parseInt(request.getParameter("roleId"));
            Role role = roleService.findById(roleId);
            user.setRole(role);
        }

        userService.update(user);

        String page = ConfigurationManager.getProperty("uri.page.clients.view") + "?id=" + userId;
        return new RedirectResult(page);
    }
}
