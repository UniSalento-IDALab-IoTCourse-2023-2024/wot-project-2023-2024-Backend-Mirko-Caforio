package it.unisalento.iotproject.machineservice.service;

import it.unisalento.iotproject.machineservice.domain.Machine;
import it.unisalento.iotproject.machineservice.domain.MachineStatus;
import it.unisalento.iotproject.machineservice.domain.ManufacturingMachineType;
import it.unisalento.iotproject.machineservice.dto.MachineDTO;
import it.unisalento.iotproject.machineservice.dto.MachineListDTO;
import it.unisalento.iotproject.machineservice.exceptions.MachineStatusUpdateException;
import it.unisalento.iotproject.machineservice.repository.MachineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MachineService {
    private final MongoTemplate mongoTemplate;
    private final MachineRepository machineRepository;

    private final MachineMessageHandler machineMessageHandler;

    private static final Logger LOGGER = LoggerFactory.getLogger(MachineService.class);

    @Autowired
    public MachineService(MongoTemplate mongoTemplate, MachineRepository machineRepository, MachineMessageHandler machineMessageHandler) {
        this.mongoTemplate = mongoTemplate;
        this.machineRepository = machineRepository;
        this.machineMessageHandler = machineMessageHandler;
    }

    public Machine getMachine(MachineDTO machineDTO) {
        Machine machine = new Machine();

        Optional.ofNullable(machineDTO.getId()).ifPresent(machine::setId);
        Optional.ofNullable(machineDTO.getAddressCountry()).ifPresent(machine::setAddressCountry);
        Optional.ofNullable(machineDTO.getAddressLocality()).ifPresent(machine::setAddressLocality);
        Optional.ofNullable(machineDTO.getAddressRegion()).ifPresent(machine::setAddressRegion);
        Optional.ofNullable(machineDTO.getDistrict()).ifPresent(machine::setDistrict);
        Optional.ofNullable(machineDTO.getStreetAddress()).ifPresent(machine::setStreetAddress);
        Optional.ofNullable(machineDTO.getStreetNumber()).ifPresent(machine::setStreetNumber);
        Optional.ofNullable(machineDTO.getAlternateName()).ifPresent(machine::setAlternateName);
        Optional.ofNullable(machineDTO.getBrandName()).ifPresent(machine::setBrandName);
        Optional.ofNullable(machineDTO.getDescription()).ifPresent(machine::setDescription);
        Optional.ofNullable(machineDTO.getManufacturerName()).ifPresent(machine::setManufacturerName);
        Optional.ofNullable(machineDTO.getManufacturingMachineType())
                .map(Enum::name)
                .map(ManufacturingMachineType::valueOf)
                .ifPresent(machine::setManufacturingMachineType);
        Optional.ofNullable(machineDTO.getName()).ifPresent(machine::setName);
        Optional.ofNullable(machineDTO.getProcessDescription()).ifPresent(machine::setProcessDescription);
        Optional.ofNullable(machineDTO.getStandardOperations()).ifPresent(machine::setStandardOperations);
        Optional.ofNullable(machineDTO.getType()).ifPresent(machine::setType);
        Optional.ofNullable(machineDTO.getVersion()).ifPresent(machine::setVersion);
        Optional.ofNullable(machineDTO.getStatus())
                .map(Enum::name)
                .map(MachineStatus::valueOf)
                .ifPresent(machine::setStatus);

        return machine;
    }

    public MachineDTO getMachineDTO(Machine machine) {
        MachineDTO machineDTO = new MachineDTO();

        Optional.ofNullable(machine.getId()).ifPresent(machineDTO::setId);
        Optional.ofNullable(machine.getAddressCountry()).ifPresent(machineDTO::setAddressCountry);
        Optional.ofNullable(machine.getAddressLocality()).ifPresent(machineDTO::setAddressLocality);
        Optional.ofNullable(machine.getAddressRegion()).ifPresent(machineDTO::setAddressRegion);
        Optional.ofNullable(machine.getDistrict()).ifPresent(machineDTO::setDistrict);
        Optional.ofNullable(machine.getStreetAddress()).ifPresent(machineDTO::setStreetAddress);
        Optional.ofNullable(machine.getStreetNumber()).ifPresent(machineDTO::setStreetNumber);
        Optional.ofNullable(machine.getAlternateName()).ifPresent(machineDTO::setAlternateName);
        Optional.ofNullable(machine.getBrandName()).ifPresent(machineDTO::setBrandName);
        Optional.ofNullable(machine.getDescription()).ifPresent(machineDTO::setDescription);
        Optional.ofNullable(machine.getManufacturerName()).ifPresent(machineDTO::setManufacturerName);
        Optional.ofNullable(machine.getManufacturingMachineType())
                .map(Enum::name)
                .map(ManufacturingMachineType::valueOf)
                .ifPresent(machineDTO::setManufacturingMachineType);
        Optional.ofNullable(machine.getName()).ifPresent(machineDTO::setName);
        Optional.ofNullable(machine.getProcessDescription()).ifPresent(machineDTO::setProcessDescription);
        Optional.ofNullable(machine.getStandardOperations()).ifPresent(machineDTO::setStandardOperations);
        Optional.ofNullable(machine.getType()).ifPresent(machineDTO::setType);
        Optional.ofNullable(machine.getVersion()).ifPresent(machineDTO::setVersion);
        Optional.ofNullable(machine.getStatus())
                .map(Enum::name)
                .map(MachineStatus::valueOf)
                .ifPresent(machineDTO::setStatus);

        return machineDTO;
    }

    public MachineListDTO getAllMachines() {
        MachineListDTO machineListDTO = new MachineListDTO();
        List<MachineDTO> machineDTOs = machineRepository.findAll().stream()
                .map(this::getMachineDTO)
                .toList();
        machineListDTO.setMachinesList(machineDTOs);

        return machineListDTO;
    }

    public MachineDTO insertMachine(MachineDTO machineDTO) {
        Machine machine = machineRepository.findByName(machineDTO.getName());

        if (machine != null) {
            return null;
        }

        machine = getMachine(machineDTO);
        machine.setStatus(MachineStatus.INACTIVE);

        machine = machineRepository.save(machine);

        return getMachineDTO(machine);
    }

    public MachineDTO updateMachine(MachineDTO machineDTO) {
        Optional<Machine> machineOptional = machineRepository.findById(machineDTO.getId());

        if (machineOptional.isEmpty()) {
            return null;
        }

        Machine machine = machineOptional.get();

        if (machine.getStatus().equals(MachineStatus.MAINTENANCE) || machine.getStatus().equals(MachineStatus.WORKING)) {
            throw new MachineStatusUpdateException("Cannot update a machine in maintenance or working status.");
        }

        Optional.ofNullable(machineDTO.getAddressCountry()).ifPresent(machine::setAddressCountry);
        Optional.ofNullable(machineDTO.getAddressLocality()).ifPresent(machine::setAddressLocality);
        Optional.ofNullable(machineDTO.getAddressRegion()).ifPresent(machine::setAddressRegion);
        Optional.ofNullable(machineDTO.getDistrict()).ifPresent(machine::setDistrict);
        Optional.ofNullable(machineDTO.getStreetAddress()).ifPresent(machine::setStreetAddress);
        Optional.ofNullable(machineDTO.getStreetNumber()).ifPresent(machine::setStreetNumber);
        Optional.ofNullable(machineDTO.getAlternateName()).ifPresent(machine::setAlternateName);
        Optional.ofNullable(machineDTO.getBrandName()).ifPresent(machine::setBrandName);
        Optional.ofNullable(machineDTO.getDescription()).ifPresent(machine::setDescription);
        Optional.ofNullable(machineDTO.getManufacturerName()).ifPresent(machine::setManufacturerName);
        Optional.ofNullable(machineDTO.getManufacturingMachineType())
                .map(Enum::name)
                .map(ManufacturingMachineType::valueOf)
                .ifPresent(machine::setManufacturingMachineType);
        Optional.ofNullable(machineDTO.getName()).ifPresent(machine::setName);
        Optional.ofNullable(machineDTO.getProcessDescription()).ifPresent(machine::setProcessDescription);
        Optional.ofNullable(machineDTO.getStandardOperations()).ifPresent(machine::setStandardOperations);
        Optional.ofNullable(machineDTO.getType()).ifPresent(machine::setType);
        Optional.ofNullable(machineDTO.getVersion()).ifPresent(machine::setVersion);

        machine = machineRepository.save(machine);

        return getMachineDTO(machine);
    }

    public MachineDTO startMachine(String id) {
        Optional<Machine> machineOptional = machineRepository.findById(id);

        if (machineOptional.isEmpty()) {
            return null;
        }

        Machine machine = machineOptional.get();

        if (machine.getStatus().equals(MachineStatus.INACTIVE)) {
            machine.setStatus(MachineStatus.ACTIVE);
            machine = machineRepository.save(machine);
            return getMachineDTO(machine);
        } else {
            throw new MachineStatusUpdateException("Cannot start a machine that isn't INACTIVE.");
        }
    }

    public MachineDTO stopMachine(String id) {
        Optional<Machine> machineOptional = machineRepository.findById(id);

        if (machineOptional.isEmpty()) {
            return null;
        }

        Machine machine = machineOptional.get();

        if (machine.getStatus().equals(MachineStatus.ACTIVE) ||
                machine.getStatus().equals(MachineStatus.WORKING) ||
                machine.getStatus().equals(MachineStatus.MAINTENANCE)) {

            machine.setStatus(MachineStatus.INACTIVE);
            machine = machineRepository.save(machine);

            machineMessageHandler.edgeRequestStop(machine.getName());

            return getMachineDTO(machine);
        } else {
            throw new MachineStatusUpdateException("Cannot stop a machine that is not active or not working.");
        }
    }

    public MachineDTO maintenanceMachine(String id) {
        Optional<Machine> machineOptional = machineRepository.findById(id);

        if (machineOptional.isEmpty()) {
            return null;
        }

        Machine machine = machineOptional.get();

        if (machine.getStatus().equals(MachineStatus.INACTIVE)) {
            machine.setStatus(MachineStatus.MAINTENANCE);
            machine = machineRepository.save(machine);
            return getMachineDTO(machine);
        } else {
            throw new MachineStatusUpdateException("Cannot put a machine in maintenance if it is not inactive.");
        }
    }

    public MachineDTO workMachine(String id) {
        Optional<Machine> machineOptional = machineRepository.findById(id);

        if (machineOptional.isEmpty()) {
            return null;
        }

        Machine machine = machineOptional.get();

        if (machine.getStatus().equals(MachineStatus.ACTIVE)) {
            machine.setStatus(MachineStatus.WORKING);
            machine = machineRepository.save(machine);

            machineMessageHandler.edgeRequestWork(machine.getName());

            return getMachineDTO(machine);
        } else {
            throw new MachineStatusUpdateException("Cannot put a machine in working status if it is not active.");
        }
    }

    public List<Machine> findMachines(MachineQueryFilters machineQueryFilters) {
        Query query = new Query();

        if (machineQueryFilters.getAddressCountry() != null) {
            query.addCriteria(Criteria.where("addressCountry").is(machineQueryFilters.getAddressCountry()));
        }

        if (machineQueryFilters.getAddressLocality() != null) {
            query.addCriteria(Criteria.where("addressLocality").is(machineQueryFilters.getAddressLocality()));
        }

        if (machineQueryFilters.getAddressRegion() != null) {
            query.addCriteria(Criteria.where("addressRegion").is(machineQueryFilters.getAddressRegion()));
        }

        if (machineQueryFilters.getBrandName() != null) {
            query.addCriteria(Criteria.where("brandName").is(machineQueryFilters.getBrandName()));
        }

        if (machineQueryFilters.getManufacturerName() != null) {
            query.addCriteria(Criteria.where("manufacturerName").is(machineQueryFilters.getManufacturerName()));
        }

        if (machineQueryFilters.getManufacturingMachineType() != null) {
            query.addCriteria(Criteria.where("manufacturingMachineType").is(machineQueryFilters.getManufacturingMachineType()));
        }

        if (machineQueryFilters.getName() != null) {
            query.addCriteria(Criteria.where("name").is(machineQueryFilters.getName()));
        }

        if (machineQueryFilters.getStatus() != null) {
            query.addCriteria(Criteria.where("status").gte(machineQueryFilters.getStatus()));
        }

        LOGGER.info("\n{}\n", query);

        List<Machine> machines = mongoTemplate.find(query, Machine.class, mongoTemplate.getCollectionName(Machine.class));

        LOGGER.info("\nResources: {}\n", machines);

        return machines;
    }
}
