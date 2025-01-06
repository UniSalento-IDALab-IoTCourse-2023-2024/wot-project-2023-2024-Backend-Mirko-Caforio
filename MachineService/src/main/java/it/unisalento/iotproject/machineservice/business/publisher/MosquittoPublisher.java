package it.unisalento.iotproject.machineservice.business.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public class MosquittoPublisher {
    private final MessageChannel mqttOutboundChannel;
    private final ObjectMapper mapper;

    private static final Logger logger = LoggerFactory.getLogger(MosquittoPublisher.class);

    @Autowired
    public MosquittoPublisher(@Qualifier("mqttOutboundChannel") MessageChannel mqttOutboundChannel) {
        this.mqttOutboundChannel = mqttOutboundChannel;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    public <T> void publish(T payload, String topic) {
        try {
            mqttOutboundChannel.send(
                    MessageBuilder.withPayload(mapper.writeValueAsString(payload))
                            .setHeader("mqtt_topic", topic)
                            .build()
            );
        } catch (Exception e) {
            logger.error("Exception occurred while publishing message to topic: {}", topic, e);
        }
    }
}