package com.epam.rd.java.basic.taxiservice.service;

import com.epam.rd.java.basic.taxiservice.exception.CarAlreadyExistException;
import com.epam.rd.java.basic.taxiservice.exception.CarNotFoundException;
import com.epam.rd.java.basic.taxiservice.exception.CarStatusAlreadyExistException;
import com.epam.rd.java.basic.taxiservice.exception.CarStatusNotFoundException;
import com.epam.rd.java.basic.taxiservice.model.Car.CarCategory;
import com.epam.rd.java.basic.taxiservice.model.Car.CarStatus;
import com.epam.rd.java.basic.taxiservice.repository.CarCategoryRepository;
import com.epam.rd.java.basic.taxiservice.repository.CarStatusRepository;

import java.util.List;

public class CarStatusService {
    private final CarStatusRepository carStatusRepository;

    public CarStatusService(CarStatusRepository carStatusRepository) {
        this.carStatusRepository = carStatusRepository;
    }

    public CarStatus findByTitle(String title) {
        return carStatusRepository.findByTitle(title).orElseThrow(()
                -> new CarStatusNotFoundException(String.format("Status %s does not exist", title)));
    }

    public Integer save(CarStatus status) {
        Integer id = null;
        if (carStatusRepository.findByTitle(status.getTitle()).isEmpty()) {
            id = carStatusRepository.save(status);
        } else {
            throw new CarStatusAlreadyExistException("This status already exists");
        }
        return id;
    }

    public void delete(CarStatus status) {
        if (!carStatusRepository.findById(status.getId()).isEmpty()) {
            carStatusRepository.delete(status);
        } else {
            throw new CarStatusNotFoundException("This status doesn't exist");
        }
    }

    public void update(CarStatus status) {
            carStatusRepository.update(status);
    }

    public List<CarStatus> findAll() {
        return carStatusRepository.findAll();
    }

    public CarStatus findById(Integer id) {
        return carStatusRepository.findById(id).orElseThrow(()
                -> new CarStatusNotFoundException(String.format("Status %d does not exist", id)));
    }
}




