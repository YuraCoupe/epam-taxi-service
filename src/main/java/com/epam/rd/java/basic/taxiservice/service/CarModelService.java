package com.epam.rd.java.basic.taxiservice.service;

import com.epam.rd.java.basic.taxiservice.exception.CarAlreadyExistException;
import com.epam.rd.java.basic.taxiservice.exception.CarNotFoundException;
import com.epam.rd.java.basic.taxiservice.model.Car.Car;
import com.epam.rd.java.basic.taxiservice.model.Car.CarModel;
import com.epam.rd.java.basic.taxiservice.repository.CarModelRepository;
import com.epam.rd.java.basic.taxiservice.repository.CarRepository;

import java.util.List;

public class CarModelService {
    private final CarModelRepository carModelRepository;

    public CarModelService(CarModelRepository carModelRepository) {
        this.carModelRepository = carModelRepository;
    }

    public CarModel findByModel(String name) {
        return carModelRepository.findByBrand(name).orElseThrow(()
                -> new CarNotFoundException(String.format("Model %s does not exist", name)));
    }

    public Integer save(CarModel carModel) {
        Integer id = null;
        if (carModelRepository.findByModel(carModel.getModel()).isEmpty()) {
            id = carModelRepository.save(carModel);
        } else {
            throw new CarAlreadyExistException("This model already exists");
        }
        return id;
    }

    public void delete(CarModel carModel) {
        if (!carModelRepository.findById(carModel.getId()).isEmpty()) {
            carModelRepository.delete(carModel);
        } else {
            throw new CarNotFoundException("This model doesn't exist");
        }
    }

    public void update(CarModel carModel) {
            carModelRepository.update(carModel);
    }

    public List<CarModel> findAll() {
        return carModelRepository.findAll();
    }

    public CarModel findById(Integer id) {
        return carModelRepository.findById(id).orElseThrow(()
                -> new CarNotFoundException(String.format("Model %d does not exist", id)));
    }
}




