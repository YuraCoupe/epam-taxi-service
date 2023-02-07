package com.epam.rd.java.basic.taxiservice.service;

import com.epam.rd.java.basic.taxiservice.exception.CarAlreadyExistException;
import com.epam.rd.java.basic.taxiservice.exception.CarNotFoundException;
import com.epam.rd.java.basic.taxiservice.exception.TripAlreadyExistException;
import com.epam.rd.java.basic.taxiservice.exception.TripNotFoundException;
import com.epam.rd.java.basic.taxiservice.model.Car.Car;
import com.epam.rd.java.basic.taxiservice.model.Trip;
import com.epam.rd.java.basic.taxiservice.repository.CarRepository;
import com.epam.rd.java.basic.taxiservice.repository.TripRepository;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TripService {
    private final TripRepository tripRepository;
    private final CarRepository carRepository;

    public TripService(TripRepository tripRepository, CarRepository carRepository) {
        this.tripRepository = tripRepository;
        this.carRepository = carRepository;
    }

    public Integer save(Trip trip) {
        return tripRepository.save(trip);
    }

    public void delete(Trip trip) {
        if (tripRepository.findById(trip.getId()).isPresent()) {
            tripRepository.delete(trip);
        } else {
            throw new TripNotFoundException("This trip doesn't exist");
        }
    }

    public void update(Trip trip) {
        tripRepository.update(trip);
    }

    public List<Trip> findAll() {
        List<Trip> trips = tripRepository.findAll();
        trips.forEach(x -> x.setCars(new HashSet<>(findCarsByTripId(x.getId()))));
        return trips;
    }

    public List<Trip> findAllWithOffsetAndLimit(String fieldToSort, String sortOrder, Timestamp timeFrom, Timestamp timeTo, int offset, int limit) {
        List<Trip> trips = tripRepository.findAllWithOffsetAndLimit(fieldToSort, sortOrder, timeFrom, timeTo, offset, limit);
        trips.forEach(x -> x.setCars(new HashSet<>(findCarsByTripId(x.getId()))));
        return trips;
    }

    public Trip findById(Integer id) {
        Trip trip = tripRepository.findById(id).orElseThrow(()
                -> new TripNotFoundException(String.format("Trip %d does not exist", id)));
        trip.setCars(new HashSet<>(findCarsByTripId(trip.getId())));
        return trip;
    }

    public Optional<Trip> findActiveByClientId(Integer clientId) {
        return tripRepository.findActiveTripByClientId(clientId);
    }

    public Optional<Trip> findActiveByDriverId(Integer driverId) {
        return tripRepository.findActiveTripByDriverId(driverId);
    }

    public List<Trip> findByUserIdWithOffsetAndLimit(Integer userId, String fieldToSort, String sortOrder,
                                                     Timestamp timeFrom, Timestamp timeTo, int offset, int limit) {
        List<Trip> trips = tripRepository.
                findByUserIdWithOffsetAndLimit(userId, fieldToSort, sortOrder, timeFrom, timeTo, offset, limit);
        trips.forEach(x -> x.setCars(new HashSet<>(findCarsByTripId(x.getId()))));
        return trips;
    }

    public List<Trip> findByDriverIdWithOffsetAndLimit(Integer driverId, String fieldToSort, String sortOrder, int offset, int limit) {
        List<Trip> trips = tripRepository.findByDriverIdWithOffsetAndLimit(driverId, fieldToSort, sortOrder, offset, limit);
        trips.forEach(x -> x.setCars(new HashSet<>(findCarsByTripId(x.getId()))));
        return trips;
    }

    private List<Car> findCarsByTripId(Integer tripId) {
        return carRepository.findByTripId(tripId);
    }

    public int getTotalNumber(Timestamp timeFrom, Timestamp timeTo) {
        return tripRepository.findTotalNumber(timeFrom, timeTo);
    }

    public int getTotalNumberByUserId(Integer userId, Timestamp timeFrom, Timestamp timeTo) {
        return tripRepository.findTotalNumberByUser(userId, timeFrom, timeTo);
    }

    public int getTotalNumberByDriverId(Integer driverId) {
        return tripRepository.findTotalNumberByDriver(driverId);
    }

}




