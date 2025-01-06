package it.unisalento.iotproject.authservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsDTO {
    private String id;
    private String email;
    private String role;
    private Boolean enabled;
}
