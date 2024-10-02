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
    private String sensorId;  // 센서 ID 추가
}
