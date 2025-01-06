package it.unisalento.iotproject.machineservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.iotproject.machineservice.business.exchanger.MosquittoExchanger;
import it.unisalento.iotproject.machineservice.business.publisher.MosquittoPublisher;
import it.unisalento.iotproject.machineservice.domain.Machine;
import it.unisalento.iotproject.machineservice.domain.MachineStatus;
import it.unisalento.iotproject.machineservice.exceptions.MachineNotFoundException;
import it.unisalento.iotproject.machineservice.repository.MachineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class MachineMessageHandler {
    private final MosquittoExchanger mosquittoExchanger;
    private final MachineRepository machineRepository;
    private final MosquittoPublisher mosquittoPublisher;
    private final ObjectMapper mapper;

    @Value("${mqtt.edge.request.stop.topic}")
    private String edgeRequestStopTopic;

    @Value("${mqtt.edge.request.work.topic}")
    private String edgeRequestWorkTopic;

    private static final Logger LOGGER = LoggerFactory.getLogger(MachineMessageHandler.class);

    @Autowired
    public MachineMessageHandler(MosquittoExchanger mosquittoExchanger, MosquittoPublisher mosquittoPublisher, MachineRepository machineRepository) {
        this.mosquittoExchanger = mosquittoExchanger;
        this.mosquittoPublisher = mosquittoPublisher;
        this.mapper = new ObjectMapper();
        this.machineRepository = machineRepository;
    }

    public void edgeRequestWork(String machineName) {
        mosquittoPublisher.publish(machineName, edgeRequestWorkTopic);
    }

    public void edgeRequestStop(String machineName) {
        mosquittoPublisher.publish(machineName, edgeRequestStopTopic);
    }

    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handleMessage(Message<?> message) {
        LOGGER.info("Received message on topic: {}", message.getHeaders().get("mqtt_receivedTopic", String.class));
        LOGGER.info("Message payload: {}", message.getPayload());

        String topic = message.getHeaders().get("mqtt_receivedTopic", String.class);

        switch (topic) {
            case "machine/auth/response":
                try {
                    mosquittoExchanger.handleAuthResponse(message);
                } catch (Exception e) {
                    LOGGER.error("Exception occurred while unmarshalling message to topic: {}", topic, e);
                }

                break;

            case "machine/request/stop":
                try {
                    handleStopRequest(message);
                } catch (Exception e) {
                    LOGGER.error("Exception occurred while publishing message to topic: {}", topic, e);
                }

                break;
            case null:
                break;
            default:
                LOGGER.info("Received message on unknown topic: {}", topic);
        }
    }

    public void handleStopRequest(Message<?> message) throws Exception {
        String machineName = mapper.readValue(message.getPayload().toString(), String.class);;

        try {
            Machine machine = machineRepository.findByName(machineName);

            if (machine == null) {
                throw new MachineNotFoundException("Machine not found");
            }

            machine.setStatus(MachineStatus.INACTIVE);
            machine = machineRepository.save(machine);

            edgeRequestStop(machine.getName());

        } catch (Exception e) {
            LOGGER.error("Error while processing notification message: {}", e.getMessage());
        }
    }
}
