package it.unisalento.iotproject.acquisitionservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "machine_vibration")
public class MachineVibration {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String machine_name;
    private float mean_x;
    private float mean_y;
    private float mean_z;
    private float rms_x;
    private float rms_y;
    private float rms_z;
    private float min_x;
    private float min_y;
    private float min_z;
    private float max_x;
    private float max_y;
    private float max_z;
    private float std_x;
    private float std_y;
    private float std_z;
    private float kurtosis_x;
    private float kurtosis_y;
    private float kurtosis_z;
    private float skewness_x;
    private float skewness_y;
    private float skewness_z;
    private float peak_to_peak_x;
    private float peak_to_peak_y;
    private float peak_to_peak_z;
    private float energy_x;
    private float energy_y;
    private float energy_z;
    private boolean prediction;
    private LocalDateTime timestamp;
}
