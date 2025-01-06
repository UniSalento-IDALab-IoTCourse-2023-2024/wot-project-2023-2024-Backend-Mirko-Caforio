package it.unisalento.iotproject.acquisitionservice.service;

import it.unisalento.iotproject.acquisitionservice.business.publisher.MosquittoPublisher;
import it.unisalento.iotproject.acquisitionservice.dto.NotificationMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationMessageService {
    private final MosquittoPublisher mosquittoPublisher;

    @Value("${mqtt.notification.request.topic}")
    private String notificationRequestTopic;

    @Autowired
    public NotificationMessageService(MosquittoPublisher mosquittoPublisher) {
        this.mosquittoPublisher = mosquittoPublisher;
    }

    public void sendNotificationMessage(NotificationMessageDTO message) {
        mosquittoPublisher.publish(message, notificationRequestTopic);
    }
}
