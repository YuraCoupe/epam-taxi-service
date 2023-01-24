package com.epam.rd.java.basic.taxiservice.validator;

import com.epam.rd.java.basic.taxiservice.model.Car.CarStatus;
import com.epam.rd.java.basic.taxiservice.model.ErrorMessage;
import com.epam.rd.java.basic.taxiservice.repository.CarRepository;
import com.epam.rd.java.basic.taxiservice.repository.CarStatusRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class CarValidator {

    private final CarRepository carRepository;
    private final CarStatusRepository carStatusRepository;

    public CarValidator(CarRepository carRepository, CarStatusRepository carStatusRepository) {
        this.carRepository = carRepository;
        this.carStatusRepository =carStatusRepository;
    }

    public ErrorMessage validateCar(HttpServletRequest req) {
        ErrorMessage errorMessage = new ErrorMessage();
        List<String> errors = new ArrayList<>();

        String brand = req.getParameter("id");
        if (brand == null || brand.isBlank()) {
            errors.add("Car model can not be empty");
        }

        String licensePlate = req.getParameter("licensePlate");
        if (licensePlate.isBlank()) {
            errors.add("License plate can not be empty");
        }

        String capacity = req.getParameter("capacity");
        if (capacity == null || capacity.isBlank() || Integer.parseInt(capacity) <= 0) {
            errors.add("Capacity can not be empty");
        }

        String categoryId = req.getParameter("categoryId");
        if (categoryId == null || categoryId.isBlank()) {
            errors.add("Category can not be empty");
        }

        String statusId = req.getParameter("statusId");
        if (statusId == null || statusId.isBlank()) {
            errors.add("Status can not be empty");
        }

        String driverId = req.getParameter("driverId");

        if (statusId != null) {
            if (carStatusRepository.findById(Integer.parseInt(statusId)).isPresent()) {
                CarStatus carStatus = carStatusRepository.findById(Integer.parseInt(statusId)).get();
                if (carStatus.getTitle().equals("available for order") && (driverId == null || driverId.isBlank())) {
                    errors.add("Select driver for available for order car");
                }
            }
        }

        errorMessage.setErrors(errors);
        return errorMessage;
    }
}
