package it.unisalento.iotproject.authservice.business;

import it.unisalento.iotproject.authservice.security.JwtUtilities;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static it.unisalento.iotproject.authservice.security.SecurityConstants.ROLE_ADMIN;

@Component
public class AdminGenerator {

    private final String adminToken;
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminGenerator.class);

    public AdminGenerator(JwtUtilities jwtUtilities) {
        this.adminToken = jwtUtilities.generateToken("DEV_ADMIN", ROLE_ADMIN);
    }

    public String getToken() {
        return adminToken;
    }

    @PostConstruct
    public void init() {
        LOGGER.info("Admin token: {}", adminToken);
    }
}
