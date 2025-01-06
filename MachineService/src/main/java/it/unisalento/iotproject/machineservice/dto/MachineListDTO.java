package it.unisalento.iotproject.machineservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MachineListDTO {
    private List<MachineDTO> machinesList;

    public MachineListDTO() {
        this.machinesList = new ArrayList<>();
    }
}
