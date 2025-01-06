package it.unisalento.iotproject.notificationservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRequestDTO {
    private String id;
    private String email;
    private String responseTopic;
}
