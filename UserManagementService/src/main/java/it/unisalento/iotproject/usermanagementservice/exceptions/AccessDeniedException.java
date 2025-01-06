package it.unisalento.iotproject.usermanagementservice.exceptions;

import it.unisalento.iotproject.usermanagementservice.exceptions.global.CustomErrorException;
import org.springframework.http.HttpStatus;

public class AccessDeniedException extends CustomErrorException {
    public AccessDeniedException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
