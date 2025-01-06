package it.unisalento.iotproject.acquisitionservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.iotproject.acquisitionservice.business.exchanger.MosquittoExchanger;
import it.unisalento.iotproject.acquisitionservice.business.publisher.MosquittoPublisher;
import it.unisalento.iotproject.acquisitionservice.dto.MachineVibrationDTO;
import it.unisalento.iotproject.acquisitionservice.dto.NotificationMessageDTO;
import it.unisalento.iotproject.acquisitionservice.dto.SupervisorEmailListDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import static it.unisalento.iotproject.acquisitionservice.service.NotificationConstants.WARNING_NOTIFICATION_TYPE;

@Service
public class AcquisitionMessageHandler {
    private final AcquisitionService acquisitionService;
    private final MosquittoPublisher mosquittoPublisher;
    private final MosquittoExchanger mosquittoExchanger;
    private final NotificationMessageService notificationMessageService;
    private final ObjectMapper mapper;

    @Value("${mqtt.cqrs.supervisor.email.request.topic}")
    private String cqrsSupervisorEmailRequestTopic;

    @Value("${mqtt.acquisition.supervisor.email.response.topic}")
    private String acquisitionSupervisorEmailResponseTopic;

    @Value("${mqtt.machine.request.stop.topic}")
    private String machineRequestStopTopic;

    private static final Logger LOGGER = LoggerFactory.getLogger(AcquisitionMessageHandler.class);

    @Autowired
    public AcquisitionMessageHandler(MosquittoPublisher mosquittoPublisher, NotificationMessageService notificationMessageService, AcquisitionService acquisitionService, MosquittoExchanger mosquittoExchanger) {
        this.mosquittoPublisher = mosquittoPublisher;
        this.notificationMessageService = notificationMessageService;
        this.mapper = new ObjectMapper();
        this.acquisitionService = acquisitionService;
        this.mosquittoExchanger = mosquittoExchanger;
    }

    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handleMessage(Message<?> message) {
        LOGGER.info("Received message on topic: {}", message.getHeaders().get("mqtt_receivedTopic", String.class));
        LOGGER.info("Message payload: {}", message.getPayload());

        String topic = message.getHeaders().get("mqtt_receivedTopic", String.class);

        switch (topic) {
            case "acquisition/data/save":
                try {
                    handleDataSave(message);
                } catch (Exception e) {
                    LOGGER.error("Exception occurred while publishing message to topic: {}", topic, e);
                }

                break;
            case "acquisition/auth/response":
                try {
                    mosquittoExchanger.handleAuthResponse(message);
                } catch (Exception e) {
                    LOGGER.error("Exception occurred while publishing message to topic: {}", topic, e);
                }

                break;
            case "acquisition/supervisor/email/response":
                try {
                    mosquittoExchanger.handleSupervisorEmailResponse(message);
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

    public void handleDataSave(Message<?> message) throws Exception {
        MachineVibrationDTO machineVibrationDTO = mapper.readValue(message.getPayload().toString(), MachineVibrationDTO.class);

        try {
            acquisitionService.saveMachineVibration(machineVibrationDTO);

            if (machineVibrationDTO.isPrediction()) {
                LOGGER.info("POSSIBLE ANOMALY DETECTED");

                mosquittoPublisher.publish(machineVibrationDTO.getMachine_name(), machineRequestStopTopic);

                LOGGER.info("PUBLISHED MACHINE STOP REQUEST: {}", machineVibrationDTO.getMachine_name());

                LOGGER.info("FINDING SUPERVISOR'S EMAILS: {}", machineVibrationDTO.getMachine_name());
                SupervisorEmailListDTO supervisorEmailListDTO = new SupervisorEmailListDTO();

                try {
                    LOGGER.info("STARTING EXCHANGE WITH CQRS");

                    supervisorEmailListDTO = mosquittoExchanger.exchangeMessage(
                            cqrsSupervisorEmailRequestTopic,
                            acquisitionSupervisorEmailResponseTopic,
                            5000
                    );

                } catch (Exception e) {
                    LOGGER.error("Error while exchanging message with CQRS: {}", e.getMessage());
                }

                for (String email : supervisorEmailListDTO.getSupervisorEmailList()) {
                    notificationMessageService.sendNotificationMessage(
                            NotificationMessageDTO.builder()
                                    .message("Possible anomaly detected on machine " + machineVibrationDTO.getMachine_name() + ". Please check the real machine status. For safety reasons, the machine has been stopped.")
                                    .receiver(email)
                                    .subject("Possible machine anomaly detected")
                                    .type(WARNING_NOTIFICATION_TYPE)
                                    .email(true)
                                    .notification(true)
                                    .build()
                    );
                }

                LOGGER.info("EMAILS SENT");
            }
        } catch (Exception e) {
            LOGGER.error("Error while processing notification message: {}", e.getMessage());
        }
    }
}
