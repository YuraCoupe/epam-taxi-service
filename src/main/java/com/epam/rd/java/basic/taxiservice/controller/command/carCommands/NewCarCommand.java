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
import com.epam.rd.java.basic.taxiservice.service.CarCategoryService;
import com.epam.rd.java.basic.taxiservice.service.CarModelService;
import com.epam.rd.java.basic.taxiservice.service.CarStatusService;
import com.epam.rd.java.basic.taxiservice.service.UserService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class NewCarCommand implements ActionCommand {
    @Override
    public CommandResult execute(HttpServletRequest request) throws UnsupportedEncodingException {
        ServletContext ctx = request.getServletContext();
        CarCategoryService carCategoryService = (CarCategoryService) ctx.getAttribute("carCategoryService");
        CarModelService carModelService = (CarModelService) ctx.getAttribute("carModelService");
        CarStatusService carStatusService = (CarStatusService) ctx.getAttribute("carStatusService");
        UserService userService = (UserService) ctx.getAttribute("userService");

        Car car = new Car();
        car.setCapacity(0);

        request.setAttribute("car", car);
        List<CarModel> carModels = carModelService.findAll();
        request.setAttribute("carModels", carModels);
        List<CarCategory> carCategories = carCategoryService.findAll();
        request.setAttribute("carCategories", carCategories);
        List<CarStatus> carStatuses = carStatusService.findAll();
        request.setAttribute("carStatuses", carStatuses);
        List<User> freeDrivers = userService.findFreeDrivers();
        request.setAttribute("freeDrivers", freeDrivers);
        String page = ConfigurationManager.getProperty("path.page.cars.new");
        return new ForwardResult(page);
    }
}
