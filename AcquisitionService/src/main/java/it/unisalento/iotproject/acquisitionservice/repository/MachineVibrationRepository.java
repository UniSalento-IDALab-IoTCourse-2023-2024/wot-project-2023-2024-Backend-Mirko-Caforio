package it.unisalento.iotproject.acquisitionservice.repository;

import it.unisalento.iotproject.acquisitionservice.domain.MachineVibration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MachineVibrationRepository extends JpaRepository<MachineVibration, String> {
}
