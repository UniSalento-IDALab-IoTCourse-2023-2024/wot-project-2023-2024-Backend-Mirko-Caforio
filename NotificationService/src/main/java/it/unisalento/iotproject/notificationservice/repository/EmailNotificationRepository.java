package it.unisalento.iotproject.notificationservice.repository;

import it.unisalento.iotproject.notificationservice.domain.EmailNotification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailNotificationRepository extends MongoRepository<EmailNotification, String> {
}
