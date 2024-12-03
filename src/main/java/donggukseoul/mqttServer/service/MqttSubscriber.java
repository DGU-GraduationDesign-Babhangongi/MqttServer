package donggukseoul.mqttServer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import donggukseoul.mqttServer.entity.*;
import donggukseoul.mqttServer.enums.SensorType;
import donggukseoul.mqttServer.repository.*;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class MqttSubscriber {

    private static final Logger logger = LoggerFactory.getLogger(MqttSubscriber.class);
    private static final String BROKER_URL = "tcp://donggukseoul.com:1883";
    private static final String TOPIC_FILTER = "meraki/v1/mt/+/ble/+/#";

    private MqttClient mqttClient;

    @Value("${mqtt.clientId}")
    private String clientId;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        try {
            mqttClient = new MqttClient(BROKER_URL, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);

            mqttClient.connect(options);
            logger.info("MQTT 브로커에 연결 성공: {}", BROKER_URL);

            subscribe();
        } catch (MqttException e) {
            logger.error("MQTT 연결 실패: {}", e.getMessage(), e);
        }
    }

    public void subscribe() throws MqttException {
        mqttClient.subscribe(TOPIC_FILTER, new IMqttMessageListener() {
            @Override
            public void messageArrived(String topic, org.eclipse.paho.client.mqttv3.MqttMessage message) throws Exception {
                String payload = new String(message.getPayload());
                logger.info("메시지 도착 - 토픽: {}, 페이로드: {}", topic, payload);

                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(payload);

                String sensorId = extractSensorIdFromTopic(topic);
                LocalDateTime timestamp = parseTimestamp(jsonNode.get("ts").asText());

                SensorType sensorType = SensorType.fromTopic(topic);
                SensorDataRepository repository = (SensorDataRepository) applicationContext.getBean(sensorType.getRepositoryClass());

                SensorData data = createSensorData(sensorType, sensorId, timestamp, jsonNode);
                repository.save(data);
                logger.info("{} data saved: {}", sensorType, data);
            }
        });
        logger.info("MQTT 구독 시작: {}", TOPIC_FILTER);
    }

    private String extractSensorIdFromTopic(String topic) {
        String[] parts = topic.split("/");
        String sensorId = parts[5];
        logger.info("센서 ID 추출: {}", sensorId);
        return sensorId;
    }

    private LocalDateTime parseTimestamp(String timestampStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.of("Asia/Seoul"));
        Instant instant = Instant.from(formatter.parse(timestampStr));
        return LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Seoul"));
    }

    private SensorData createSensorData(SensorType sensorType, String sensorId, LocalDateTime timestamp, JsonNode jsonNode) {
        switch (sensorType) {
            case TEMPERATURE:
                return new SensorDataTemperature(sensorId, timestamp, jsonNode.get("celsius").asDouble());
            case TVOC:
                return new SensorDataTvoc(sensorId, timestamp, jsonNode.get("tvoc").asDouble());
            case AMBIENTNOISE:
                return new SensorDataAmbientNoise(sensorId, timestamp, jsonNode.get("ambientNoise").asDouble());
            case IAQINDEX:
                return new SensorDataIaqIndex(sensorId, timestamp, jsonNode.get("iaqIndex").asDouble());
            case AQMSCORES:
                return new SensorDataAqmScores(sensorId, timestamp, jsonNode.toString());
            case HUMIDITY:
                return new SensorDataHumidity(sensorId, timestamp, jsonNode.get("humidity").asDouble());
            case USBPOWERED:
                return new SensorDataUsbPowered(sensorId, timestamp, jsonNode.get("usbPowered").asBoolean());
            case BUTTONPRESSED:
                return new SensorDataButtonPressed(sensorId, timestamp, jsonNode.get("buttonPressed").asBoolean());
            case WATERDETECTION:
                return new SensorDataWaterDetection(sensorId, timestamp, jsonNode.get("wet").asBoolean());
            case PM2_5MASSCONCENTRATION:
                return new SensorDataPm2_5MassConcentration(sensorId, timestamp, jsonNode.get("PM2_5MassConcentration").asDouble());
            default:
                throw new IllegalArgumentException("Unknown sensor type: " + sensorType);
        }
    }
}