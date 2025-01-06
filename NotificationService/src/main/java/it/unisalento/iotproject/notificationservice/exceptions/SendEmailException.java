package it.unisalento.iotproject.notificationservice.exceptions;

import it.unisalento.iotproject.notificationservice.exceptions.global.CustomErrorException;
import org.springframework.http.HttpStatus;

public class SendEmailException extends CustomErrorException {
    public SendEmailException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
