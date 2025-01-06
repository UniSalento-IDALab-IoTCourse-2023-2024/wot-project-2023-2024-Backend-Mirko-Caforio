package it.unisalento.iotproject.notificationservice.exceptions;

import it.unisalento.iotproject.notificationservice.exceptions.global.CustomErrorException;
import org.springframework.http.HttpStatus;

public class NotificationNotFoundException extends CustomErrorException {

    public NotificationNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
