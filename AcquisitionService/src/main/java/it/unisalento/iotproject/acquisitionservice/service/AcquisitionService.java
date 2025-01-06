package it.unisalento.iotproject.acquisitionservice.service;

import it.unisalento.iotproject.acquisitionservice.domain.MachineVibration;
import it.unisalento.iotproject.acquisitionservice.domain.MachineVibrationQueryFilters;
import it.unisalento.iotproject.acquisitionservice.dto.MachineVibrationDTO;
import it.unisalento.iotproject.acquisitionservice.dto.MachineVibrationListDTO;
import it.unisalento.iotproject.acquisitionservice.repository.MachineVibrationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AcquisitionService {
    private final MachineVibrationRepository machineVibrationRepository;
    private final JdbcTemplate jdbcTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(AcquisitionService.class);

    @Autowired
    public AcquisitionService(MachineVibrationRepository machineVibrationRepository, JdbcTemplate jdbcTemplate) {
        this.machineVibrationRepository = machineVibrationRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public MachineVibration getMachineVibration(MachineVibrationDTO machineVibrationDTO) {
        MachineVibration machineVibration = new MachineVibration();

        Optional.ofNullable(machineVibrationDTO.getMachine_name()).ifPresent(machineVibration::setMachine_name);
        Optional.of(machineVibrationDTO.getMean_x()).ifPresent(machineVibration::setMean_x);
        Optional.of(machineVibrationDTO.getMean_y()).ifPresent(machineVibration::setMean_y);
        Optional.of(machineVibrationDTO.getMean_z()).ifPresent(machineVibration::setMean_z);
        Optional.of(machineVibrationDTO.getRms_x()).ifPresent(machineVibration::setRms_x);
        Optional.of(machineVibrationDTO.getRms_y()).ifPresent(machineVibration::setRms_y);
        Optional.of(machineVibrationDTO.getRms_z()).ifPresent(machineVibration::setRms_z);
        Optional.of(machineVibrationDTO.getMin_x()).ifPresent(machineVibration::setMin_x);
        Optional.of(machineVibrationDTO.getMin_y()).ifPresent(machineVibration::setMin_y);
        Optional.of(machineVibrationDTO.getMin_z()).ifPresent(machineVibration::setMin_z);
        Optional.of(machineVibrationDTO.getMax_x()).ifPresent(machineVibration::setMax_x);
        Optional.of(machineVibrationDTO.getMax_y()).ifPresent(machineVibration::setMax_y);
        Optional.of(machineVibrationDTO.getMax_z()).ifPresent(machineVibration::setMax_z);
        Optional.of(machineVibrationDTO.getStd_x()).ifPresent(machineVibration::setStd_x);
        Optional.of(machineVibrationDTO.getStd_y()).ifPresent(machineVibration::setStd_y);
        Optional.of(machineVibrationDTO.getStd_z()).ifPresent(machineVibration::setStd_z);
        Optional.of(machineVibrationDTO.getKurtosis_x()).ifPresent(machineVibration::setKurtosis_x);
        Optional.of(machineVibrationDTO.getKurtosis_y()).ifPresent(machineVibration::setKurtosis_y);
        Optional.of(machineVibrationDTO.getKurtosis_z()).ifPresent(machineVibration::setKurtosis_z);
        Optional.of(machineVibrationDTO.getSkewness_x()).ifPresent(machineVibration::setSkewness_x);
        Optional.of(machineVibrationDTO.getSkewness_y()).ifPresent(machineVibration::setSkewness_y);
        Optional.of(machineVibrationDTO.getSkewness_z()).ifPresent(machineVibration::setSkewness_z);
        Optional.of(machineVibrationDTO.getPeak_to_peak_x()).ifPresent(machineVibration::setPeak_to_peak_x);
        Optional.of(machineVibrationDTO.getPeak_to_peak_y()).ifPresent(machineVibration::setPeak_to_peak_y);
        Optional.of(machineVibrationDTO.getPeak_to_peak_z()).ifPresent(machineVibration::setPeak_to_peak_z);
        Optional.of(machineVibrationDTO.getEnergy_x()).ifPresent(machineVibration::setEnergy_x);
        Optional.of(machineVibrationDTO.getEnergy_y()).ifPresent(machineVibration::setEnergy_y);
        Optional.of(machineVibrationDTO.getEnergy_z()).ifPresent(machineVibration::setEnergy_z);
        Optional.of(machineVibrationDTO.isPrediction()).ifPresent(machineVibration::setPrediction);
        Optional.ofNullable(machineVibrationDTO.getTimestamp()).ifPresent(machineVibration::setTimestamp);

        return machineVibration;
    }

    public MachineVibrationDTO getMachineVibrationDTO(MachineVibration machineVibration) {
        MachineVibrationDTO machineVibrationDTO = new MachineVibrationDTO();

        Optional.ofNullable(machineVibration.getId()).ifPresent(machineVibrationDTO::setId);
        Optional.ofNullable(machineVibration.getMachine_name()).ifPresent(machineVibrationDTO::setMachine_name);
        Optional.of(machineVibration.getMean_x()).ifPresent(machineVibrationDTO::setMean_x);
        Optional.of(machineVibration.getMean_y()).ifPresent(machineVibrationDTO::setMean_y);
        Optional.of(machineVibration.getMean_z()).ifPresent(machineVibrationDTO::setMean_z);
        Optional.of(machineVibration.getRms_x()).ifPresent(machineVibrationDTO::setRms_x);
        Optional.of(machineVibration.getRms_y()).ifPresent(machineVibrationDTO::setRms_y);
        Optional.of(machineVibration.getRms_z()).ifPresent(machineVibrationDTO::setRms_z);
        Optional.of(machineVibration.getMin_x()).ifPresent(machineVibrationDTO::setMin_x);
        Optional.of(machineVibration.getMin_y()).ifPresent(machineVibrationDTO::setMin_y);
        Optional.of(machineVibration.getMin_z()).ifPresent(machineVibrationDTO::setMin_z);
        Optional.of(machineVibration.getMax_x()).ifPresent(machineVibrationDTO::setMax_x);
        Optional.of(machineVibration.getMax_y()).ifPresent(machineVibrationDTO::setMax_y);
        Optional.of(machineVibration.getMax_z()).ifPresent(machineVibrationDTO::setMax_z);
        Optional.of(machineVibration.getStd_x()).ifPresent(machineVibrationDTO::setStd_x);
        Optional.of(machineVibration.getStd_y()).ifPresent(machineVibrationDTO::setStd_y);
        Optional.of(machineVibration.getStd_z()).ifPresent(machineVibrationDTO::setStd_z);
        Optional.of(machineVibration.getKurtosis_x()).ifPresent(machineVibrationDTO::setKurtosis_x);
        Optional.of(machineVibration.getKurtosis_y()).ifPresent(machineVibrationDTO::setKurtosis_y);
        Optional.of(machineVibration.getKurtosis_z()).ifPresent(machineVibrationDTO::setKurtosis_z);
        Optional.of(machineVibration.getSkewness_x()).ifPresent(machineVibrationDTO::setSkewness_x);
        Optional.of(machineVibration.getSkewness_y()).ifPresent(machineVibrationDTO::setSkewness_y);
        Optional.of(machineVibration.getSkewness_z()).ifPresent(machineVibrationDTO::setSkewness_z);
        Optional.of(machineVibration.getPeak_to_peak_x()).ifPresent(machineVibrationDTO::setPeak_to_peak_x);
        Optional.of(machineVibration.getPeak_to_peak_y()).ifPresent(machineVibrationDTO::setPeak_to_peak_y);
        Optional.of(machineVibration.getPeak_to_peak_z()).ifPresent(machineVibrationDTO::setPeak_to_peak_z);
        Optional.of(machineVibration.getEnergy_x()).ifPresent(machineVibrationDTO::setEnergy_x);
        Optional.of(machineVibration.getEnergy_y()).ifPresent(machineVibrationDTO::setEnergy_y);
        Optional.of(machineVibration.getEnergy_z()).ifPresent(machineVibrationDTO::setEnergy_z);
        Optional.of(machineVibration.isPrediction()).ifPresent(machineVibrationDTO::setPrediction);
        Optional.ofNullable(machineVibration.getTimestamp()).ifPresent(machineVibrationDTO::setTimestamp);

        return machineVibrationDTO;
    }

    public MachineVibrationListDTO getAllMachineVibrations() {
        MachineVibrationListDTO machineVibrationListDTO = new MachineVibrationListDTO();

        Iterable<MachineVibration> machineVibrations = machineVibrationRepository.findAll();

        List<MachineVibrationDTO> machineVibrationDTOList = StreamSupport
                .stream(machineVibrations.spliterator(), false)
                .map(this::getMachineVibrationDTO)
                .collect(Collectors.toList());

        machineVibrationListDTO.setMachineVibrationList(machineVibrationDTOList);

        return machineVibrationListDTO;
    }

    public void saveMachineVibration(MachineVibrationDTO machineVibrationDTO) {
        MachineVibration machineVibration = getMachineVibration(machineVibrationDTO);

        try {
            machineVibrationRepository.save(machineVibration);

            LOGGER.info("MachineVibration saved: {}", machineVibration);
        } catch (Exception e) {
            LOGGER.error("Error while saving MachineVibration: {}", e.getMessage());
        }
    }

    public List<MachineVibration> findMachines(MachineVibrationQueryFilters machineVibrationQueryFilters) {
        StringBuilder sql = new StringBuilder("SELECT * FROM machine_vibration WHERE 1=1");

        if (machineVibrationQueryFilters.getMachineName() != null) {
            sql.append(" AND machine_name = '").append(machineVibrationQueryFilters.getMachineName()).append("'");
        }

        if (machineVibrationQueryFilters.getPrediction() != null) {
            sql.append(" AND prediction = ").append(machineVibrationQueryFilters.getPrediction());
        }

        if (machineVibrationQueryFilters.getFrom() != null) {
            sql.append(" AND timestamp >= '").append(machineVibrationQueryFilters.getFrom()).append("'");
        }

        if (machineVibrationQueryFilters.getTo() != null) {
            sql.append(" AND timestamp <= '").append(machineVibrationQueryFilters.getTo()).append("'");
        }

        LOGGER.info("\n{}\n", sql);

        List<MachineVibration> machines = null;

        try {
            machines = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(MachineVibration.class));
        } catch (Exception e) {
            LOGGER.error("Error while finding MachineVibrations: {}", e.getMessage());
        }

        LOGGER.info("\nMachineVibrations: {}\n", machines);

        return machines;
    }
}