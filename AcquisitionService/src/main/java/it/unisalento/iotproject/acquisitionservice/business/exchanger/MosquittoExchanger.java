package it.unisalento.iotproject.acquisitionservice.business.exchanger;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.iotproject.acquisitionservice.business.publisher.MosquittoPublisher;
import it.unisalento.iotproject.acquisitionservice.dto.*;
import it.unisalento.iotproject.acquisitionservice.service.NotificationMessageService;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static it.unisalento.iotproject.acquisitionservice.service.NotificationConstants.WARNING_NOTIFICATION_TYPE;

@Component
@Getter
@Setter
public class MosquittoExchanger {
    private final NotificationMessageService notificationMessageService;
    private final MosquittoPublisher mosquittoPublisher;
    private final ObjectMapper mapper;
    private final Map<String, CompletableFuture<UserDetailsDTO>> authPendingRequests;
    private final Map<String, CompletableFuture<SupervisorEmailListDTO>> supervisorEmailPendingRequests;

    private static final Logger LOGGER = LoggerFactory.getLogger(MosquittoExchanger.class);

    @Autowired
    public MosquittoExchanger(NotificationMessageService notificationMessageService, MosquittoPublisher mosquittoPublisher) {
        this.notificationMessageService = notificationMessageService;
        this.mosquittoPublisher = mosquittoPublisher;
        this.mapper = new ObjectMapper();
        this.authPendingRequests = new ConcurrentHashMap<>();
        this.supervisorEmailPendingRequests = new ConcurrentHashMap<>();
    }

    private <T> T awaitResponse(String requestId, CompletableFuture<T> future, Map<String, CompletableFuture<T>> pendingRequests, long timeoutMillis) {
        try {
            return future.get(timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            pendingRequests.remove(requestId);
            LOGGER.error("Timeout or exception while waiting for response: {}", e.getMessage());
            return null;
        }
    }

    public UserDetailsDTO exchangeMessage(String email, String requestTopic, String responseTopic, long timeoutMillis) {
        String requestId = UUID.randomUUID().toString();
        CompletableFuture<UserDetailsDTO> future = new CompletableFuture<>();
        authPendingRequests.put(requestId, future);

        AuthRequestDTO authRequestDTO = AuthRequestDTO.builder()
                .id(requestId)
                .email(email)
                .responseTopic(responseTopic)
                .build();

        mosquittoPublisher.publish(authRequestDTO, requestTopic);

        return awaitResponse(requestId, future, authPendingRequests, timeoutMillis);
    }

    public SupervisorEmailListDTO exchangeMessage(String requestTopic, String responseTopic, long timeoutMillis) {
        String requestId = UUID.randomUUID().toString();
        CompletableFuture<SupervisorEmailListDTO> future = new CompletableFuture<>();
        supervisorEmailPendingRequests.put(requestId, future);

        SupervisorEmailRequestDTO supervisorEmailRequestDTO = SupervisorEmailRequestDTO.builder()
                .id(requestId)
                .responseTopic(responseTopic)
                .build();

        mosquittoPublisher.publish(supervisorEmailRequestDTO, requestTopic);

        return awaitResponse(requestId, future, supervisorEmailPendingRequests, timeoutMillis);
    }

    public void handleAuthResponse(Message<?> message) throws Exception {
        UserDetailsDTO userDetailsDTO = mapper.readValue(message.getPayload().toString(), UserDetailsDTO.class);
        String requestId = userDetailsDTO.getId();
        CompletableFuture<UserDetailsDTO> future = authPendingRequests.remove(requestId);

        if (future != null) {
            future.complete(userDetailsDTO);
        } else {
            LOGGER.error("Received response for unknown or expired request ID: {}", requestId);
        }
    }

    public void handleSupervisorEmailResponse(Message<?> message) throws Exception {
        SupervisorEmailListDTO supervisorEmailListDTO = mapper.readValue(message.getPayload().toString(), SupervisorEmailListDTO.class);
        String requestId = supervisorEmailListDTO.getId();
        CompletableFuture<SupervisorEmailListDTO> future = supervisorEmailPendingRequests.remove(requestId);

        if (future != null) {
            future.complete(supervisorEmailListDTO);
        } else {
            LOGGER.error("Received response for unknown or expired request ID: {}", requestId);
        }
    }
}
