package donggukseoul.mqttServer.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AqmScoreDTO {
    private String sensorId;
    private LocalDateTime timestamp;
    private String temperatureStatus;
    private String humidityStatus;
    private String tvocStatus;
    private String pm25Status;
    private String ambientNoiseStatus;
    private String co2Status;
}
