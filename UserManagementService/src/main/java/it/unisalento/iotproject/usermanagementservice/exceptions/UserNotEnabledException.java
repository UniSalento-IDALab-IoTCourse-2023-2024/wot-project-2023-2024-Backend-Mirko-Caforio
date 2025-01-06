package it.unisalento.iotproject.usermanagementservice.exceptions;

import it.unisalento.iotproject.usermanagementservice.exceptions.global.CustomErrorException;
import org.springframework.http.HttpStatus;

public class UserNotEnabledException extends CustomErrorException {
    public UserNotEnabledException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
