package donggukseoul.mqttServer.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SensorDataDTO {
    private String sensorId;
    private LocalDateTime timestamp;
    private Double value;
    private String data;
}
