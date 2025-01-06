package it.unisalento.iotproject.authservice.exceptions;

import it.unisalento.iotproject.authservice.exceptions.global.CustomErrorException;
import org.springframework.http.HttpStatus;

public class TokenException extends CustomErrorException {
    public TokenException(String message) {
        super(message, HttpStatus.EXPECTATION_FAILED);
    }
}
