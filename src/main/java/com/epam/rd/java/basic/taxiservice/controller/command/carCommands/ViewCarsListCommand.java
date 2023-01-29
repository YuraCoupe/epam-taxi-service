package com.epam.rd.java.basic.taxiservice.controller.command.carCommands;

import com.epam.rd.java.basic.taxiservice.config.ConfigurationManager;
import com.epam.rd.java.basic.taxiservice.controller.command.ActionCommand;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.CommandResult;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.ForwardResult;
import com.epam.rd.java.basic.taxiservice.model.Car.Car;
import com.epam.rd.java.basic.taxiservice.model.Trip;
import com.epam.rd.java.basic.taxiservice.model.User;
import com.epam.rd.java.basic.taxiservice.service.CarService;
import com.epam.rd.java.basic.taxiservice.service.TripService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public class ViewCarsListCommand implements ActionCommand {

    @Override
    public CommandResult execute(HttpServletRequest request) {
        ServletContext ctx = request.getServletContext();
        CarService carService = (CarService) ctx.getAttribute("carService");

        int totalNumber = carService.getTotalNumber();
        int pageNumber = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
        int limit = 5;
        int offset = (pageNumber - 1) * limit;
        int pageCount = totalNumber / limit + 1;

        List<Car> cars = carService.findAllWithOffsetAndLimit(offset, limit);
        request.setAttribute("cars", cars);
        request.setAttribute("page", pageNumber);
        request.setAttribute("pageCount", pageCount);

        String page = ConfigurationManager.getProperty("path.page.cars.list");
        return new ForwardResult(page);
    }
}
