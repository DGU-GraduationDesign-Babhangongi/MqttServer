package donggukseoul.mqttServer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassroomCreateDTO {  // ID 없이 생성할 DTO

    private String name;
    private Integer floor;
    private String building;
    private String sensorId;  // Sensor ID는 nullable
}
