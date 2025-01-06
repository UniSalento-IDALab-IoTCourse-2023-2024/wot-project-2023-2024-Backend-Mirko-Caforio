package it.unisalento.iotproject.authservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CredentialsRestoreDTO {
    private String email;
    private String token;
    private LocalDateTime requestDate;
    private LocalDateTime expirationDate;
}
