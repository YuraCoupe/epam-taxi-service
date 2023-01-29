package com.epam.rd.java.basic.taxiservice.controller;

import com.epam.rd.java.basic.taxiservice.config.DatabaseManager;
import com.epam.rd.java.basic.taxiservice.config.PostgresHikariProvider;
import com.epam.rd.java.basic.taxiservice.config.PropertiesUtil;
import com.epam.rd.java.basic.taxiservice.model.ErrorMessage;
import com.epam.rd.java.basic.taxiservice.model.User;
import com.epam.rd.java.basic.taxiservice.model.Role;
import com.epam.rd.java.basic.taxiservice.repository.*;
import com.epam.rd.java.basic.taxiservice.service.*;
import com.epam.rd.java.basic.taxiservice.validator.UserValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

//@WebServlet(urlPatterns = "/users/*")
public class UserServlet extends HttpServlet {
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private UserValidator validator;


    @Override
    public void init() {
        ServletContext ctx = getServletContext();
        this.userService = (UserService) ctx.getAttribute("userService");
        this.roleService = (RoleService) ctx.getAttribute("roleService");
        validator =(UserValidator) ctx.getAttribute("userValidator");
        passwordEncoder = (PasswordEncoder) ctx.getAttribute("passwordEncoder");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Integer userId = null;
        User user = new User();

        if (!req.getParameter("userId").isBlank()) {
            userId = Integer.parseInt(req.getParameter("userId"));
            user.setId(userId);
        }

        ErrorMessage errorMessage = validator.validate(req);
        if (!errorMessage.getErrors().isEmpty()) {
            req.setAttribute("errorMessage", errorMessage);
            if (Objects.nonNull(userId)) {
                handleId(userId, req, resp);
            } else {
                handleNew(req, resp);
            }
            return;
        }

        String phoneNumber = req.getParameter("phoneNumber");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String password = req.getParameter("password");
        String roleIdString = req.getParameter("roleId");

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
            Integer roleId  = Integer.parseInt(req.getParameter("roleId"));
            Role role = roleService.findById(roleId);
            user.setRole(role);
        }

        if (Objects.isNull(userId)) {
            userId = userService.save(user);
        } else {
            userService.update(user);
        }

        resp.sendRedirect("/users");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        String idStr = requestURI.replaceAll("/users/?", "");
        String deleteId = req.getParameter("deleteId");
        String personId = req.getParameter("id");
        if (deleteId != null) {
            User person = userService.findById(Integer.parseInt(deleteId));
            userService.delete(person);
            resp.sendRedirect("/users");
        } else if ("new".equalsIgnoreCase(idStr)) {
            handleNew(req, resp);
        } else if (personId != null) {
            handleId(Integer.parseInt(personId), req, resp);
        } else if (!idStr.equals("")) {
            try {
                Integer id = Integer.parseInt(idStr);

                handleId(id, req, resp);
            } catch (RuntimeException e) {
                resp.sendRedirect("/users");
            }
        } else {
            int totalNumber = userService.getTotalNumber();
            int page = req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 1;
            int limit = 5;
            int offset = (page - 1) * limit;
            int pageCount = totalNumber / limit + 1;

            List<User> users = userService.findAllWithOffsetAndLimit(offset, limit);
            req.setAttribute("users", users);
            req.setAttribute("page", page);
            req.setAttribute("pageCount", pageCount);
            req.getRequestDispatcher("/jsp/users.jsp").forward(req, resp);
        }
    }

    private void handleNew(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("user", new User());
        List<Role> roles = roleService.findAll();
        req.setAttribute("roles", roles);
        req.getRequestDispatcher("/jsp/user.jsp").forward(req, resp);
    }

    private void handleId(Integer id, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = userService.findById(id);
        user.setPassword("*********");
        req.setAttribute("user", user);
        List<Role> roles = roleService.findAll();
        req.setAttribute("roles", roles);
        req.setCharacterEncoding("UTF-8");
        req.getRequestDispatcher("/jsp/user.jsp").forward(req, resp);
    }
}
