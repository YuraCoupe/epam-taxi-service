package com.epam.rd.java.basic.taxiservice.controller.command.carCommands;

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
import com.epam.rd.java.basic.taxiservice.model.Trip;
import com.epam.rd.java.basic.taxiservice.model.User;
import com.epam.rd.java.basic.taxiservice.service.*;
import com.epam.rd.java.basic.taxiservice.validator.CarValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.lang.invoke.MethodHandles;

public class UpdateCarCommand implements ActionCommand {
    final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public CommandResult execute(HttpServletRequest request) {
        ServletContext ctx = request.getServletContext();
        CarCategoryService carCategoryService = (CarCategoryService) ctx.getAttribute("carCategoryService");
        CarService carService = (CarService) ctx.getAttribute("carService");
        CarModelService carModelService = (CarModelService) ctx.getAttribute("carModelService");
        UserService userService = (UserService) ctx.getAttribute("userService");
        CarStatusService carStatusService = (CarStatusService) ctx.getAttribute("carStatusService");
        CarValidator validator = (CarValidator) ctx.getAttribute("carValidator");

        ErrorMessage errorMessage = validator.validate(request);
        if (!errorMessage.getErrors().isEmpty()) {
            request.setAttribute("errorMessage", errorMessage);
            String page = ConfigurationManager.getProperty("uri.page.cars.edit");
            return new ForwardResult(page);
        }

        int carId;
        Car car = new Car();

        carId = Integer.parseInt(request.getParameter("carId"));
        car.setId(carId);

        Integer modelId = Integer.parseInt(request.getParameter("id"));
        CarModel model = carModelService.findById(modelId);
        Integer capacity = Integer.parseInt(request.getParameter("capacity"));
        Integer categoryId = Integer.parseInt(request.getParameter("categoryId"));
        Integer statusId = Integer.parseInt(request.getParameter("statusId"));
        String licensePlate = request.getParameter("licensePlate");

        car.setModel(model);
        car.setCapacity(capacity);
        CarCategory category = carCategoryService.findById(categoryId);
        car.setCategory(category);
        CarStatus status = carStatusService.findById(statusId);
        car.setStatus(status);
        car.setLicensePlate(licensePlate);

        String driverIdString = request.getParameter("driverId");
        int driverId;
        User driver = new User();
        if (driverIdString != null && !driverIdString.isBlank()) {
            driverId = Integer.parseInt(driverIdString);
            driver = userService.findById(driverId);
        }
        car.setDriver(driver);

        if (car.getCurrentTrip() == null) {
            Trip currentTrip = new Trip();
            currentTrip.setId(null);
            car.setCurrentTrip(currentTrip);
        }

        carService.update(car);

        logger.info("Car {} {} {} was updated", car.getLicensePlate(), car.getModel().getBrand(), car.getModel().getModel());

        String page = ConfigurationManager.getProperty("path.uri.cars.view") + "?id=" + carId;
        return new RedirectResult(page);
    }
}
