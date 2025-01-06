package it.unisalento.iotproject.notificationservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.iotproject.notificationservice.business.exchanger.MosquittoExchanger;
import it.unisalento.iotproject.notificationservice.domain.EmailNotification;
import it.unisalento.iotproject.notificationservice.dto.NotificationMessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class NotificationMessageHandler {
    private final MosquittoExchanger mosquittoExchanger;
    private final ObjectMapper mapper;
    private final NotificationService notificationService;
    private final EmailNotificationSender emailNotificationSender;

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationMessageHandler.class);

    @Autowired
    public NotificationMessageHandler(MosquittoExchanger mosquittoExchanger, NotificationService notificationService, EmailNotificationSender emailNotificationSender) {
        this.mosquittoExchanger = mosquittoExchanger;
        this.notificationService = notificationService;
        this.emailNotificationSender = emailNotificationSender;
        this.mapper = new ObjectMapper();
    }

    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handleMessage(Message<?> message) {
        LOGGER.info("Received message on topic: {}", message.getHeaders().get("mqtt_receivedTopic", String.class));
        LOGGER.info("Message payload: {}", message.getPayload());

        String topic = message.getHeaders().get("mqtt_receivedTopic", String.class);

        switch (topic) {
            case "notification/request":
                try {
                    handleNotificationRequest(message);
                } catch (Exception e) {
                    LOGGER.error("Exception occurred while publishing message to topic: {}", topic, e);
                }

                break;
            case "notification/auth/response":
                try {
                    mosquittoExchanger.handleAuthResponse(message);
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

    public void handleNotificationRequest(Message<?> message) throws Exception {
        NotificationMessageDTO notificationDTO = mapper.readValue(message.getPayload().toString(), NotificationMessageDTO.class);

        try {
            if(notificationDTO.isNotification()) {
                notificationService.getPopupNotification(notificationDTO);
                LOGGER.info("Popup notification message saved");
            }

            if(notificationDTO.isEmail()) {
                EmailNotification emailNotification = notificationService.getEmailNotification(notificationDTO);
                emailNotificationSender.sendNotification(emailNotification);
                LOGGER.info("Email notification message saved");
            }

            if (!notificationDTO.isNotification() && !notificationDTO.isEmail()) {
                notificationService.getPopupNotification(notificationDTO);
            }
        } catch (Exception e) {
            LOGGER.error("Error while processing notification message: {}", e.getMessage());
        }
    }
}
