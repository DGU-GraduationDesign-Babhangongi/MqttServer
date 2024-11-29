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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
                String utcTime = dataNode.path("시간").asText();
                String fanStatus = dataNode.path("fan").asText(); // 팬 상태


                // Parse UTC time and convert to KST
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                LocalDateTime utcDateTime = LocalDateTime.parse(utcTime, formatter);
                LocalDateTime kstDateTime = utcDateTime.plusHours(9); // UTC + 9시간

                // Save fan status log to DB
                FanStatusLog fanStatusLog = FanStatusLog.builder()
                        .classroom(classroom)
                        .timestamp(kstDateTime.toString())
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
