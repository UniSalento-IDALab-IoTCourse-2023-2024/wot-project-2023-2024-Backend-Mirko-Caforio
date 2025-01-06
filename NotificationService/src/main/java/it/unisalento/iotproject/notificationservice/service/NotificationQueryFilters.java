package it.unisalento.iotproject.notificationservice.service;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationQueryFilters {
    private String email;
    private String subject;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime from;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime to;
    private Boolean read;
}
