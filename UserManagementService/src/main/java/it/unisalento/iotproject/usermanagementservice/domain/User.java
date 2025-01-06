package it.unisalento.iotproject.usermanagementservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "user")
public class User {
    @Id
    private String id;
    private String email;
    private String name;
    private String surname;
    private String role;
    private LocalDateTime registrationDate;
    private Boolean enabled;
}
