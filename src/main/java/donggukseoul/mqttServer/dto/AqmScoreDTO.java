package donggukseoul.mqttServer.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AqmScoreDTO {
    private String sensorId;
    private LocalDateTime timestamp;
//    private Double value; // 다른 센서 데이터의 실수 값을 저장

    // AQM Scores 관련 상태
    private String temperatureStatus;
    private String humidityStatus;
    private String tvocStatus;
    private String pm25Status;
    private String ambientNoiseStatus;
    private String co2Status;
}
