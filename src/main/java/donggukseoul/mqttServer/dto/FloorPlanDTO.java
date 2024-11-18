// FloorPlanDTO.java
package donggukseoul.mqttServer.dto;

import lombok.Data;

@Data
public class FloorPlanDTO {
    private Long id;
    private int floor;
    private String imageUrl;
}