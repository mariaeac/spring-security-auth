package com.meac.springsecurity.services;

import com.meac.springsecurity.controller.dto.CreateUserDTO;
import com.meac.springsecurity.entities.Role;
import com.meac.springsecurity.entities.User;
import com.meac.springsecurity.repositories.RoleRepository;
import com.meac.springsecurity.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;


@Service
public class UserServices {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServices(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(CreateUserDTO userDTO) {

        var basicRole = roleRepository.findByName(Role.Values.BASIC.name());

        // Verifica se o username já existe
        var user = (userRepository.findByUsername(userDTO.username()));
        if (user.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        User newUser = new User();
        newUser.setUsername(userDTO.username());
        newUser.setPassword(passwordEncoder.encode(userDTO.password()));
        newUser.setRoles(Set.of(basicRole));
         userRepository.save(newUser);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }



}
