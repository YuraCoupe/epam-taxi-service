package com.epam.rd.java.basic.taxiservice.service;

import com.epam.rd.java.basic.taxiservice.exception.UserAlreadyExistException;
import com.epam.rd.java.basic.taxiservice.exception.UserNotFoundException;
import com.epam.rd.java.basic.taxiservice.model.User;
import com.epam.rd.java.basic.taxiservice.repository.UserRepository;

import java.util.List;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).orElseThrow(()
                -> new UserNotFoundException(String.format("User with phone number %s does not exist", phoneNumber)));
    }

    public Integer save(User user) {
        Integer id;
        if (userRepository.findByPhoneNumber(user.getPhoneNumber()).isEmpty()) {
            id = userRepository.save(user);
        } else {
            throw new UserAlreadyExistException("This User already exists");
        }
        return id;
    }

    public void delete(User user) {
        if (userRepository.findById(user.getId()).isPresent()) {
            userRepository.delete(user);
        } else {
            throw new UserNotFoundException("This User doesn't exist");
        }
    }

    public void update(User user) {
            userRepository.update(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findAllWithOffsetAndLimit(int offset, int limit) {
        return userRepository.findAllWithOffsetAndLimit(offset, limit);
    }

    public User findById(Integer id) {
        return userRepository.findById(id).orElseThrow(()
                -> new UserNotFoundException(String.format("User with ID %d does not exist", id)));
    }

    public int getTotalNumber() {
        return userRepository.findTotalNumber();
    }

    public List<User> findFreeDrivers() {
        return userRepository.findFreeDrivers();
    }

    public List<User> findAllClients() {
        return userRepository.findAllClients();
    }

}




