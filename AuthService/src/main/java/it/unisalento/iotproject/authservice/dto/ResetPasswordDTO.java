package it.unisalento.iotproject.authservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDTO {
    private String newPassword;
    private String token;
}
