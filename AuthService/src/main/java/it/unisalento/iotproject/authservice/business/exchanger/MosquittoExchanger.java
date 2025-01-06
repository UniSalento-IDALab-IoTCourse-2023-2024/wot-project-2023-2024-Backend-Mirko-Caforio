package it.unisalento.iotproject.authservice.business.exchanger;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.iotproject.authservice.business.publisher.MosquittoPublisher;
import it.unisalento.iotproject.authservice.dto.AuthRequestDTO;
import it.unisalento.iotproject.authservice.dto.UserDetailsDTO;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
@Getter
@Setter
public class MosquittoExchanger {
    private final MosquittoPublisher mosquittoPublisher;
    private final ObjectMapper mapper;
    private final Map<String, CompletableFuture<UserDetailsDTO>> authPendingRequests;

    private static final Logger LOGGER = LoggerFactory.getLogger(MosquittoExchanger.class);

    @Autowired
    public MosquittoExchanger(MosquittoPublisher mosquittoPublisher) {
        this.mosquittoPublisher = mosquittoPublisher;
        this.mapper = new ObjectMapper();
        this.authPendingRequests = new ConcurrentHashMap<>();
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

    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handleMessage(Message<?> message) {
        LOGGER.info("Received message on topic: {}", message.getHeaders().get("mqtt_receivedTopic", String.class));
        LOGGER.info("Message payload: {}", message.getPayload());

        String topic = message.getHeaders().get("mqtt_receivedTopic", String.class);

        switch (topic) {
            case "notification/auth/response":
                try {
                    handleAuthResponse(message);
                } catch (Exception e) {
                    LOGGER.error("Exception occurred while publishing message to topic: {}", topic, e);
                }

                break;
            case null:
                break;
            default:
                LOGGER.info("Received message on unknown topic: {}", topic);
        }
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
}
