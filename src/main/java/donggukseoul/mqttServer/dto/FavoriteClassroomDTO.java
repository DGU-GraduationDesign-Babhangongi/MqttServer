package donggukseoul.mqttServer.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoriteClassroomDTO {
    private Long id;
    private String name;
    private Integer floor;
    private String building;
    private String sensorId;
    private boolean isFavorited; // 즐겨찾기 여부
}
