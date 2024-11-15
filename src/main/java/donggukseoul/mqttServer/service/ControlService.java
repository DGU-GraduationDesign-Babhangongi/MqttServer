package donggukseoul.mqttServer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import donggukseoul.mqttServer.dto.ClassroomStatusDTO;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ControlService {

    private final ObjectMapper objectMapper;
    private List<ClassroomStatusDTO> classroomStatuses = new ArrayList<>();

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

            List<ClassroomStatusDTO> newStatuses = new ArrayList<>();
            for (JsonNode dataNode : dataArray) {
                int classroom = dataNode.path("강의실").asInt();
                String time = dataNode.path("시간").asText();
                String fanStatus = dataNode.path("fan").asText();

                // 이상 수치가 있는 경우 해당 항목들의 이름을 리스트로 저장
                List<String> abnormalValues = new ArrayList<>();
                JsonNode abnormalNode = dataNode.path("이상 수치");
                if (abnormalNode.isObject()) {
                    abnormalNode.fieldNames().forEachRemaining(abnormalValues::add);
                }

                ClassroomStatusDTO statusDTO = new ClassroomStatusDTO(classroom, time, fanStatus, abnormalValues);
                newStatuses.add(statusDTO);
            }

            // 업데이트된 상태 리스트로 교체
            classroomStatuses = newStatuses;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ClassroomStatusDTO> getClassroomStatuses() {
        return classroomStatuses;
    }
}
