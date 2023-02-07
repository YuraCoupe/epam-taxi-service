package com.epam.rd.java.basic.taxiservice.controller.command.tripCommands;

import com.epam.rd.java.basic.taxiservice.config.ConfigurationManager;
import com.epam.rd.java.basic.taxiservice.controller.command.ActionCommand;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.CommandResult;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.ForwardResult;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.RedirectResult;
import com.epam.rd.java.basic.taxiservice.exception.CarNotFoundException;
import com.epam.rd.java.basic.taxiservice.model.*;
import com.epam.rd.java.basic.taxiservice.model.Car.Car;
import com.epam.rd.java.basic.taxiservice.model.Car.CarCategory;
import com.epam.rd.java.basic.taxiservice.model.Car.CarStatus;
import com.epam.rd.java.basic.taxiservice.service.*;
import com.epam.rd.java.basic.taxiservice.validator.TripValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SaveTripCommand implements ActionCommand {
    final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public CommandResult execute(HttpServletRequest request) {
        ServletContext ctx = request.getServletContext();
        CarCategoryService categoryService = (CarCategoryService) ctx.getAttribute("carCategoryService");
        CarService carService = (CarService) ctx.getAttribute("carService");
        UserService userService = (UserService) ctx.getAttribute("userService");
        TripStatusService tripStatusService = (TripStatusService) ctx.getAttribute("tripStatusService");
        BingMapsService bingMapsService = (BingMapsService) ctx.getAttribute("bingMapsService");
        PriceService priceService = (PriceService) ctx.getAttribute("priceService");
        TripService tripService = (TripService) ctx.getAttribute("tripService");
        CarStatusService carStatusService = (CarStatusService) ctx.getAttribute("carStatusService");
           TripValidator validator = (TripValidator) ctx.getAttribute("tripValidator");

        User loggedInUser = (User) request.getSession().getAttribute("user");
        Integer userId = loggedInUser.getId();

        ErrorMessage errorMessage = validator.validate(request);
        if (!errorMessage.getErrors().isEmpty()) {
            request.setAttribute("errorMessage", errorMessage);
            String page = ConfigurationManager.getProperty("path.uri.trips.new");
            return new ForwardResult(page);
        }

        String allowSeveralCars = request.getParameter("allowSeveralCars");
        List<Car> cars = new ArrayList<>();
        if (allowSeveralCars == null) {
            try {
                int categoryId = Integer.parseInt(request.getParameter("categoryId"));
                CarCategory category = categoryService.findById(categoryId);
                int capacity = Integer.parseInt(request.getParameter("passengersNumber"));
                cars.add(carService.findOneByCategoryAndCapacity(category.getTitle(), capacity));
            } catch (CarNotFoundException e) {
                request.setAttribute("noCarAvailable", "true");
                String page = ConfigurationManager.getProperty("path.uri.trips.new");
                return new ForwardResult(page);
            }
        } else {
            try {
                int categoryId = Integer.parseInt(request.getParameter("categoryId"));
                CarCategory category = categoryService.findById(categoryId);
                int capacity = Integer.parseInt(request.getParameter("passengersNumber"));
                cars.addAll(carService.findSeveralByCategoryAndCapacity(category.getTitle(), capacity));
            } catch (CarNotFoundException e) {
                request.setAttribute("noCarAvailable", "true");
                String page = ConfigurationManager.getProperty("path.uri.trips.new");
                return new ForwardResult(page);
            }
        }

        Trip trip = new Trip();

        User user = userService.findById(userId);
        String departureAddress = request.getParameter("departureAddress");
        String destinationAddress = request.getParameter("destinationAddress");
        Integer numberOfPassengers = Integer.parseInt(request.getParameter("passengersNumber"));
        Integer categoryId = Integer.parseInt(request.getParameter("categoryId"));
        CarCategory category = categoryService.findById(categoryId);
        TripStatus status = tripStatusService.findByTitle("Open");

        trip.setUser(user);

        trip.setNumberOfPassengers(numberOfPassengers);
        trip.setCategory(category);
        trip.setStatus(status);
        BingRoute bingRoute = bingMapsService.getRoute(departureAddress, destinationAddress);
        trip.setDepartureAddress(bingRoute.getStartLocation());
        trip.setDestinationAddress(bingRoute.getEndLocation());
        trip.setDistance(bingRoute.getTravelDistance());
        trip.setCars(new HashSet<>(cars));
        BigDecimal price = priceService.calculateTripPrice(trip.getDistance(), user.getSumSpent(), category, trip.getCars().size());
        trip.setPrice(price);
        trip.setOpenTime(new Timestamp(System.currentTimeMillis() / 60000L * 60000L));

        request.setAttribute("trip", trip);

        Integer tripId = tripService.save(trip);

        logger.info("New trip {} was created", trip.getId());

        CarStatus carStatus = carStatusService.findByTitle("on route");
        cars.forEach(car -> {
            car.setStatus(carStatus);
            car.setCurrentTrip(trip);
            carService.update(car);
            logger.info(
                    "Car {} changed status to {} and assigned to trip {}",
                    car.getLicensePlate(),
                    carStatus.getTitle(),
                    trip.getId()
            );
        });

        request.getSession().setAttribute("activeTripId", tripId);

        String page = ConfigurationManager.getProperty("path.uri.trips.view") + "?id=" + tripId;
        return new RedirectResult(page);
    }
}
