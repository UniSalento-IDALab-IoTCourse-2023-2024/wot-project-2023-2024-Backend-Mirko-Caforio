package it.unisalento.iotproject.authservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListCredentialsRestoreDTO {
    private List<CredentialsRestoreDTO> list;
}
