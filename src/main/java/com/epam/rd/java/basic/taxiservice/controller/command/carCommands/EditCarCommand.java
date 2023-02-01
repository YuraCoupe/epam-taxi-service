package com.epam.rd.java.basic.taxiservice.controller.command.carCommands;

import com.epam.rd.java.basic.taxiservice.config.ConfigurationManager;
import com.epam.rd.java.basic.taxiservice.controller.command.ActionCommand;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.CommandResult;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.ForwardResult;
import com.epam.rd.java.basic.taxiservice.model.Car.Car;
import com.epam.rd.java.basic.taxiservice.model.Car.CarCategory;
import com.epam.rd.java.basic.taxiservice.model.Car.CarModel;
import com.epam.rd.java.basic.taxiservice.model.Car.CarStatus;
import com.epam.rd.java.basic.taxiservice.model.Trip;
import com.epam.rd.java.basic.taxiservice.model.User;
import com.epam.rd.java.basic.taxiservice.service.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class EditCarCommand implements ActionCommand {
    @Override
    public CommandResult execute(HttpServletRequest request) throws UnsupportedEncodingException {
        ServletContext ctx = request.getServletContext();
        CarCategoryService carCategoryService = (CarCategoryService) ctx.getAttribute("carCategoryService");
        CarService carService = (CarService) ctx.getAttribute("carService");
        CarModelService carModelService = (CarModelService) ctx.getAttribute("carModelService");
        CarStatusService carStatusService = (CarStatusService) ctx.getAttribute("carStatusService");
        UserService userService = (UserService) ctx.getAttribute("userService");

        Integer id = Integer.parseInt(request.getParameter("id"));

        Car car = carService.findById(id);
        request.setAttribute("car", car);
        List<CarModel> carModels = carModelService.findAll();
        request.setAttribute("carModels", carModels);
        List<CarCategory> carCategories = carCategoryService.findAll();
        request.setAttribute("carCategories", carCategories);
        List<CarStatus> carStatuses = carStatusService.findAll();
        request.setAttribute("carStatuses", carStatuses);
        List<User> freeDrivers = userService.findFreeDrivers();
        request.setAttribute("freeDrivers", freeDrivers);

        String page = ConfigurationManager.getProperty("path.page.cars.edit");
        return new ForwardResult(page);
    }
}
