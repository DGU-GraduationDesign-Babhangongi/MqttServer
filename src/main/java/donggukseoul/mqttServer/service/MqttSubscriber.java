package donggukseoul.mqttServer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import donggukseoul.mqttServer.entity.*;
import donggukseoul.mqttServer.repository.*;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


@Service
public class MqttSubscriber {

    private MqttClient mqttClient;

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
        File pahoFolder = new File("/paho");
        if (!pahoFolder.exists() && !pahoFolder.mkdir()) {
            throw new RuntimeException("Failed to create the /paho folder");
        }
        System.setProperty("java.io.tmpdir", "/paho");
        try {
            // MQTT 브로커 주소와 클라이언트 ID 설정
            String brokerUrl = "tcp://donggukseoul.com:1883"; // 여기에 실제 브로커 주소를 입력하세요
            String clientId = "test"; // 여기에 실제 클라이언트 ID를 입력하세요

            mqttClient = new MqttClient(brokerUrl, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);

            // 브로커에 연결
            mqttClient.connect(options);
            System.out.println("MQTT 브로커에 연결 성공: " + brokerUrl);

            // 구독 시작
            subscribe();
        } catch (MqttException e) {
            e.printStackTrace();
            System.out.println("MQTT 연결 실패: " + e.getMessage());
        }
    }

    public void subscribe() throws MqttException {
        String topicFilter = "meraki/v1/mt/+/ble/+/#";
        mqttClient.subscribe(topicFilter, new IMqttMessageListener() {
            @Override
            public void messageArrived(String topic, org.eclipse.paho.client.mqttv3.MqttMessage message) throws Exception {
                String payload = new String(message.getPayload());
                System.out.println("메시지 도착 - 토픽: " + topic + ", 페이로드: " + payload);

                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(payload);

                String sensorId = extractSensorIdFromTopic(topic);

                String timestampStr = jsonNode.get("ts").asText();
                DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.of("Asia/Seoul"));
                Instant instant = Instant.from(formatter.parse(timestampStr));
                LocalDateTime timestamp = LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Seoul"));
                System.out.println("타임스탬프 파싱 성공: " + timestamp);

                if (topic.contains("waterDetection")) {
                    // Extract water detection data
                    boolean value = jsonNode.get("wet").asBoolean();

                    // Save water detection data
                    SensorDataWaterDetection sensorDataWaterDetection = new SensorDataWaterDetection();
                    sensorDataWaterDetection.setSensorId(sensorId);
                    sensorDataWaterDetection.setTimestamp(timestamp);
                    sensorDataWaterDetection.setValue(value);
                    sensorDataWaterDetectionRepository.save(sensorDataWaterDetection);
                    System.out.println("Water detection data saved: " + sensorDataWaterDetection);
                } else if (topic.contains("temperature")) {
                    // Extract temperature data
                    Double temperatureValue = jsonNode.get("celsius").asDouble();

                    // Save temperature data
                    SensorDataTemperature sensorDataTemperature = new SensorDataTemperature();
                    sensorDataTemperature.setSensorId(sensorId);
                    sensorDataTemperature.setTimestamp(timestamp);
                    sensorDataTemperature.setValue(temperatureValue);
                    sensorDataTemperatureRepository.save(sensorDataTemperature);
                    System.out.println("Temperature data saved: " + sensorDataTemperature);
                } else if (topic.contains("tvoc")) {
                    // Extract TVOC data
                    Double tvocValue = jsonNode.get("tvoc").asDouble();

                    // Save TVOC data
                    SensorDataTvoc sensorDataTvoc = new SensorDataTvoc();
                    sensorDataTvoc.setSensorId(sensorId);
                    sensorDataTvoc.setTimestamp(timestamp);
                    sensorDataTvoc.setValue(tvocValue);
                    sensorDataTvocRepository.save(sensorDataTvoc);
                    System.out.println("TVOC data saved: " + sensorDataTvoc);
                } else if (topic.contains("ambientNoise")) {
                    // Extract ambient noise data
                    Double ambientNoiseValue = jsonNode.get("ambientNoise").asDouble();

                    // Save ambient noise data
                    SensorDataAmbientNoise sensorDataAmbientNoise = new SensorDataAmbientNoise();
                    sensorDataAmbientNoise.setSensorId(sensorId);
                    sensorDataAmbientNoise.setTimestamp(timestamp);
                    sensorDataAmbientNoise.setValue(ambientNoiseValue);
                    sensorDataAmbientNoiseRepository.save(sensorDataAmbientNoise);
                    System.out.println("Ambient noise data saved: " + sensorDataAmbientNoise);
                } else if (topic.contains("iaqIndex")) {
                    // Extract IAQ index data
                    Double iaqIndexValue = jsonNode.get("iaqIndex").asDouble();

                    // Save IAQ index data
                    SensorDataIaqIndex sensorDataIaqIndex = new SensorDataIaqIndex();
                    sensorDataIaqIndex.setSensorId(sensorId);
                    sensorDataIaqIndex.setTimestamp(timestamp);
                    sensorDataIaqIndex.setValue(iaqIndexValue);
                    sensorDataIaqIndexRepository.save(sensorDataIaqIndex);
                    System.out.println("IAQ index data saved: " + sensorDataIaqIndex);
                } else if (topic.contains("aqmScores")) {
                    // Extract AQM scores data
                    String aqmScores = jsonNode.toString();

                    // Save AQM scores data
                    SensorDataAqmScores sensorDataAqmScores = new SensorDataAqmScores();
                    sensorDataAqmScores.setSensorId(sensorId);
                    sensorDataAqmScores.setTimestamp(timestamp);
                    sensorDataAqmScores.setAqmScores(aqmScores);
                    sensorDataAqmScoresRepository.save(sensorDataAqmScores);
                    System.out.println("AQM scores data saved: " + sensorDataAqmScores);
                } else if (topic.contains("humidity")) {
                    // Extract humidity data
                    Double humidityValue = jsonNode.get("humidity").asDouble();

                    // Save humidity data
                    SensorDataHumidity sensorDataHumidity = new SensorDataHumidity();
                    sensorDataHumidity.setSensorId(sensorId);
                    sensorDataHumidity.setTimestamp(timestamp);
                    sensorDataHumidity.setValue(humidityValue);
                    sensorDataHumidityRepository.save(sensorDataHumidity);
                    System.out.println("Humidity data saved: " + sensorDataHumidity);
                } else if (topic.contains("usbPowered")) {
                    // Extract USB powered data
                    Boolean usbPoweredValue = jsonNode.get("usbPowered").asBoolean();

                    // Save USB powered data
                    SensorDataUsbPowered sensorDataUsbPowered = new SensorDataUsbPowered();
                    sensorDataUsbPowered.setSensorId(sensorId);
                    sensorDataUsbPowered.setTimestamp(timestamp);
                    sensorDataUsbPowered.setValue(usbPoweredValue);
                    sensorDataUsbPoweredRepository.save(sensorDataUsbPowered);
                    System.out.println("USB powered data saved: " + sensorDataUsbPowered);
                } else if (topic.contains("buttonPressed")) {
                    // Extract button pressed data
                    Boolean buttonPressedValue = jsonNode.get("buttonPressed").asBoolean();

                    // Save button pressed data
                    SensorDataButtonPressed sensorDataButtonPressed = new SensorDataButtonPressed();
                    sensorDataButtonPressed.setSensorId(sensorId);
                    sensorDataButtonPressed.setTimestamp(timestamp);
                    sensorDataButtonPressed.setValue(buttonPressedValue);
                    sensorDataButtonPressedRepository.save(sensorDataButtonPressed);
                    System.out.println("Button pressed data saved: " + sensorDataButtonPressed);
                } else if (topic.contains("PM2_5MassConcentration")) {
                    // Extract PM2_5 data
                    Double PM2_5Value = jsonNode.get("PM2_5MassConcentration").asDouble();

                    // Save PM2_5 data
                    SensorDataPm2_5MassConcentration sensorDataPm2_5MassConcentration = new SensorDataPm2_5MassConcentration();
                    sensorDataPm2_5MassConcentration.setSensorId(sensorId);
                    sensorDataPm2_5MassConcentration.setTimestamp(timestamp);
                    sensorDataPm2_5MassConcentration.setValue(PM2_5Value);
                    sensorDataPm2_5MassConcentrationRepository.save(sensorDataPm2_5MassConcentration);
                    System.out.println("PM2_5 data saved: " + sensorDataPm2_5MassConcentration);
                } else {
                    System.out.println("Unknown topic: " + topic);
                }
            }
        });
        System.out.println("MQTT 구독 시작: " + topicFilter);
    }

    private String extractSensorIdFromTopic(String topic) {
        String[] parts = topic.split("/");
        String sensorId = parts[5];
        System.out.println("센서 ID 추출: " + sensorId);
        return sensorId;
    }

}
