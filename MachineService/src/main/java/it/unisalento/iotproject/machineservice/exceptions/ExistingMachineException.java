package it.unisalento.iotproject.machineservice.exceptions;

import it.unisalento.iotproject.machineservice.exceptions.global.CustomErrorException;
import org.springframework.http.HttpStatus;

public class ExistingMachineException extends CustomErrorException {
    public ExistingMachineException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
