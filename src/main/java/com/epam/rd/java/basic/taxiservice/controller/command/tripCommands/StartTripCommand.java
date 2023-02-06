package com.epam.rd.java.basic.taxiservice.controller.command.tripCommands;

import com.epam.rd.java.basic.taxiservice.config.ConfigurationManager;
import com.epam.rd.java.basic.taxiservice.controller.command.ActionCommand;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.CommandResult;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.RedirectResult;
import com.epam.rd.java.basic.taxiservice.model.Trip;
import com.epam.rd.java.basic.taxiservice.model.TripStatus;
import com.epam.rd.java.basic.taxiservice.model.User;
import com.epam.rd.java.basic.taxiservice.service.TripService;
import com.epam.rd.java.basic.taxiservice.service.TripStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.lang.invoke.MethodHandles;

public class StartTripCommand implements ActionCommand {
    final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public CommandResult execute(HttpServletRequest request) {
        ServletContext ctx = request.getServletContext();
        TripService tripService = (TripService) ctx.getAttribute("tripService");
        TripStatusService tripStatusService = (TripStatusService) ctx.getAttribute("tripStatusService");

        User loggedInUser = (User) request.getSession().getAttribute("user");
        Integer userId = loggedInUser.getId();

        Integer tripId = Integer.parseInt(request.getParameter("tripId"));
        Trip trip = tripService.findById(tripId);
        TripStatus status = tripStatusService.findByTitle("Processing");
        trip.setStatus(status);
        tripService.update(trip);

        logger.info(
                "Trip {} started by driver {} {} {}",
                trip.getId(),
                loggedInUser.getPhoneNumber(),
                loggedInUser.getFirstName(),
                loggedInUser.getLastName()
        );

        String page = ConfigurationManager.getProperty("path.uri.trips.view") + "?id=" + tripId;
        return new RedirectResult(page);

    }
}
