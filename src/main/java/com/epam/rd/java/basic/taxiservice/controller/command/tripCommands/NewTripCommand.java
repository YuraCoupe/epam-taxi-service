package com.epam.rd.java.basic.taxiservice.controller.command.tripCommands;

import com.epam.rd.java.basic.taxiservice.config.ConfigurationManager;
import com.epam.rd.java.basic.taxiservice.controller.command.ActionCommand;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.CommandResult;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.ForwardResult;
import com.epam.rd.java.basic.taxiservice.model.Car.CarCategory;
import com.epam.rd.java.basic.taxiservice.model.Trip;
import com.epam.rd.java.basic.taxiservice.service.CarCategoryService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class NewTripCommand implements ActionCommand {
    @Override
    public CommandResult execute(HttpServletRequest request) throws UnsupportedEncodingException {
        ServletContext ctx = request.getServletContext();
        CarCategoryService categoryService = (CarCategoryService) ctx.getAttribute("carCategoryService");

        Trip trip = new Trip();

        request.setAttribute("trip", trip);
        List<CarCategory> categories = categoryService.findAll();
        request.setAttribute("categories", categories);
        request.setCharacterEncoding("UTF-8");
        request.setAttribute("msKey", System.getenv("MS_KEY"));
        String page = ConfigurationManager.getProperty("path.page.trips.new");
        return new ForwardResult(page);
    }
}
