package donggukseoul.mqttServer.entity;

import jakarta.persistence.MappedSuperclass;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class SensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sensorId;
    private LocalDateTime timestamp;

    public SensorData() {
    }

    public SensorData(String sensorId, LocalDateTime timestamp) {
        this.sensorId = sensorId;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}