package donggukseoul.mqttServer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import donggukseoul.mqttServer.entity.FanStatusLog;
import donggukseoul.mqttServer.repository.FanStatusLogRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClassroomDataService {

    private final ObjectMapper objectMapper;
    private final FanStatusLogRepository fanStatusLogRepository;

    @PostConstruct
    public void initializeMqttClient() {
        try {
            MqttClient client = new MqttClient("tcp://donggukseoul.com:1883", MqttClient.generateClientId(), new MemoryPersistence());
            client.connect();
            client.subscribe("sensor/data");

            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    // Handle connection loss
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String payload = new String(message.getPayload());
                    processMqttMessage(payload);
                }

                @Override
                public void deliveryComplete(org.eclipse.paho.client.mqttv3.IMqttDeliveryToken token) {
                    // Handle completion of message delivery
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processMqttMessage(String payload) {
        try {
            JsonNode rootNode = objectMapper.readTree(payload);
            JsonNode dataArray = rootNode.path("data");

            for (JsonNode dataNode : dataArray) {
                int classroom = dataNode.path("강의실").asInt();
                String time = dataNode.path("시간").asText();
                String fanStatus = dataNode.path("fan").asText(); // 팬 상태

                // Save fan status log to DB
                FanStatusLog fanStatusLog = FanStatusLog.builder()
                        .classroom(classroom)
                        .timestamp(time)
                        .fanStatus(fanStatus)
                        .build();

                fanStatusLogRepository.save(fanStatusLog);
                System.out.println("Saved fan status log for classroom " + classroom + ": " + fanStatusLog);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
