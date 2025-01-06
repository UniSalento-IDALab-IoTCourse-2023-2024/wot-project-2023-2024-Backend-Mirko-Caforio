package it.unisalento.iotproject.authservice.exceptions;

import it.unisalento.iotproject.authservice.exceptions.global.CustomErrorException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExist extends CustomErrorException {
    public UserAlreadyExist(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
