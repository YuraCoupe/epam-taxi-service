package com.epam.rd.java.basic.taxiservice.controller.old;

import com.epam.rd.java.basic.taxiservice.exception.UserNotFoundException;
import com.epam.rd.java.basic.taxiservice.model.ErrorMessage;
import com.epam.rd.java.basic.taxiservice.model.User;
import com.epam.rd.java.basic.taxiservice.service.*;
import com.epam.rd.java.basic.taxiservice.validator.UserValidator;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

//@WebServlet({"/login"})
public class LoginServlet extends HttpServlet {
    private UserValidator userValidator;
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    public void init() {
        ServletContext ctx = getServletContext();
        userValidator = (UserValidator) ctx.getAttribute("userValidator");
        userService = (UserService) ctx.getAttribute("userService");
        passwordEncoder = (PasswordEncoder) ctx.getAttribute("passwordEncoder");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String phoneNumber = req.getParameter("phoneNumber");
        String password = req.getParameter("password");

        ErrorMessage errorMessage = userValidator.validateLogin(req);
        if (!errorMessage.getErrors().isEmpty()) {
            req.setAttribute("errorMessage", errorMessage);
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
            return;
        }

        User userFromDB;
        try {
            userFromDB = userService.findByPhoneNumber(phoneNumber);
        } catch (UserNotFoundException e) {
            errorMessage.setErrors(List.of("Wrong phone number or password. Please, try again"));
            req.setAttribute("errorMessage", errorMessage);
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
            return;
        }

        String enctyptedPassword = passwordEncoder.encode(password);


        if(passwordEncoder.matches(password, enctyptedPassword)){
            HttpSession session = req.getSession();
            session.setAttribute("isLoggedIn", true);
            session.setAttribute("phoneNumber", phoneNumber);
            session.setAttribute("user", userFromDB);
            resp.sendRedirect("/trips"); //this page should be only acccessed after login
        }else{
            resp.sendRedirect("login?error=true");
        }
    }
}