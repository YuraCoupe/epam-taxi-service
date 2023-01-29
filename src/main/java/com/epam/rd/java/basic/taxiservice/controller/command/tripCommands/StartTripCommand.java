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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class StartTripCommand implements ActionCommand {
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
        String page = ConfigurationManager.getProperty("path.uri.trips.view") + "?id=" + tripId;
        return new RedirectResult(page);

    }
}
