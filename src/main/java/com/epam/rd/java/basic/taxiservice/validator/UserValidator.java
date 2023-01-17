package com.epam.rd.java.basic.taxiservice.validator;

import com.epam.rd.java.basic.taxiservice.model.ErrorMessage;
import com.epam.rd.java.basic.taxiservice.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class UserValidator {

    private final UserRepository userRepository;
    //private final DeveloperDAO developerDAO;
    //private final ProjectDAO projectDAO;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
//        this.developerDAO = developerDAO;
//        this.projectDAO = projectDAO;
    }

    public ErrorMessage validate(HttpServletRequest req) {
        ErrorMessage errorMessage = new ErrorMessage();
        List<String> errors = new ArrayList<>();

        String licensePlate = req.getParameter("phoneNumber");
        if (licensePlate.isBlank()) {
            errors.add("Phone number can not be empty");
        }

        String password = req.getParameter("password");
        if (password.isBlank()) {
            errors.add("Password can not be empty");
        }

        String firstName = req.getParameter("firstName");
        if (firstName.isBlank()) {
            errors.add("First name can not be empty");
        }

        String lastName = req.getParameter("lastName");
        if (lastName.isBlank()) {
            errors.add("Last name can not be empty");
        }

        String roleId = req.getParameter("roleId");
        Object isLoggedObj = req.getSession().getAttribute("isLoggedIn");
        if(isLoggedObj != null) {
            boolean isLoggedIn = (Boolean) isLoggedObj;
            if (roleId == null || roleId.isBlank() && isLoggedIn) {
                errors.add("Role can not be empty");
            }
        }

        errorMessage.setErrors(errors);
        return errorMessage;
    }

    public ErrorMessage validateLogin(HttpServletRequest req) {
        ErrorMessage errorMessage = new ErrorMessage();
        List<String> errors = new ArrayList<>();

        String licensePlate = req.getParameter("phoneNumber");
        if (licensePlate.isBlank()) {
            errors.add("Phone number can not be empty");
        }

        String password = req.getParameter("password");
        if (password.isBlank()) {
            errors.add("Password can not be empty");
        }

        errorMessage.setErrors(errors);
        return errorMessage;
    }
}

