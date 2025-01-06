package it.unisalento.iotproject.usermanagementservice.service;

import it.unisalento.iotproject.usermanagementservice.domain.User;
import it.unisalento.iotproject.usermanagementservice.dto.UserDTO;
import it.unisalento.iotproject.usermanagementservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDataConsistencyService {
    private final UserService userService;
    private final UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDataConsistencyService.class);

    @Autowired
    public UserDataConsistencyService(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public void handleUserDataConsistency(UserDTO userDTO) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(userDTO.getEmail()));

        if (optionalUser.isPresent()) {
            LOGGER.error("User already exists: {}", userDTO.getEmail());
        } else {
            User user = new User();
            user.setEmail(userDTO.getEmail());
            user.setName(userDTO.getName());
            user.setSurname(userDTO.getSurname());
            user.setRole(userDTO.getRole());
            user.setEnabled(true);
            user.setRegistrationDate(userDTO.getRegistrationDate());

            LOGGER.info("User domain: {}", user);

            userService.createUser(user);
        }
    }
}