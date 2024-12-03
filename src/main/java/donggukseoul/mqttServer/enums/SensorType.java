package donggukseoul.mqttServer.enums;

import donggukseoul.mqttServer.repository.*;

public enum SensorType {
    ALL("all", null),

    TEMPERATURE("temperature", SensorDataTemperatureRepository.class),

    TVOC("tvoc", SensorDataTvocRepository.class),
    AMBIENT_NOISE("ambientNoise", SensorDataAmbientNoiseRepository.class),
    IAQ_INDEX("iaqIndex", SensorDataIaqIndexRepository.class),
    AQM_SCORES("aqmScores", SensorDataAqmScoresRepository.class),
    HUMIDITY("humidity", SensorDataHumidityRepository.class),
    USB_POWERED("usbPowered", SensorDataUsbPoweredRepository.class),
    BUTTON_PRESSED("buttonPressed", SensorDataButtonPressedRepository.class),
    WATER_DETECTION("waterDetection", SensorDataWaterDetectionRepository.class),
    PM2_5_MASS_CONCENTRATION("PM2_5MassConcentration", SensorDataPm2_5MassConcentrationRepository.class);

    private final String topic;
    private final Class<? extends SensorDataRepository<?>> repositoryClass;

    SensorType(String topic, Class<? extends SensorDataRepository<?>> repositoryClass) {
        this.topic = topic;
        this.repositoryClass = repositoryClass;
    }

    public String getTopic() {
        return topic;
    }

    public Class<? extends SensorDataRepository<?>> getRepositoryClass() {
        return repositoryClass;
    }

    public static SensorType fromTopic(String topic) {
        for (SensorType type : values()) {
            if (topic.contains(type.getTopic())) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown topic: " + topic);
    }
}