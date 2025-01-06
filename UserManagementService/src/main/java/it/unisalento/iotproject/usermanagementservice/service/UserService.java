package it.unisalento.iotproject.usermanagementservice.service;

import it.unisalento.iotproject.usermanagementservice.domain.User;
import it.unisalento.iotproject.usermanagementservice.dto.UserDTO;
import it.unisalento.iotproject.usermanagementservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final MongoTemplate mongoTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, MongoTemplate mongoTemplate) {
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Get all users.
     * @return List of all users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get a user by email.
     * @param email The email of the user.
     * @return User object containing the user details.
     */
    public User getUserByEmail(String email) {
        //Ricerca utenti abilitati
        return userRepository.findByEmailAndEnabled(email,true);
    }

    /**
     * Create a new user.
     * @param user The User object to be created.
     * @return The created User object.
     */
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Find users based on provided filters.
     * @param email The email of the user.
     * @param name The name of the user.
     * @param surname The surname of the user.
     * @param role The role of the user.
     * @param enabled The enabled status of the user.
     * @return List of users that match the filters.
     */
    public List<User> findUsers(String email, String name, String surname, String role, Boolean enabled) {
        Query query = new Query();

        if (email != null) {
            query.addCriteria(Criteria.where("email").is(email));
        }
        if (name != null) {
            query.addCriteria(Criteria.where("name").is(name));
        }
        if (surname != null) {
            query.addCriteria(Criteria.where("surname").is(surname));
        }
        if (role != null) {
            query.addCriteria(Criteria.where("role").is(role));
        }

        query.addCriteria(Criteria.where("enabled").is(Objects.requireNonNullElse(enabled, true)));

        return mongoTemplate.find(query, User.class);
    }

    /**
     * Convert a User domain object to a UserDTO.
     * @param user The User object to be converted.
     * @return The converted UserDTO.
     */
    public UserDTO domainToDto(User user) {
        UserDTO userDTO = new UserDTO();

        Optional.ofNullable(user.getEmail()).ifPresent(userDTO::setEmail);
        Optional.ofNullable(user.getName()).ifPresent(userDTO::setName);
        Optional.ofNullable(user.getSurname()).ifPresent(userDTO::setSurname);
        Optional.ofNullable(user.getRegistrationDate()).ifPresent(userDTO::setRegistrationDate);
        Optional.ofNullable(user.getRole()).ifPresent(userDTO::setRole);

        return userDTO;
    }
}
