package donggukseoul.mqttServer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SensorDataWaterDetection extends SensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sensorId;
    private LocalDateTime timestamp;
    private Boolean value;

    public SensorDataWaterDetection(String sensorId, LocalDateTime timestamp, boolean wet) {
        this.sensorId = sensorId;
        this.timestamp = timestamp;
        this.value = wet;
    }
}