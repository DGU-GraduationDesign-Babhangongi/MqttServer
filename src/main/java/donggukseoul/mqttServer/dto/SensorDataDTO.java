package donggukseoul.mqttServer.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SensorDataDTO {

    private String sensorId;

    private LocalDateTime timestamp;

    private Double value; // 다른 센서 데이터의 실수 값을 저장

}
