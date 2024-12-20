package donggukseoul.mqttServer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuildingCreateDTO {
    private String name;
    private int maxFloor;
}
