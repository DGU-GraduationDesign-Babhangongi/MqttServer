package donggukseoul.mqttServer.dto;

import lombok.Data;

@Data
public class BuildingCreateDTO {
    private String name;
    private int maxFloor;
}