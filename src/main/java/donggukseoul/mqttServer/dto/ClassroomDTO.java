package donggukseoul.mqttServer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassroomDTO {

    private Long id;
    private String name;
    private Integer floor;
    private String building;
    private String sensorId;
    private String sensorType;
    private double sensorX; // 센서 x 좌표 (소수점 포함)
    private double sensorY; // 센서 y 좌표 (소수점 포함)
}
