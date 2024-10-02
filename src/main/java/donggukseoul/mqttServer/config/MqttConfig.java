package donggukseoul.mqttServer.config;


import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {

    @Bean
    public MqttClient mqttClient() throws MqttException {
        String brokerUrl = "tcp://donggukseoul.com:1883";
        String clientId = MqttClient.generateClientId();
        MqttClient client = new MqttClient(brokerUrl, clientId);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);

        client.connect(options);
        return client;
    }
}
