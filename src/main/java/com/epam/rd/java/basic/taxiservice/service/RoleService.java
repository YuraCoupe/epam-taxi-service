package com.epam.rd.java.basic.taxiservice.service;

import com.epam.rd.java.basic.taxiservice.exception.CarAlreadyExistException;
import com.epam.rd.java.basic.taxiservice.exception.CarNotFoundException;
import com.epam.rd.java.basic.taxiservice.exception.RoleNotFoundException;
import com.epam.rd.java.basic.taxiservice.model.Role;
import com.epam.rd.java.basic.taxiservice.repository.RoleRepository;

import java.util.List;

public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByTitle(String title) {
        return roleRepository.findByTitle(title).orElseThrow(()
                -> new CarNotFoundException(String.format("Role %s does not exist", title)));
    }

    public Integer save(Role role) {
        Integer id = null;
        if (roleRepository.findByTitle(role.getTitle()).isEmpty()) {
            id = roleRepository.save(role);
        } else {
            throw new CarAlreadyExistException("This role already exists");
        }
        return id;
    }

    public void delete(Role role) {
        if (!roleRepository.findById(role.getId()).isEmpty()) {
            roleRepository.delete(role);
        } else {
            throw new RoleNotFoundException("This role doesn't exist");
        }
    }

    public void update(Role role) {
            roleRepository.update(role);
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Role findById(Integer id) {
        return roleRepository.findById(id).orElseThrow(()
                -> new RoleNotFoundException(String.format("Role %d does not exist", id)));
    }

    public Role getClientRole() {
        return roleRepository.getClientRole().orElseThrow(()
                -> new RoleNotFoundException(String.format("Role does not exist")));
    }
}




