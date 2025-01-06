package it.unisalento.iotproject.usermanagementservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.iotproject.usermanagementservice.business.publisher.MosquittoPublisher;
import it.unisalento.iotproject.usermanagementservice.domain.User;
import it.unisalento.iotproject.usermanagementservice.dto.AuthRequestDTO;
import it.unisalento.iotproject.usermanagementservice.dto.SupervisorEmailRequestDTO;
import it.unisalento.iotproject.usermanagementservice.dto.UserDTO;
import it.unisalento.iotproject.usermanagementservice.dto.UserSecurityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Service
public class UserManagementMessageHandler {
    private final UserDataConsistencyService userDataConsistencyService;
    private final SupervisorService supervisorService;
    private final MosquittoPublisher mosquittoPublisher;
    private final ObjectMapper mapper;
    private final UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserManagementMessageHandler.class);

    @Autowired
    public UserManagementMessageHandler(UserDataConsistencyService userDataConsistencyService, SupervisorService supervisorService, UserService userService, MosquittoPublisher mosquittoPublisher) {
        this.userDataConsistencyService = userDataConsistencyService;
        this.supervisorService = supervisorService;
        this.mosquittoPublisher = mosquittoPublisher;
        this.userService = userService;
        this.mapper = new ObjectMapper();
    }

    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handleMessage(Message<?> message) throws IOException {
        String topic = message.getHeaders().get("mqtt_receivedTopic", String.class);

        switch (topic) {
            case "cqrs/auth/request":
                try {
                    handleAuthRequest(message);
                } catch (Exception e) {
                    LOGGER.error("Exception occurred while publishing message to topic: {}", topic, e);
                }

                break;
            case "cqrs/data/consistency":
                UserDTO userDTO = new UserDTO();

                try {
                    userDTO = mapper.readValue(message.getPayload().toString(), UserDTO.class);
                } catch (Exception e) {
                    LOGGER.error("Exception occurred while publishing message to topic: {}", topic, e);
                }

                userDataConsistencyService.handleUserDataConsistency(userDTO);
                break;
            case "cqrs/supervisor/email/request":
                SupervisorEmailRequestDTO supervisorRequestDTO = new SupervisorEmailRequestDTO();

                try {
                    supervisorRequestDTO = mapper.readValue(message.getPayload().toString(), SupervisorEmailRequestDTO.class);
                } catch (Exception e) {
                    LOGGER.error("Exception occurred while publishing message to topic: {}", topic, e);
                }

                supervisorService.handleSupervisorRequest(supervisorRequestDTO);

                break;
            case null:
                break;
            default:
                LOGGER.info("Received message on unknown topic: {}", topic);
        }
    }

    private void handleAuthRequest(Message<?> message) throws Exception {
        AuthRequestDTO authRequestDTO = mapper.readValue(message.getPayload().toString(), AuthRequestDTO.class);

        try {
            User user = userService.getUserByEmail(authRequestDTO.getEmail());

            if (user == null) {
                return;
            }

            LOGGER.info("User {} found", authRequestDTO.getEmail());

            UserSecurityDTO userSecurityDTO = new UserSecurityDTO();
            userSecurityDTO.setId(authRequestDTO.getId());
            userSecurityDTO.setEmail(user.getEmail());
            userSecurityDTO.setRole(user.getRole());
            userSecurityDTO.setEnabled(user.getEnabled());

            LOGGER.info("User {} processed", userSecurityDTO);

            mosquittoPublisher.publish(userSecurityDTO, authRequestDTO.getResponseTopic());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
