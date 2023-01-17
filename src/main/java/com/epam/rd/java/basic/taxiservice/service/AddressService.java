package com.epam.rd.java.basic.taxiservice.service;

import com.epam.rd.java.basic.taxiservice.exception.AddressAlreadyExistException;
import com.epam.rd.java.basic.taxiservice.exception.AddressNotFoundException;
import com.epam.rd.java.basic.taxiservice.exception.CarAlreadyExistException;
import com.epam.rd.java.basic.taxiservice.exception.CarNotFoundException;
import com.epam.rd.java.basic.taxiservice.model.Address;
import com.epam.rd.java.basic.taxiservice.model.Car.Car;
import com.epam.rd.java.basic.taxiservice.repository.AddressRepository;
import com.epam.rd.java.basic.taxiservice.repository.CarRepository;

import java.util.List;

public class AddressService {
    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Integer save(Address address) {
        Integer id = null;
        if (addressRepository.findByStreetAndBuilding(address.getStreet().getStreetType(), address.getStreet().getTitle(), address.getBuilding()).isEmpty()) {
            id = addressRepository.save(address);
        } else {
            throw new AddressAlreadyExistException("This address already exists");
        }
        return id;
    }

    public void delete(Address address) {
        if (!addressRepository.findById(address.getId()).isEmpty()) {
            addressRepository.delete(address);
        } else {
            throw new AddressNotFoundException("This address doesn't exist");
        }
    }

    public void update(Address address) {
            addressRepository.update(address);
    }

    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    public List<Address> findAllWithOffsetAndLimit(int offset, int limit) {
        return addressRepository.findAllWithOffsetAndLimit(offset, limit);
    }

    public List<Address> findByStreet(String streetType, String street) {
        return addressRepository.findByStreet(streetType, street);
    }

    public List<Address> findByStreetWithOffsetAndLimit(String streetType, String street, int offset, int limit) {
        return addressRepository.findByStreetWithOffsetAndLimit(streetType, street, offset, limit);
    }

    public Address findById(Integer id) {
        return addressRepository.findById(id).orElseThrow(()
                -> new CarNotFoundException(String.format("Address %d does not exist", id)));
    }

    public int getTotalNumber() {
        return addressRepository.findTotalNumber();
    }
}




