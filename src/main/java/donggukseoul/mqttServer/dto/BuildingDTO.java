// BuildingDTO.java
package donggukseoul.mqttServer.dto;

import lombok.Data;
import java.util.List;

@Data
public class BuildingDTO {
    private Long id;
    private String name;
    private int maxFloor;
    private List<FloorPlanDTO> floorPlans;
}