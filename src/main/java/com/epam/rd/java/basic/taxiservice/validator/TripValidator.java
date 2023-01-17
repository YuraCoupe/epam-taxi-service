package com.epam.rd.java.basic.taxiservice.validator;

import com.epam.rd.java.basic.taxiservice.model.Address;
import com.epam.rd.java.basic.taxiservice.model.Car.CarCategory;
import com.epam.rd.java.basic.taxiservice.model.ErrorMessage;
import com.epam.rd.java.basic.taxiservice.repository.AddressRepository;
import com.epam.rd.java.basic.taxiservice.repository.CarCategoryRepository;
import com.epam.rd.java.basic.taxiservice.repository.CarRepository;
import com.epam.rd.java.basic.taxiservice.repository.TripRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TripValidator {

    private final TripRepository tripRepository;
    private final AddressRepository addressRepository;
    private final CarCategoryRepository categoryRepository;

    public TripValidator(TripRepository tripRepository, AddressRepository addressRepository, CarCategoryRepository categoryRepository) {
        this.tripRepository = tripRepository;
        this.addressRepository = addressRepository;
        this.categoryRepository = categoryRepository;
    }

    public ErrorMessage validate(HttpServletRequest req) {
        ErrorMessage errorMessage = new ErrorMessage();
        List<String> errors = new ArrayList<>();

        String departureAddressIdString = req.getParameter("departureAddressId");
        if (departureAddressIdString == null || departureAddressIdString.isBlank()) {
            errors.add("Departure address can not be empty");
        } else {
            Integer departureAddressId = Integer.parseInt(departureAddressIdString);
            Optional<Address> departureAddress = addressRepository.findById(departureAddressId);
            if (departureAddress.isEmpty()) {
                errors.add("This departure address doesn't exist");
            }
        }

        String destinationAddressIdString = req.getParameter("destinationAddressId");
        if (destinationAddressIdString == null || destinationAddressIdString.isBlank()) {
            errors.add("Destination address can not be empty");
        } else {
            Integer destinationAddressId = Integer.parseInt(destinationAddressIdString);
            Optional<Address> destinationAddress = addressRepository.findById(destinationAddressId);
            if (destinationAddress.isEmpty()) {
                errors.add("This destination address doesn't exist");
            }
        }
        String numberOfPassengersString = req.getParameter("passengersNumber");
        if (numberOfPassengersString == null || numberOfPassengersString.isBlank()) {
            errors.add("Number of passengers can not be empty");
        } else {
            Integer numberOfPassengers = Integer.parseInt(numberOfPassengersString);
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
