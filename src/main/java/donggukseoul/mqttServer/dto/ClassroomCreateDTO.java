package donggukseoul.mqttServer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassroomCreateDTO {
    private String name;
    private int floor;
    private String building;
    private String sensorId;
    private String sensorType;
    private double sensorX; // 센서 x 좌표 (소수점 포함)
    private double sensorY; // 센서 y 좌표 (소수점 포함)
}
