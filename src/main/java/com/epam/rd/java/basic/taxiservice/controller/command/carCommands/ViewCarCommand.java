package com.epam.rd.java.basic.taxiservice.controller.command.carCommands;

import com.epam.rd.java.basic.taxiservice.config.ConfigurationManager;
import com.epam.rd.java.basic.taxiservice.controller.command.ActionCommand;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.CommandResult;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.ForwardResult;
import com.epam.rd.java.basic.taxiservice.model.Car.Car;
import com.epam.rd.java.basic.taxiservice.model.Trip;
import com.epam.rd.java.basic.taxiservice.service.CarService;
import com.epam.rd.java.basic.taxiservice.service.TripService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public class ViewCarCommand implements ActionCommand {
    @Override
    public CommandResult execute(HttpServletRequest request) throws UnsupportedEncodingException {
        ServletContext ctx = request.getServletContext();
        CarService carService = (CarService) ctx.getAttribute("carService");
        Integer id = Integer.parseInt(request.getParameter("id"));
        Car car = carService.findById(id);
        request.setAttribute("car", car);
        String page = ConfigurationManager.getProperty("path.page.cars.view");
        return new ForwardResult(page);
    }
}
