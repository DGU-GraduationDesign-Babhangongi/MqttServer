package donggukseoul.mqttServer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassroomCreateDTO {

    private String name;
    private Integer floor;
    private String building;
    private String sensorId;
    private String sensorType;
}
