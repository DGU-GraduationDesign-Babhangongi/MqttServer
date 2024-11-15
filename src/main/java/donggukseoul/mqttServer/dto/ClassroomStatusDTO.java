package donggukseoul.mqttServer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomStatusDTO {
    private int classroom;
    private String time;
    private String fanStatus;
    private List<String> abnormalValues;
}
