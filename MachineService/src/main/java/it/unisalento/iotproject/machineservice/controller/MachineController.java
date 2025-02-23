package it.unisalento.iotproject.machineservice.controller;

import it.unisalento.iotproject.machineservice.domain.Machine;
import it.unisalento.iotproject.machineservice.dto.MachineDTO;
import it.unisalento.iotproject.machineservice.dto.MachineListDTO;
import it.unisalento.iotproject.machineservice.exceptions.ExistingMachineException;
import it.unisalento.iotproject.machineservice.exceptions.MachineNotFoundException;
import it.unisalento.iotproject.machineservice.service.MachineQueryFilters;
import it.unisalento.iotproject.machineservice.service.MachineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static it.unisalento.iotproject.machineservice.security.SecurityConstants.ROLE_ADMIN;
import static it.unisalento.iotproject.machineservice.security.SecurityConstants.ROLE_SUPERVISOR;

@RestController
@RequestMapping("/api/machine")
public class MachineController {
    private final MachineService machineService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MachineController.class);

    @Autowired
    public MachineController(MachineService machineService) {
        this.machineService = machineService;
    }

    @GetMapping("/find/all")
    @Secured({ROLE_ADMIN, ROLE_SUPERVISOR})
    public MachineListDTO getAllMachines() {
        return machineService.getAllMachines();
    }

    @GetMapping("/find")
    @Secured({ROLE_ADMIN, ROLE_SUPERVISOR})
    public MachineListDTO getMachineByFilter(@ModelAttribute MachineQueryFilters filters) {
        MachineListDTO machineListDTO = new MachineListDTO();
        List<MachineDTO> list = new ArrayList<>();
        machineListDTO.setMachinesList(list);

        List<Machine> machines = machineService.findMachines(filters);

        for (Machine machine : machines) {
            list.add(machineService.getMachineDTO(machine));
        }

        return machineListDTO;
    }

    @PostMapping(value = "/insert", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ROLE_ADMIN, ROLE_SUPERVISOR})
    public MachineDTO insertMachine(@RequestBody MachineDTO machineDTO) {
        MachineDTO existingMachine = machineService.insertMachine(machineDTO);

        if (existingMachine == null) {
            throw new ExistingMachineException("Machine already exists");
        }

        return existingMachine;
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ROLE_ADMIN, ROLE_SUPERVISOR})
    public MachineDTO updateMachine(@RequestBody MachineDTO machineDTO) {
        MachineDTO updatedMachine = machineService.updateMachine(machineDTO);

        if (updatedMachine == null) {
            throw new MachineNotFoundException("Machine not found");
        }

        return updatedMachine;
    }

    @PutMapping(value = "/start/{id}")
    @Secured({ROLE_ADMIN, ROLE_SUPERVISOR})
    public MachineDTO startMachine(@PathVariable String id) {
        MachineDTO machineDTO = machineService.startMachine(id);

        if (machineDTO == null) {
            throw new MachineNotFoundException("Machine not found");
        }

        return machineDTO;
    }

    @PutMapping(value = "/stop/{id}")
    @Secured({ROLE_ADMIN, ROLE_SUPERVISOR})
    public MachineDTO stopMachine(@PathVariable String id) {
        MachineDTO machineDTO = machineService.stopMachine(id);

        if (machineDTO == null) {
            throw new MachineNotFoundException("Machine not found");
        }

        return machineDTO;
    }

    @PutMapping(value = "/work/{id}")
    @Secured({ROLE_ADMIN, ROLE_SUPERVISOR})
    public MachineDTO workMachine(@PathVariable String id) {
        MachineDTO machineDTO = machineService.workMachine(id);

        if (machineDTO == null) {
            throw new MachineNotFoundException("Machine not found");
        }

        return machineDTO;
    }

    @PutMapping(value = "/maintenance/{id}")
    @Secured({ROLE_ADMIN, ROLE_SUPERVISOR})
    public MachineDTO maintenanceMachine(@PathVariable String id) {
        MachineDTO machineDTO = machineService.maintenanceMachine(id);

        if (machineDTO == null) {
            throw new MachineNotFoundException("Machine not found");
        }

        return machineDTO;
    }
}
