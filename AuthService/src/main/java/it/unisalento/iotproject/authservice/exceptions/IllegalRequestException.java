package it.unisalento.iotproject.authservice.exceptions;

import it.unisalento.iotproject.authservice.exceptions.global.CustomErrorException;
import org.springframework.http.HttpStatus;

public class IllegalRequestException extends CustomErrorException {
    public IllegalRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
