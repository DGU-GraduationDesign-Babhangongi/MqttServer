package donggukseoul.mqttServer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import donggukseoul.mqttServer.entity.SensorData;
import donggukseoul.mqttServer.repository.ClassroomRepository;
import donggukseoul.mqttServer.repository.SensorDataRepository;
import donggukseoul.mqttServer.repository.SensorTypeRepository;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Iterator;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MqttSubscriber {

    private static final Logger logger = LoggerFactory.getLogger(MqttSubscriber.class);
    private static final String BROKER_URL = "tcp://donggukseoul.com:1883";
    private static final String TOPIC_FILTER = "meraki/v1/mt/+/ble/+/#";

    private MqttClient mqttClient;

    @Value("${mqtt.clientId}")
    private String clientId;

    private final SensorTypeRepository sensorTypeRepository;
    private final SensorDataRepository sensorDataRepository;
    private final ClassroomRepository classroomRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        try {
            mqttClient = new MqttClient(BROKER_URL, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);

            mqttClient.connect(options);
            logger.info("MQTT broker connected: {}", BROKER_URL);

            subscribe();
        } catch (MqttException e) {
            logger.error("MQTT connection failed: {}", e.getMessage(), e);
        }
    }

    public void subscribe() throws MqttException {
        mqttClient.subscribe(TOPIC_FILTER, new IMqttMessageListener() {
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                try {
                    String payload = new String(message.getPayload());
                    logger.info("Message received - Topic: {}, Payload: {}", topic, payload);

                    String sensorId = extractSensorIdFromTopic(topic);
                    if (!classroomRepository.existsBySensorId(sensorId)) {
                        logger.debug("Unrecognized sensor ID, ignoring message: {}", sensorId);
                        return;
                    }

                    String sensorType = extractSensorTypeFromTopic(topic);
                    if (!sensorTypeRepository.existsByTypeName(sensorType)) {
                        logger.debug("Unrecognized sensor type, ignoring message: {}", sensorType);
                        return;
                    }


                    LocalDateTime timestamp = parseTimestamp(payload);
                    JsonNode jsonNode = objectMapper.readTree(payload);

                    SensorData data = createSensorData(sensorId, sensorType, timestamp, jsonNode);
                    sensorDataRepository.save(data);

                    logger.info("Data saved for sensor type {}: {}", sensorType, data);
                } catch (Exception e) {
                    logger.error("Error processing message: {}", e.getMessage(), e);
                }
            }
        });
        logger.info("MQTT subscription started for topic filter: {}", TOPIC_FILTER);
    }

    private String extractSensorTypeFromTopic(String topic) {
        String[] parts = topic.split("/");
        return parts[6]; // 센서 타입이 6번째 위치에 있음
    }

    private String extractSensorIdFromTopic(String topic) {
        String[] parts = topic.split("/");
        return parts[5]; // 센서 ID가 5번째 위치에 있음
    }

    private LocalDateTime parseTimestamp(String payload) throws Exception {
        JsonNode rootNode = objectMapper.readTree(payload);
        String timestampStr = rootNode.get("ts").asText();
        Instant instant = Instant.parse(timestampStr);
        return LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Seoul"));
    }

    private SensorData createSensorData(String sensorId, String sensorType, LocalDateTime timestamp, JsonNode jsonNode) {
        SensorData sensorData = new SensorData();
        sensorData.setSensorId(sensorId);
        sensorData.setSensorType(sensorType);
        sensorData.setTimestamp(timestamp);

        ObjectNode extraData = objectMapper.createObjectNode();

        // "ts" 필드는 제외하고 처리
        Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String key = field.getKey();
            JsonNode valueNode = field.getValue();

            if (!key.equals("ts")) {
                if (valueNode.isNumber()) {
                    // 숫자형 값은 value에 저장
                    sensorData.setValue(valueNode.asDouble());
                } else if (valueNode.isBoolean()) {
                    // 불리언 값은 1.0 또는 0.0으로 value에 저장
                    sensorData.setValue(valueNode.asBoolean() ? 1.0 : 0.0);
                } else {
                    // 그 외 값은 extraData에 추가
                    extraData.set(key, valueNode);
                }
            }
        }

        if (!extraData.isEmpty()) {
            sensorData.setValueString(extraData.toString());
        }

        return sensorData;
    }
}
