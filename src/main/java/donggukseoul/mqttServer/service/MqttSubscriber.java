package donggukseoul.mqttServer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import donggukseoul.mqttServer.entity.*;
import donggukseoul.mqttServer.repository.*;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private SensorDataTemperatureRepository sensorDataTemperatureRepository;
    @Autowired
    private SensorDataTvocRepository sensorDataTvocRepository;
    @Autowired
    private SensorDataAmbientNoiseRepository sensorDataAmbientNoiseRepository;
    @Autowired
    private SensorDataIaqIndexRepository sensorDataIaqIndexRepository;
    @Autowired
    private SensorDataAqmScoresRepository sensorDataAqmScoresRepository;
    @Autowired
    private SensorDataHumidityRepository sensorDataHumidityRepository;
    @Autowired
    private SensorDataUsbPoweredRepository sensorDataUsbPoweredRepository;
    @Autowired
    private SensorDataButtonPressedRepository sensorDataButtonPressedRepository;
    @Autowired
    private SensorDataWaterDetectionRepository sensorDataWaterDetectionRepository;
    @Autowired
    private SensorDataPm2_5MassConcentrationRepository sensorDataPm2_5MassConcentrationRepository;

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

                if (topic.contains("waterDetection")) {
                    saveWaterDetectionData(sensorId, timestamp, jsonNode.get("wet").asBoolean());
                } else if (topic.contains("temperature")) {
                    saveTemperatureData(sensorId, timestamp, jsonNode.get("celsius").asDouble());
                } else if (topic.contains("tvoc")) {
                    saveTvocData(sensorId, timestamp, jsonNode.get("tvoc").asDouble());
                } else if (topic.contains("ambientNoise")) {
                    saveAmbientNoiseData(sensorId, timestamp, jsonNode.get("ambientNoise").asDouble());
                } else if (topic.contains("iaqIndex")) {
                    saveIaqIndexData(sensorId, timestamp, jsonNode.get("iaqIndex").asDouble());
                } else if (topic.contains("aqmScores")) {
                    saveAqmScoresData(sensorId, timestamp, jsonNode.toString());
                } else if (topic.contains("humidity")) {
                    saveHumidityData(sensorId, timestamp, jsonNode.get("humidity").asDouble());
                } else if (topic.contains("usbPowered")) {
                    saveUsbPoweredData(sensorId, timestamp, jsonNode.get("usbPowered").asBoolean());
                } else if (topic.contains("buttonPressed")) {
                    saveButtonPressedData(sensorId, timestamp, jsonNode.get("buttonPressed").asBoolean());
                } else if (topic.contains("PM2_5MassConcentration")) {
                    savePm2_5MassConcentrationData(sensorId, timestamp, jsonNode.get("PM2_5MassConcentration").asDouble());
                } else {
                    logger.warn("Unknown topic: {}", topic);
                }
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

    private void saveWaterDetectionData(String sensorId, LocalDateTime timestamp, boolean value) {
        SensorDataWaterDetection data = new SensorDataWaterDetection();
        data.setSensorId(sensorId);
        data.setTimestamp(timestamp);
        data.setValue(value);
        sensorDataWaterDetectionRepository.save(data);
        logger.info("Water detection data saved: {}", data);
    }

    private void saveTemperatureData(String sensorId, LocalDateTime timestamp, double value) {
        SensorDataTemperature data = new SensorDataTemperature();
        data.setSensorId(sensorId);
        data.setTimestamp(timestamp);
        data.setValue(value);
        sensorDataTemperatureRepository.save(data);
        logger.info("Temperature data saved: {}", data);
    }

    private void saveTvocData(String sensorId, LocalDateTime timestamp, double value) {
        SensorDataTvoc data = new SensorDataTvoc();
        data.setSensorId(sensorId);
        data.setTimestamp(timestamp);
        data.setValue(value);
        sensorDataTvocRepository.save(data);
        logger.info("TVOC data saved: {}", data);
    }

    private void saveAmbientNoiseData(String sensorId, LocalDateTime timestamp, double value) {
        SensorDataAmbientNoise data = new SensorDataAmbientNoise();
        data.setSensorId(sensorId);
        data.setTimestamp(timestamp);
        data.setValue(value);
        sensorDataAmbientNoiseRepository.save(data);
        logger.info("Ambient noise data saved: {}", data);
    }

    private void saveIaqIndexData(String sensorId, LocalDateTime timestamp, double value) {
        SensorDataIaqIndex data = new SensorDataIaqIndex();
        data.setSensorId(sensorId);
        data.setTimestamp(timestamp);
        data.setValue(value);
        sensorDataIaqIndexRepository.save(data);
        logger.info("IAQ index data saved: {}", data);
    }

    private void saveAqmScoresData(String sensorId, LocalDateTime timestamp, String value) {
        SensorDataAqmScores data = new SensorDataAqmScores();
        data.setSensorId(sensorId);
        data.setTimestamp(timestamp);
        data.setAqmScores(value);
        sensorDataAqmScoresRepository.save(data);
        logger.info("AQM scores data saved: {}", data);
    }

    private void saveHumidityData(String sensorId, LocalDateTime timestamp, double value) {
        SensorDataHumidity data = new SensorDataHumidity();
        data.setSensorId(sensorId);
        data.setTimestamp(timestamp);
        data.setValue(value);
        sensorDataHumidityRepository.save(data);
        logger.info("Humidity data saved: {}", data);
    }

    private void saveUsbPoweredData(String sensorId, LocalDateTime timestamp, boolean value) {
        SensorDataUsbPowered data = new SensorDataUsbPowered();
        data.setSensorId(sensorId);
        data.setTimestamp(timestamp);
        data.setValue(value);
        sensorDataUsbPoweredRepository.save(data);
        logger.info("USB powered data saved: {}", data);
    }

    private void saveButtonPressedData(String sensorId, LocalDateTime timestamp, boolean value) {
        SensorDataButtonPressed data = new SensorDataButtonPressed();
        data.setSensorId(sensorId);
        data.setTimestamp(timestamp);
        data.setValue(value);
        sensorDataButtonPressedRepository.save(data);
        logger.info("Button pressed data saved: {}", data);
    }

    private void savePm2_5MassConcentrationData(String sensorId, LocalDateTime timestamp, double value) {
        SensorDataPm2_5MassConcentration data = new SensorDataPm2_5MassConcentration();
        data.setSensorId(sensorId);
        data.setTimestamp(timestamp);
        data.setValue(value);
        sensorDataPm2_5MassConcentrationRepository.save(data);
        logger.info("PM2_5 data saved: {}", data);
    }
}