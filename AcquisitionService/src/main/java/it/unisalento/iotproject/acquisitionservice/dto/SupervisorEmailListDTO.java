package it.unisalento.iotproject.acquisitionservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SupervisorEmailListDTO {
    private String id;
    private List<String> supervisorEmailList;

    public SupervisorEmailListDTO() {
        this.supervisorEmailList = new ArrayList<>();
    }
}
