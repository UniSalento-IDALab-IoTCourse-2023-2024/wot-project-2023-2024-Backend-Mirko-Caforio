package it.unisalento.iotproject.usermanagementservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupervisorEmailRequestDTO {
    private String id;
    private String responseTopic;
}