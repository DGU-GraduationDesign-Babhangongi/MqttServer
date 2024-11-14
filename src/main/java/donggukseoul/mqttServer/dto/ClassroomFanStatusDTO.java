package donggukseoul.mqttServer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomFanStatusDTO {
    private int classroom;
    private String fanStatus;
}
