package it.unisalento.iotproject.authservice.service;

import it.unisalento.iotproject.authservice.business.publisher.MosquittoPublisher;
import it.unisalento.iotproject.authservice.dto.RegistrationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DataConsistencyService {
    private final MosquittoPublisher mosquittoPublisher;

    @Value("${mqtt.cqrs.consistency.topic}")
    private String cqrsConsistencyTopic;

    @Autowired
    public DataConsistencyService(MosquittoPublisher mosquittoPublisher) {
        this.mosquittoPublisher = mosquittoPublisher;
    }

    public void alertDataConsistency(RegistrationDTO user) {
        mosquittoPublisher.publish(user, cqrsConsistencyTopic);
    }
}
