package it.unisalento.iotproject.machineservice.repository;

import it.unisalento.iotproject.machineservice.domain.Machine;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MachineRepository extends MongoRepository<Machine, String> {
    Machine findByName(String name);
}
