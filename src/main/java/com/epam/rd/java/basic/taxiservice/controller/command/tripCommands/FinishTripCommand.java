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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.lang.invoke.MethodHandles;
import java.sql.Timestamp;

public class FinishTripCommand implements ActionCommand {
    final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
                logger.info(
                        "Driver {} {} {} finished trip {}",
                        loggedInUser.getPhoneNumber(),
                        loggedInUser.getFirstName(),
                        loggedInUser.getLastName(),
                        trip.getId()
                );
                logger.info("Car {} {} {} status changed to {}",
                        car.getLicensePlate(),
                        car.getModel().getBrand(),
                        car.getModel().getModel(),
                        carStatus.getTitle());
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
            trip.setCloseTime(new Timestamp(System.currentTimeMillis() / 60000L * 60000L));
            tripService.update(trip);
            logger.info("Trip {} finished", trip.getId());
        }

        String page = ConfigurationManager.getProperty("path.uri.trips.view") + "?id=" + tripId;
        return new RedirectResult(page);
    }
}
