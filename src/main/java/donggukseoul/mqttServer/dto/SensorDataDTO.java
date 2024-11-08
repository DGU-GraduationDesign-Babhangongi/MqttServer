package donggukseoul.mqttServer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class SensorDataDTO {

    private String sensorId;

    private LocalDateTime timestamp;

    private Double value;

    private String sensorType;
//    public SensorDataDTO(String sensorId, LocalDateTime timestamp, double value, String sensorType) {
//        this.sensorId = sensorId;
//        this.timestamp = timestamp;
//        this.value = value;
//        this.sensorType = sensorType;
//    }
}
