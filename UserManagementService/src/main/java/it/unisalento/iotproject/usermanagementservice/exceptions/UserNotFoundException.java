package it.unisalento.iotproject.usermanagementservice.exceptions;

import it.unisalento.iotproject.usermanagementservice.exceptions.global.CustomErrorException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends CustomErrorException {
    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
