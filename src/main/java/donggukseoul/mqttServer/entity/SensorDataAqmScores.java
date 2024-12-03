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
public class SensorDataAqmScores extends SensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sensorId;
    private LocalDateTime timestamp;
    private String aqmScores;

    public SensorDataAqmScores(String sensorId, LocalDateTime timestamp, String string) {
        this.sensorId = sensorId;
        this.timestamp = timestamp;
        this.aqmScores = string;
    }
}