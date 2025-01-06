package it.unisalento.iotproject.authservice.service;

import it.unisalento.iotproject.authservice.business.publisher.MosquittoPublisher;
import it.unisalento.iotproject.authservice.dto.NotificationMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationMessageHandler {
    private final MosquittoPublisher mosquittoPublisher;

    @Value("${mqtt.notification.request.topic}")
    private String notificationRequestTopic;

    @Autowired
    public NotificationMessageHandler(MosquittoPublisher mosquittoPublisher) {
        this.mosquittoPublisher = mosquittoPublisher;
    }

    public void sendNotificationMessage(NotificationMessageDTO message) {
        mosquittoPublisher.publish(message, notificationRequestTopic);
    }
}
