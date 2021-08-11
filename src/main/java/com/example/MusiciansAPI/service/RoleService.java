package com.example.MusiciansAPI.service;

import com.example.MusiciansAPI.model.Role;
import com.example.MusiciansAPI.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;


}
