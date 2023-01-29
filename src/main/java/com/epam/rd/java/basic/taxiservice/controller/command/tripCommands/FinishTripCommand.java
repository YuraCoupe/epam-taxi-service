package com.epam.rd.java.basic.taxiservice.controller.command.tripCommands;

import com.epam.rd.java.basic.taxiservice.config.ConfigurationManager;
import com.epam.rd.java.basic.taxiservice.controller.command.ActionCommand;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.CommandResult;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.RedirectResult;
import com.epam.rd.java.basic.taxiservice.model.Car.Car;
import com.epam.rd.java.basic.taxiservice.model.Car.CarStatus;
import com.epam.rd.java.basic.taxiservice.model.Trip;
import com.epam.rd.java.basic.taxiservice.model.TripStatus;
import com.epam.rd.java.basic.taxiservice.model.User;
import com.epam.rd.java.basic.taxiservice.service.CarService;
import com.epam.rd.java.basic.taxiservice.service.CarStatusService;
import com.epam.rd.java.basic.taxiservice.service.TripService;
import com.epam.rd.java.basic.taxiservice.service.TripStatusService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

public class FinishTripCommand implements ActionCommand {
    @Override
    public CommandResult execute(HttpServletRequest request) {
        ServletContext ctx = request.getServletContext();
        TripService tripService = (TripService) ctx.getAttribute("tripService");
        TripStatusService tripStatusService = (TripStatusService) ctx.getAttribute("tripStatusService");
        CarStatusService carStatusService = (CarStatusService) ctx.getAttribute("carStatusService");
        CarService carService = (CarService) ctx.getAttribute("carService");

        User loggedInUser = (User) request.getSession().getAttribute("user");
        Integer userId = loggedInUser.getId();

        Integer tripId = Integer.parseInt(request.getParameter("tripId"));
        Trip trip = tripService.findById(tripId);

        CarStatus carStatus = carStatusService.findByTitle("available for order");
        for (Car car : trip.getCars()) {
            if (car.getDriver().getId().equals(userId)) {
                car.setStatus(carStatus);
                car.getCurrentTrip().setId(null);
                carService.update(car);
            }
        }

        request.getSession().removeAttribute("activeTripId");

        boolean tripIsCompleted = true;
        for (Car car : trip.getCars()) {
            if (car.getStatus().getTitle().equals("on route")) {
                tripIsCompleted = false;
                break;
            }
        }
        if (tripIsCompleted) {
            TripStatus status = tripStatusService.findByTitle("Completed");
            trip.setStatus(status);
            trip.setCloseTime(new Timestamp(System.currentTimeMillis()));
            tripService.update(trip);
        }

        String page = ConfigurationManager.getProperty("uri.page.trips.view") + "?id=" + tripId;
        return new RedirectResult(page);
    }
}
