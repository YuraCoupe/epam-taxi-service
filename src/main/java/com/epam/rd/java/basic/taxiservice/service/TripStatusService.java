package com.epam.rd.java.basic.taxiservice.service;

import com.epam.rd.java.basic.taxiservice.exception.CarAlreadyExistException;
import com.epam.rd.java.basic.taxiservice.exception.CarNotFoundException;
import com.epam.rd.java.basic.taxiservice.exception.TripStatusAlreadyExistException;
import com.epam.rd.java.basic.taxiservice.exception.TripStatusNotFoundException;
import com.epam.rd.java.basic.taxiservice.model.Car.CarStatus;
import com.epam.rd.java.basic.taxiservice.model.Trip;
import com.epam.rd.java.basic.taxiservice.model.TripStatus;
import com.epam.rd.java.basic.taxiservice.repository.CarStatusRepository;
import com.epam.rd.java.basic.taxiservice.repository.TripStatusRepository;

import java.util.List;

public class TripStatusService {
    private final TripStatusRepository tripStatusRepository;

    public TripStatusService(TripStatusRepository tripStatusRepository) {
        this.tripStatusRepository = tripStatusRepository;
    }

    public TripStatus findByTitle(String title) {
        return tripStatusRepository.findByTitle(title).orElseThrow(()
                -> new TripStatusNotFoundException(String.format("Status %s does not exist", title)));
    }

    public Integer save(TripStatus status) {
        Integer id = null;
        if (tripStatusRepository.findByTitle(status.getTitle()).isEmpty()) {
            id = tripStatusRepository.save(status);
        } else {
            throw new TripStatusAlreadyExistException("This status already exists");
        }
        return id;
    }

    public void delete(TripStatus status) {
        if (!tripStatusRepository.findById(status.getId()).isEmpty()) {
            tripStatusRepository.delete(status);
        } else {
            throw new TripStatusNotFoundException("This status doesn't exist");
        }
    }

    public void update(TripStatus status) {
            tripStatusRepository.update(status);
    }

    public List<TripStatus> findAll() {
        return tripStatusRepository.findAll();
    }

    public TripStatus findById(Integer id) {
        return tripStatusRepository.findById(id).orElseThrow(()
                -> new TripStatusNotFoundException(String.format("Status %d does not exist", id)));
    }
}




