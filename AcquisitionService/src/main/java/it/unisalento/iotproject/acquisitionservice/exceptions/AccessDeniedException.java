package it.unisalento.iotproject.acquisitionservice.exceptions;

import it.unisalento.iotproject.acquisitionservice.exceptions.global.CustomErrorException;
import org.springframework.http.HttpStatus;

public class AccessDeniedException extends CustomErrorException {
    public AccessDeniedException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
