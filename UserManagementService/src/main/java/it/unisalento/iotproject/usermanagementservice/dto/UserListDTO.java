package it.unisalento.iotproject.usermanagementservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserListDTO {
    private List<UserDTO> usersList;

    public UserListDTO() {
        this.usersList = new ArrayList<>();
    }
}
