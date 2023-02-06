package com.epam.rd.java.basic.taxiservice.controller.command.tripCommands;

import com.epam.rd.java.basic.taxiservice.config.ConfigurationManager;
import com.epam.rd.java.basic.taxiservice.controller.command.ActionCommand;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.CommandResult;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.ForwardResult;
import com.epam.rd.java.basic.taxiservice.model.Trip;
import com.epam.rd.java.basic.taxiservice.model.User;
import com.epam.rd.java.basic.taxiservice.service.TripService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public class ViewTripsListCommand implements ActionCommand {

    @Override
    public CommandResult execute(HttpServletRequest request) {
        ServletContext ctx = request.getServletContext();
        TripService tripService = (TripService) ctx.getAttribute("tripService");

        User loggedInUser = (User) request.getSession().getAttribute("user");
        String loggedUserRoleTitle = loggedInUser.getRole().getTitle();
        if (request.getSession().getAttribute("activeTripId") == null) {
            if (loggedUserRoleTitle.equals("ROLE_CLIENT")) {
                Optional<Trip> activeTripOptional = tripService.findActiveByClientId(loggedInUser.getId());
                activeTripOptional.ifPresent(trip -> request.getSession().setAttribute("activeTripId", trip.getId()));
            } else if (loggedUserRoleTitle.equals("ROLE_DRIVER")) {
                Optional<Trip> activeTripOptional = tripService.findActiveByDriverId(loggedInUser.getId());
                activeTripOptional.ifPresent(trip -> request.getSession().setAttribute("activeTripId", trip.getId()));
            }
        } else {
            if (loggedUserRoleTitle.equals("ROLE_CLIENT")) {
                Optional<Trip> activeTripOptional = tripService.findActiveByClientId(loggedInUser.getId());
                if (activeTripOptional.isEmpty()) {
                    request.getSession().removeAttribute("activeTripId");
                }
            } else if (loggedUserRoleTitle.equals("ROLE_DRIVER")) {
                Optional<Trip> activeTripOptional = tripService.findActiveByDriverId(loggedInUser.getId());
                if (activeTripOptional.isEmpty()) {
                    request.getSession().removeAttribute("activeTripId");
                }
            }
        }

        String fieldToSort = request.getParameter("tripsFieldToSort");
        String tripsChangeSortOrder = request.getParameter("tripsChangeSortOrder");
        String sortOrder = (String) request.getSession().getAttribute("tripsSortOrder");
        if (fieldToSort == null) {
            fieldToSort = (String) request.getSession().getAttribute("tripsFieldToSort");
        } else {
            request.getSession().setAttribute("tripsFieldToSort", fieldToSort);
        }
        if (tripsChangeSortOrder != null) {
            sortOrder = sortOrder.equals("DESC") ? "ASC" : "DESC";
            request.getSession().setAttribute("tripsSortOrder", sortOrder);
        }

        int totalNumber;
        int pageNumber = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
        int limit = 5;
        int offset = (pageNumber - 1) * limit;
        List<Trip> trips;

        switch (loggedUserRoleTitle) {
            case "ROLE_ADMINISTRATOR": {
                totalNumber = tripService.getTotalNumber();
                trips = tripService.findAllWithOffsetAndLimit(fieldToSort, sortOrder, offset, limit);
                break;
            }
            case "ROLE_DRIVER": {
                totalNumber = tripService.getTotalNumberByDriverId(loggedInUser.getId());
                trips = tripService.findByDriverIdWithOffsetAndLimit(loggedInUser.getId(), fieldToSort, sortOrder, offset, limit);
                break;
            }
            default: {
                totalNumber = tripService.getTotalNumberByUserId(loggedInUser.getId());
                trips = tripService.findByUserIdWithOffsetAndLimit(loggedInUser.getId(), fieldToSort, sortOrder, offset, limit);
            }
        }

        int pageCount = totalNumber % limit == 0 ? totalNumber / limit : totalNumber / limit + 1;
        request.setAttribute("trips", trips);
        request.setAttribute("page", pageNumber);
        request.setAttribute("pageCount", pageCount);

        String page = ConfigurationManager.getProperty("path.page.trips.list");
        return new ForwardResult(page);
    }
}
