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
public class SensorDataPm2_5MassConcentration extends SensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sensorId;
    private LocalDateTime timestamp;
    private Double value;

    public SensorDataPm2_5MassConcentration(String sensorId, LocalDateTime timestamp, double pm25MassConcentration) {
        this.sensorId = sensorId;
        this.timestamp = timestamp;
        this.value = pm25MassConcentration;
    }
}