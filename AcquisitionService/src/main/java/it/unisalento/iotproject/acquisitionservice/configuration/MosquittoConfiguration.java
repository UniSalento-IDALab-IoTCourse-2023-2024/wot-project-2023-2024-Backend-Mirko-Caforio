package it.unisalento.iotproject.acquisitionservice.configuration;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.util.concurrent.Executors;

@Configuration
public class MosquittoConfiguration {

    @Value("${mqtt.hostname}")
    private String mqttHostname;

    @Value("${mqtt.port}")
    private String mqttPort;

    @Value("${mqtt.clientId}")
    private String clientId;

    @Value("${mqtt.username}")
    private String mqttUsername;

    @Value("${mqtt.password}")
    private String mqttPassword;

    @Value("${mqtt.acquisition.auth.response.topic}")
    private String acquisitionAuthResponseTopic;

    @Value("${mqtt.acquisition.data.save.topic}")
    private String acquisitionDataTopic;

    @Value("${mqtt.acquisition.supervisor.email.response.topic}")
    private String acquisitionSupervisorEmailResponseTopic;

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        String brokerUrl = "tcp://" + mqttHostname + ":" + mqttPort;
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();

        MqttConnectOptions options = new MqttConnectOptions();

        options.setServerURIs(new String[] { brokerUrl });
        options.setUserName(mqttUsername);
        options.setPassword(mqttPassword.toCharArray());
        options.setAutomaticReconnect(true);

        factory.setConnectionOptions(options);

        return factory;
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new ExecutorChannel(Executors.newCachedThreadPool());
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new ExecutorChannel(Executors.newCachedThreadPool());
    }

    @Bean
    public MqttPahoMessageDrivenChannelAdapter inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                clientId,
                mqttClientFactory(),
                acquisitionAuthResponseTopic,
                acquisitionDataTopic,
                acquisitionSupervisorEmailResponseTopic
        );

        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(2);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(MqttClient.generateClientId(), mqttClientFactory());

        messageHandler.setAsync(true);
        messageHandler.setDefaultQos(2);

        return messageHandler;
    }
}