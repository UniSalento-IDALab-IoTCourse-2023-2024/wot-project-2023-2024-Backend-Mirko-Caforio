package it.unisalento.iotproject.authservice.service;

import it.unisalento.iotproject.authservice.business.exchanger.MosquittoExchanger;
import it.unisalento.iotproject.authservice.dto.UserDetailsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static it.unisalento.iotproject.authservice.security.SecurityConstants.ROLE_ADMIN;

@Service
public class UserCheckService {
    private final MosquittoExchanger mosquittoExchanger;

    @Value("${mqtt.cqrs.auth.request.topic}")
    private String cqrsRequestTopic;

    @Value("${mqtt.auth.cqrs.response.topic}")
    private String authResponseTopic;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCheckService.class);

    @Autowired
    public UserCheckService(MosquittoExchanger mosquittoExchanger) {
        this.mosquittoExchanger = mosquittoExchanger;
    }

    /**
     * Load the user details by email
     * @param email the email of the user
     * @return the user details
     * @throws UsernameNotFoundException if the user is not found
     */
    public UserDetailsDTO loadUserByUsername(String email) throws UsernameNotFoundException, InterruptedException {
        UserDetailsDTO user = null;

        //Chiamata MQTT a CQRS per ottenere i dettagli dell'utente
        try {
            user = mosquittoExchanger.exchangeMessage(email, cqrsRequestTopic, authResponseTopic, 2000);
        } catch (Exception e) {
            LOGGER.error("Error while exchanging message with CQRS: {}", e.getMessage());
        }

        if(user == null) {
            throw new UsernameNotFoundException(email);
        }

        return user;
    }


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
     * Get the email of the current user
     * @return the email of the current user
     */
    public String getCurrentUserEmail(){
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
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
