package it.unisalento.iotproject.machineservice.service;

import it.unisalento.iotproject.machineservice.domain.MachineStatus;
import it.unisalento.iotproject.machineservice.domain.ManufacturingMachineType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MachineQueryFilters {
    private String addressCountry;
    private String addressLocality;
    private String addressRegion;
    private String brandName;
    private String manufacturerName;
    private ManufacturingMachineType manufacturingMachineType;
    private String name;
    private MachineStatus status;
}
