package com.epam.rd.java.basic.taxiservice.service;

import com.epam.rd.java.basic.taxiservice.exception.CarAlreadyExistException;
import com.epam.rd.java.basic.taxiservice.exception.CarNotFoundException;
import com.epam.rd.java.basic.taxiservice.exception.StreetAlreadyExistException;
import com.epam.rd.java.basic.taxiservice.exception.StreetNotFoundException;
import com.epam.rd.java.basic.taxiservice.model.Car.CarModel;
import com.epam.rd.java.basic.taxiservice.model.Street;
import com.epam.rd.java.basic.taxiservice.repository.CarModelRepository;
import com.epam.rd.java.basic.taxiservice.repository.StreetRepository;

import java.util.List;

public class StreetService {
    private final StreetRepository streetRepository;

    public StreetService(StreetRepository streetRepository) {
        this.streetRepository = streetRepository;
    }

    public Street findByTitle(String type, String title) {
        return streetRepository.findByTitle(type, title).orElseThrow(()
                -> new StreetNotFoundException(String.format("Street %s %s does not exist", title, type)));
    }

    public Integer save(Street street) {
        Integer id = null;
        if (streetRepository.findByTitle(street.getStreetType(), street.getTitle()).isEmpty()) {
            id = streetRepository.save(street);
        } else {
            throw new StreetAlreadyExistException("This street already exists");
        }
        return id;
    }

    public void delete(Street street) {
        if (!streetRepository.findById(street.getId()).isEmpty()) {
            streetRepository.delete(street);
        } else {
            throw new StreetNotFoundException("This street doesn't exist");
        }
    }

    public void update(Street street) {
            streetRepository.update(street);
    }

    public List<Street> findAll() {
        return streetRepository.findAll();
    }

    public Street findById(Integer id) {
        return streetRepository.findById(id).orElseThrow(()
                -> new StreetNotFoundException(String.format("Street %d does not exist", id)));
    }
}




