package it.unisalento.iotproject.machineservice.service;

import it.unisalento.iotproject.machineservice.business.exchanger.MosquittoExchanger;
import it.unisalento.iotproject.machineservice.dto.UserDetailsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static it.unisalento.iotproject.machineservice.security.SecurityConstants.ROLE_ADMIN;

/**
 * The UserCheckService class provides methods for checking user details.
 * It includes methods for loading user details by username, checking user roles, and checking if a user is enabled.
 * It uses a MessageExchanger for exchanging messages and a MessageExchangeStrategy for defining the message exchange strategy.
 */
@Service
public class UserCheckService {
    /**
     * The MessageExchanger used for exchanging messages.
     */
    private final MosquittoExchanger mosquittoExchanger;

    @Value("${mqtt.cqrs.auth.request.topic}")
    private String cqrsAuthRequestTopic;

    @Value("${mqtt.machine.auth.response.topic}")
    private String machineAuthResponseTopic;

    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserCheckService.class);

    @Autowired
    public UserCheckService(MosquittoExchanger mosquittoExchanger) {
        this.mosquittoExchanger = mosquittoExchanger;
    }

    /**
     * Loads user details by username.
     * @param email The email of the user.
     * @return The UserDetailsDTO of the user.
     * @throws UsernameNotFoundException if the user is not found.
     */
    public UserDetailsDTO loadUserByUsername(String email) throws UsernameNotFoundException {
        // MQTT call to CQRS to get user details
        UserDetailsDTO user = null;

        try {
            user = mosquittoExchanger.exchangeMessage(email, cqrsAuthRequestTopic, machineAuthResponseTopic, 2000);
        } catch (Exception e) {
            LOGGER.error("Error while exchanging message with CQRS: {}", e.getMessage());
        }

        if (user != null) {
            LOGGER.info("User {} found with role: {} and enabled {}", user.getEmail(), user.getRole(), user.getEnabled());
        }

        return user;
    }

    /**
     * Checks if a user is enabled.
     * @param enable The enabled status of the user.
     * @return The enabled status of the user.
     */
    public Boolean isEnable(Boolean enable) {
        return enable;
    }

    /**
     * Check if the current user is the user with the given email
     * @param email the email of the user to check
     * @return true if the current user is the user with the given email, false otherwise
     */
    public Boolean isCorrectUser(String email){
        return email.equals(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    /**
     * Check if the current user is an administrator
     * @return true if the current user is an administrator, false otherwise
     */
    public Boolean isAdministrator(){
        String currentRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        return currentRole.equalsIgnoreCase(ROLE_ADMIN);
    }
}