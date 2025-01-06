package it.unisalento.iotproject.acquisitionservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MachineVibrationListDTO {
    private List<MachineVibrationDTO> machineVibrationList;

    public MachineVibrationListDTO() {
        this.machineVibrationList = new ArrayList<>();
    }
}
