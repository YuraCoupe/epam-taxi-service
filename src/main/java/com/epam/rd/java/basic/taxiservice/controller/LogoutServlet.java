package com.epam.rd.java.basic.taxiservice.controller;

import com.epam.rd.java.basic.taxiservice.config.DatabaseManager;
import com.epam.rd.java.basic.taxiservice.config.PostgresHikariProvider;
import com.epam.rd.java.basic.taxiservice.config.PropertiesUtil;
import com.epam.rd.java.basic.taxiservice.exception.UserNotFoundException;
import com.epam.rd.java.basic.taxiservice.model.ErrorMessage;
import com.epam.rd.java.basic.taxiservice.model.User;
import com.epam.rd.java.basic.taxiservice.repository.UserRepository;
import com.epam.rd.java.basic.taxiservice.service.UserService;
import com.epam.rd.java.basic.taxiservice.validator.UserValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate();
        resp.sendRedirect("login");    }
}