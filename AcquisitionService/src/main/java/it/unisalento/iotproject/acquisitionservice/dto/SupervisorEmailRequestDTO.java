package it.unisalento.iotproject.acquisitionservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SupervisorEmailRequestDTO {
    private String id;
    private String responseTopic;
}