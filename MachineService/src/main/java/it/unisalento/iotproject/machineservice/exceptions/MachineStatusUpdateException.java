package it.unisalento.iotproject.machineservice.exceptions;

import it.unisalento.iotproject.machineservice.exceptions.global.CustomErrorException;
import org.springframework.http.HttpStatus;

public class MachineStatusUpdateException extends CustomErrorException {
    public MachineStatusUpdateException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
