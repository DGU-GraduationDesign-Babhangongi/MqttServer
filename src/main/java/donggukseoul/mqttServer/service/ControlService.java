package donggukseoul.mqttServer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import donggukseoul.mqttServer.dto.ClassroomStatusDTO;
import donggukseoul.mqttServer.entity.FanStatusLog;
import donggukseoul.mqttServer.repository.FanStatusLogRepository;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ControlService {

    private final ObjectMapper objectMapper;
    private final FanStatusLogRepository fanStatusLogRepository;
    private final Map<Integer, ClassroomStatusDTO> classroomStatuses = new HashMap<>();

    @PostConstruct
    public void initializeMqttClient() {
        try {
            MqttClient client = new MqttClient("tcp://donggukseoul.com:1883", "control", new MemoryPersistence());
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
                    // Handle completion of message delivery if necessary
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
                String fanStatus = dataNode.path("fan").asText();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                LocalDateTime utcDateTime = LocalDateTime.parse(time, formatter);
                LocalDateTime kstDateTime = utcDateTime.plusHours(9); // UTC + 9시간

                List<String> abnormalValues = new ArrayList<>();
                JsonNode abnormalNode = dataNode.path("이상 수치");
                if (abnormalNode.isObject()) {
                    abnormalNode.fieldNames().forEachRemaining(abnormalValues::add);
                }

                ClassroomStatusDTO existingStatus = classroomStatuses.get(classroom);

                if (existingStatus == null || !existingStatus.getFanStatus().equals(fanStatus)) {
                    FanStatusLog fanStatusLog = FanStatusLog.builder()
                            .classroom(classroom)
                            .timestamp(kstDateTime.toString())
                            .fanStatus(fanStatus)
                            .build();

                    fanStatusLogRepository.save(fanStatusLog);
                    System.out.println("Saved fan status log for classroom " + classroom + ": " + fanStatusLog);
                }

                ClassroomStatusDTO statusDTO = new ClassroomStatusDTO(classroom, time, fanStatus, abnormalValues);
                classroomStatuses.put(classroom, statusDTO);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ClassroomStatusDTO> getClassroomStatuses() {
        return new ArrayList<>(classroomStatuses.values());
    }
}