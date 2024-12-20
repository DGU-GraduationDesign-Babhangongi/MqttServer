package donggukseoul.mqttServer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class SensorDataDTO {

    private String sensorId;

    private String building;

    private String name;

    private LocalDateTime timestamp;

    private Double value;

    private String sensorType;

    private String level;
    public SensorDataDTO(String sensorId, String building, String name, LocalDateTime timestamp, double value, String sensorType) {
        this.sensorId = sensorId;
        this.building = building;
        this.name = name;
        this.timestamp = timestamp;
        this.value = value;
        this.sensorType = sensorType;
    }
}
