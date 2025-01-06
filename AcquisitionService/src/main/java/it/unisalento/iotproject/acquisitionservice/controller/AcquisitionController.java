package it.unisalento.iotproject.acquisitionservice.controller;

import it.unisalento.iotproject.acquisitionservice.domain.MachineVibration;
import it.unisalento.iotproject.acquisitionservice.dto.MachineVibrationDTO;
import it.unisalento.iotproject.acquisitionservice.dto.MachineVibrationListDTO;
import it.unisalento.iotproject.acquisitionservice.service.AcquisitionService;
import it.unisalento.iotproject.acquisitionservice.domain.MachineVibrationQueryFilters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static it.unisalento.iotproject.acquisitionservice.security.SecurityConstants.ROLE_ADMIN;

@RestController
@RequestMapping("/api/acquisition")
public class AcquisitionController {
    private final AcquisitionService acquisitionService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AcquisitionController.class);

    @Autowired
    public AcquisitionController(AcquisitionService acquisitionService) {
        this.acquisitionService = acquisitionService;
    }

    @GetMapping("/find/all")
    @Secured({ROLE_ADMIN})
    public MachineVibrationListDTO getAllMachinesVibrations() {
        return acquisitionService.getAllMachineVibrations();
    }

    @GetMapping("/find")
    @Secured({ROLE_ADMIN})
    public MachineVibrationListDTO getMachineVibrationsByFilter(@ModelAttribute MachineVibrationQueryFilters filters) {
        MachineVibrationListDTO machineVibrationListDTO = new MachineVibrationListDTO();
        List<MachineVibrationDTO> list = new ArrayList<>();
        machineVibrationListDTO.setMachineVibrationList(list);

        List<MachineVibration> machineVibrations = acquisitionService.findMachines(filters);

        for (MachineVibration machineVibration : machineVibrations) {
            list.add(acquisitionService.getMachineVibrationDTO(machineVibration));
        }

        return machineVibrationListDTO;
    }
}
