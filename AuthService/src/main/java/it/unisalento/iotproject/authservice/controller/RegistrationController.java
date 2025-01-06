package it.unisalento.iotproject.authservice.controller;

import it.unisalento.iotproject.authservice.domain.User;
import it.unisalento.iotproject.authservice.dto.RegistrationDTO;
import it.unisalento.iotproject.authservice.dto.UserDTO;
import it.unisalento.iotproject.authservice.exceptions.IllegalRequestException;
import it.unisalento.iotproject.authservice.exceptions.UserAlreadyExist;
import it.unisalento.iotproject.authservice.repository.UserRepository;
import it.unisalento.iotproject.authservice.service.DataConsistencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static it.unisalento.iotproject.authservice.configuration.SecurityConfig.passwordEncoder;
import static it.unisalento.iotproject.authservice.security.SecurityConstants.*;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {
    private final UserRepository userRepository;

    private final DataConsistencyService dataConsistencyService;

    @Autowired
    public RegistrationController(UserRepository userRepository, DataConsistencyService dataConsistencyService) {
        this.userRepository = userRepository;
        this.dataConsistencyService = dataConsistencyService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO post(@RequestBody RegistrationDTO registrationDTO) {
        User existingUser = userRepository.findByEmail(registrationDTO.getEmail());
        if (existingUser != null) {
            throw new UserAlreadyExist("User already exists: " + registrationDTO.getEmail());
        }

        try{
            if (registrationDTO.getRole() == null) {
                registrationDTO.setRole(ROLE_SUPERVISOR);
            }
        } catch (Exception e){
            throw new IllegalRequestException("Invalid role: " + registrationDTO.getRole());
        }

        User user = new User();
        user.setName(registrationDTO.getName());
        user.setSurname(registrationDTO.getSurname());
        user.setEmail(registrationDTO.getEmail());
        user.setRegistrationDate(LocalDateTime.now());
        user.setPassword(passwordEncoder().encode(registrationDTO.getPassword()));

        switch (registrationDTO.getRole().toUpperCase()) {
            case ROLE_SUPERVISOR -> user.setRole(ROLE_SUPERVISOR);
            case ROLE_ADMIN -> user.setRole(ROLE_ADMIN);
            default -> throw new IllegalRequestException("Invalid role: " + registrationDTO.getRole());
        }

        user = userRepository.save(user);

        registrationDTO.setRegistrationDate(user.getRegistrationDate());
        dataConsistencyService.alertDataConsistency(registrationDTO);

        UserDTO retUser = new UserDTO();
        retUser.setId(user.getId());
        retUser.setName(user.getName());
        retUser.setSurname(user.getSurname());
        retUser.setEmail(user.getEmail());
        retUser.setRole(user.getRole());
        retUser.setRegistrationDate(user.getRegistrationDate());
        
        return retUser;
    }
}
