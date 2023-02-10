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

public class DeleteTripCommand implements ActionCommand {
    final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public CommandResult execute(HttpServletRequest request) {
        ServletContext ctx = request.getServletContext();
        TripService tripService = (TripService) ctx.getAttribute("tripService");
        Integer deleteId = Integer.parseInt(request.getParameter("id"));
        Trip trip = tripService.findById(deleteId);
        tripService.delete(trip);

        logger.info("Trip {} was deleted", trip.getId());

        String page = ConfigurationManager.getProperty("path.uri.trips.list");
        return new RedirectResult(page);

    }
}
