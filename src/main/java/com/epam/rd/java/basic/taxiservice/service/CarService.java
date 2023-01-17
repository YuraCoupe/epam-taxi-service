package com.epam.rd.java.basic.taxiservice.service;

import com.epam.rd.java.basic.taxiservice.exception.CarAlreadyExistException;
import com.epam.rd.java.basic.taxiservice.exception.CarNotFoundException;
import com.epam.rd.java.basic.taxiservice.model.Car.Car;
import com.epam.rd.java.basic.taxiservice.repository.CarRepository;

import java.util.ArrayList;
import java.util.List;

public class CarService {
    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Integer save(Car car) {
        Integer id = null;
        if (carRepository.findByLicensePlate(car.getLicensePlate()).isEmpty()) {
            id = carRepository.save(car);
        } else {
            throw new CarAlreadyExistException("This car already exists");
        }
        return id;
    }

    public void delete(Car car) {
        if (!carRepository.findById(car.getId()).isEmpty()) {
            carRepository.delete(car);
        } else {
            throw new CarNotFoundException("This car doesn't exist");
        }
    }

    public void update(Car car) {
            carRepository.update(car);
    }

    public List<Car> findAll() {
        return carRepository.findAll();
    }

    public List<Car> findAllWithOffsetAndLimit(int offset, int limit) {
        return carRepository.findAllWithOffsetAndLimit(offset, limit);
    }

    public Car findById(Integer id) {
        return carRepository.findById(id).orElseThrow(()
                -> new CarNotFoundException(String.format("Car %d does not exist", id)));
    }

    public List<Car> findSeveralByCategoryAndCapacity(String category, int capacity) {
        List<Car> cars = carRepository.findSevaralByCategory(category);
        List<Car> carsToReturn = new ArrayList<>();
        if (cars.size() != 0) {
            int totalCapacity = 0;
            for (Car car: cars) {
                totalCapacity += car.getCapacity();
                carsToReturn.add(car);
                if (totalCapacity >= capacity) {
                    return carsToReturn;
                }
            }
            throw new CarNotFoundException("Car not found");
        }
        throw new CarNotFoundException("Car not found");
    }

    public Car findOneByCategoryAndCapacity(String category, int capacity) {
        return carRepository.findOneByCategoryAndCapacity(category, capacity).orElseThrow(()
                -> new CarNotFoundException(String.format("Car not found")));
    }

    public int getTotalNumber() {
        return carRepository.findTotalNumber();
    }
}




