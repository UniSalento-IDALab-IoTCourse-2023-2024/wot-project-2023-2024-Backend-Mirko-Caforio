package it.unisalento.iotproject.acquisitionservice.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MachineVibrationQueryFilters {
    private String machineName;
    private Boolean prediction;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime from;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime to;
}
