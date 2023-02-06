package com.epam.rd.java.basic.taxiservice.controller.command.carCommands;

import com.epam.rd.java.basic.taxiservice.config.ConfigurationManager;
import com.epam.rd.java.basic.taxiservice.controller.command.ActionCommand;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.CommandResult;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.RedirectResult;
import com.epam.rd.java.basic.taxiservice.model.Car.Car;
import com.epam.rd.java.basic.taxiservice.model.Trip;
import com.epam.rd.java.basic.taxiservice.service.CarService;
import com.epam.rd.java.basic.taxiservice.service.TripService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.lang.invoke.MethodHandles;

public class DeleteCarCommand implements ActionCommand {
    final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public CommandResult execute(HttpServletRequest request) {
        ServletContext ctx = request.getServletContext();
        CarService carService = (CarService) ctx.getAttribute("carService");
        Integer deleteId = Integer.parseInt(request.getParameter("id"));
        Car car = carService.findById(deleteId);
        carService.delete(car);
        logger.info("Car {} {} {} was deleted", car.getLicensePlate(), car.getModel().getBrand(), car.getModel().getModel());
        String page = ConfigurationManager.getProperty("path.page.cars.list") + car.getId();
        return new RedirectResult(page);

    }
}
