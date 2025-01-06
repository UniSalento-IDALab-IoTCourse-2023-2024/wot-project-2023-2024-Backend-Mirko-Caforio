package it.unisalento.iotproject.machineservice.dto;

import it.unisalento.iotproject.machineservice.domain.MachineStatus;
import it.unisalento.iotproject.machineservice.domain.ManufacturingMachineType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MachineDTO {
    private String id;
    private String addressCountry;
    private String addressLocality;
    private String addressRegion;
    private String district;
    private String streetAddress;
    private String streetNumber;
    private String alternateName;
    private String brandName;
    private String description;
    private String manufacturerName;
    private ManufacturingMachineType manufacturingMachineType;
    private String name;
    private String processDescription;
    private List<String> standardOperations;
    private String type;
    private String version;
    private MachineStatus status;
}
