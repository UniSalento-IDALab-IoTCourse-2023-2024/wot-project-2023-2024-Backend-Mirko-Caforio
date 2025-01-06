package it.unisalento.iotproject.machineservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document(collection = "machines")
public class Machine {
    @Id
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
