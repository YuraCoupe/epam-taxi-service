package com.epam.rd.java.basic.taxiservice.validator;

import com.epam.rd.java.basic.taxiservice.model.BingRoute;
import com.epam.rd.java.basic.taxiservice.model.Car.CarCategory;
import com.epam.rd.java.basic.taxiservice.model.ErrorMessage;
import com.epam.rd.java.basic.taxiservice.repository.CarCategoryRepository;
import com.epam.rd.java.basic.taxiservice.repository.TripRepository;
import com.epam.rd.java.basic.taxiservice.service.BingMapsService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TripValidator {

    private final TripRepository tripRepository;
    private final CarCategoryRepository categoryRepository;
    private final BingMapsService bingMapsService;

    public TripValidator(TripRepository tripRepository, CarCategoryRepository categoryRepository, BingMapsService bingMapsService) {
        this.tripRepository = tripRepository;
        this.categoryRepository = categoryRepository;
        this.bingMapsService = bingMapsService;
    }

    public ErrorMessage validate(HttpServletRequest req) {
        ErrorMessage errorMessage = new ErrorMessage();
        List<String> errors = new ArrayList<>();

        String departureAddress = req.getParameter("departureAddress");
        String destinationAddress = req.getParameter("destinationAddress");


        if (departureAddress == null || departureAddress.isBlank()) {
            errors.add("Departure address can not be empty");
        }
        if (destinationAddress == null || destinationAddress.isBlank()) {
            errors.add("Destination address can not be empty");
        }
        if (departureAddress != null && !departureAddress.isBlank()
                && destinationAddress != null && !destinationAddress.isBlank()) {
            BingRoute bingRoute = bingMapsService.getRoute(departureAddress, destinationAddress);

            if (bingRoute.getStatusCode() != 200) {
                errors.addAll(bingRoute.getErrorMessage().getErrors());
            } else {
                if (bingRoute.getStartLocation().equals("Kyiv, Ukraine")) {
                    errors.add("Departure address doesn't exist");
                }
                if (bingRoute.getEndLocation().equals("Kyiv, Ukraine")) {
                    errors.add("Destination address doesn't exist");
                }
            }
        }

        String numberOfPassengersString = req.getParameter("passengersNumber");
        if (numberOfPassengersString == null || numberOfPassengersString.isBlank()) {
            errors.add("Number of passengers can not be empty");
        } else {
            int numberOfPassengers = Integer.parseInt(numberOfPassengersString);
            if (numberOfPassengers < 1 || numberOfPassengers > 7) {
                errors.add("Number of passengers should be from 1 to 7");
            }
        }

        String categoryIdString = req.getParameter("categoryId");
        if (categoryIdString == null || categoryIdString.isBlank()) {
            errors.add("Category can not be empty");
        } else {
            Integer categoryId = Integer.parseInt(categoryIdString);
            Optional<CarCategory> category = categoryRepository.findById(categoryId);
            if (category.isEmpty()) {
                errors.add("This category doesn't exist");
            }
        }

        errorMessage.setErrors(errors);
        return errorMessage;
    }
}
