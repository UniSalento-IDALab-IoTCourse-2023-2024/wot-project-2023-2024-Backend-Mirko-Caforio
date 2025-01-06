package it.unisalento.iotproject.acquisitionservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthRequestDTO {
    private String id;
    private String email;
    private String responseTopic;
}
