package donggukseoul.mqttServer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sensorId;

    private String sensorType; // 센서 타입 (예: "temperature", "humidity" 등)

    private Double value;

    private String valueString; // 문자열 값

    private LocalDateTime timestamp;

//    // 새로운 생성자 추가
//    public SensorData(String sensorId, String sensorType, LocalDateTime timestamp, String value) {
//        this.sensorId = sensorId;
//        this.sensorType = sensorType;
//        this.timestamp = timestamp;
//        this.value = Double.valueOf(value);
//    }

    // 센서 타입별 임계값 반환 메서드
    public double getThreshold() {
        switch (sensorType.toLowerCase()) {
            case "temperature":
                return 27.5; // 예제 임계값, 필요에 따라 수정
            case "humidity":
                return 90.0;
            case "tvoc":
                return 10000.0;
            case "pm2.5":
                return 64.0;
            case "ambientnoise":
                return 80.0;
            default:
                throw new IllegalArgumentException("Unknown sensor type: " + sensorType);
        }
    }
}
