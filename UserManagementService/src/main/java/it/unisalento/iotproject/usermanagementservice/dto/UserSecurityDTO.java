package it.unisalento.iotproject.usermanagementservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSecurityDTO {
    private String id;
    private String email;
    private String role;
    private Boolean enabled;
}
