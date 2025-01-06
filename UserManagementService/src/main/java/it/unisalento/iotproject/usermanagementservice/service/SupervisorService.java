package it.unisalento.iotproject.usermanagementservice.service;

import it.unisalento.iotproject.usermanagementservice.business.publisher.MosquittoPublisher;
import it.unisalento.iotproject.usermanagementservice.domain.User;
import it.unisalento.iotproject.usermanagementservice.dto.SupervisorEmailListDTO;
import it.unisalento.iotproject.usermanagementservice.dto.SupervisorEmailRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.unisalento.iotproject.usermanagementservice.security.SecurityConstants.ROLE_SUPERVISOR;

@Service
public class SupervisorService {
    private final MosquittoPublisher mosquittoPublisher;
    private final UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SupervisorService.class);

    @Autowired
    public SupervisorService(MosquittoPublisher mosquittoPublisher, UserService userService) {
        this.mosquittoPublisher = mosquittoPublisher;
        this.userService = userService;
    }

    public void handleSupervisorRequest(SupervisorEmailRequestDTO supervisorEmailRequestDTO) {
        try {
            List<User> users = userService.findUsers(
                    null,
                    null,
                    null,
                    ROLE_SUPERVISOR,
                    true
            );

            if (users == null) {
                return;
            }

            LOGGER.info("Supervisors email found");

            SupervisorEmailListDTO supervisorEmailListDTO = new SupervisorEmailListDTO();
            supervisorEmailListDTO.setId(supervisorEmailRequestDTO.getId());
            supervisorEmailListDTO.setSupervisorEmailList(users.stream().map(User::getEmail).toList());

            LOGGER.info("Supervisor {} processed", supervisorEmailListDTO);

            mosquittoPublisher.publish(supervisorEmailListDTO, supervisorEmailRequestDTO.getResponseTopic());
        } catch (Exception e) {
            LOGGER.info("Error while processing supervisor email request: {}", e.getMessage());
        }
    }
}
