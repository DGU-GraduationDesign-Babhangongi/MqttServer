package donggukseoul.mqttServer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_installation_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorInstallationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;  // 센서가 설치된 강의실

    private String sensorId;  // 설치된 센서 ID
    private LocalDateTime timestamp;  // 센서 설치/변경 시간
}
