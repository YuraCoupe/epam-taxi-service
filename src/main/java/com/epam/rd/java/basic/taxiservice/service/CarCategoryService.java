package com.epam.rd.java.basic.taxiservice.service;

import com.epam.rd.java.basic.taxiservice.exception.CarAlreadyExistException;
import com.epam.rd.java.basic.taxiservice.exception.CarNotFoundException;
import com.epam.rd.java.basic.taxiservice.model.Car.CarCategory;
import com.epam.rd.java.basic.taxiservice.model.Car.CarModel;
import com.epam.rd.java.basic.taxiservice.repository.CarCategoryRepository;
import com.epam.rd.java.basic.taxiservice.repository.CarModelRepository;

import java.util.List;

public class CarCategoryService {
    private final CarCategoryRepository carCategoryRepository;

    public CarCategoryService(CarCategoryRepository carCategoryRepository) {
        this.carCategoryRepository = carCategoryRepository;
    }

    public CarCategory findByTitle(String title) {
        return carCategoryRepository.findByTitle(title).orElseThrow(()
                -> new CarNotFoundException(String.format("Category %s does not exist", title)));
    }

    public Integer save(CarCategory category) {
        Integer id = null;
        if (carCategoryRepository.findByTitle(category.getTitle()).isEmpty()) {
            id = carCategoryRepository.save(category);
        } else {
            throw new CarAlreadyExistException("This category already exists");
        }
        return id;
    }

    public void delete(CarCategory category) {
        if (!carCategoryRepository.findById(category.getId()).isEmpty()) {
            carCategoryRepository.delete(category);
        } else {
            throw new CarNotFoundException("This category doesn't exist");
        }
    }

    public void update(CarCategory category) {
            carCategoryRepository.update(category);
    }

    public List<CarCategory> findAll() {
        return carCategoryRepository.findAll();
    }

    public CarCategory findById(Integer id) {
        return carCategoryRepository.findById(id).orElseThrow(()
                -> new CarNotFoundException(String.format("Category %d does not exist", id)));
    }
}




