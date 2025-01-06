package it.unisalento.iotproject.notificationservice.service;

import it.unisalento.iotproject.notificationservice.domain.Notification;

public interface NotificationSender {
    void sendNotification(Notification notification);
}
