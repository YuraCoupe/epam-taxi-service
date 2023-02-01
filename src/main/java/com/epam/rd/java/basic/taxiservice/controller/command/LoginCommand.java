package com.epam.rd.java.basic.taxiservice.controller.command;

import com.epam.rd.java.basic.taxiservice.config.ConfigurationManager;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.CommandResult;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.ForwardResult;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.RedirectResult;
import com.epam.rd.java.basic.taxiservice.exception.UserNotFoundException;
import com.epam.rd.java.basic.taxiservice.model.ErrorMessage;
import com.epam.rd.java.basic.taxiservice.model.User;
import com.epam.rd.java.basic.taxiservice.service.UserService;
import com.epam.rd.java.basic.taxiservice.validator.UserValidator;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class LoginCommand implements ActionCommand {
    private static final String PARAM_NAME_PHONE_NUMBER = "phoneNumber";
    private static final String PARAM_NAME_PASSWORD = "password";

    @Override
    public CommandResult execute(HttpServletRequest request) {
        String page;
        String phoneNumber = request.getParameter(PARAM_NAME_PHONE_NUMBER);
        String password = request.getParameter(PARAM_NAME_PASSWORD);
//        if (LoginLogic.checkLogin(login, pass)) {
//            request.setAttribute("user", login);
//            page = ConfigurationManager.getProperty("path.page.main");
//        } else {
//            request.setAttribute("errorLoginPassMessage", MessageManager.getProperty("message.loginerror"));
//            page = ConfigurationManager.getProperty("path.page.login");
//        }
//        return page;

        ServletContext ctx = request.getServletContext();

        UserValidator userValidator = (UserValidator) ctx.getAttribute("userValidator");

        ErrorMessage errorMessage = userValidator.validateLogin(request);
        if (!errorMessage.getErrors().isEmpty()) {
            request.setAttribute("errorMessage", errorMessage);
            page = ConfigurationManager.getProperty("path.page.login");
            return new ForwardResult(page);
        }

        User userFromDB;
        try {
            UserService userService = (UserService) ctx.getAttribute("userService");
            userFromDB = userService.findByPhoneNumber(phoneNumber);
        } catch (UserNotFoundException e) {
            errorMessage.setErrors(List.of("Wrong phone number or password. Please, try again"));
            request.setAttribute("errorMessage", errorMessage);
            page = ConfigurationManager.getProperty("path.page.login");
            return new ForwardResult(page);
        }

        PasswordEncoder passwordEncoder = (PasswordEncoder) ctx.getAttribute("passwordEncoder");
        String enctyptedPassword = passwordEncoder.encode(password);

        String language = (String) request.getSession().getAttribute("language");
        request.getSession().invalidate();

        if(passwordEncoder.matches(password, enctyptedPassword)){
            HttpSession session = request.getSession();
            session.setAttribute("isLoggedIn", true);
            session.setAttribute("phoneNumber", phoneNumber);
            session.setAttribute("user", userFromDB);
            session.setAttribute("language", language);
            page = ConfigurationManager.getProperty("path.uri.trips.list");
            return new RedirectResult(page);
        }else{
            errorMessage.setErrors(List.of("Wrong phone number or password. Please, try again"));
            request.setAttribute("errorMessage", errorMessage);
            page = ConfigurationManager.getProperty("path.page.login");
            return new ForwardResult(page);
        }
    }
}
