package donggukseoul.mqttServer.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BuildingDetailDTO {
    private Long id;
    private String name;
    private int maxFloor;
    private String imageUrl; // 건물 이미지 링크
    private List<String> floorPlanImages; // 층별 이미지 URL 리스트
    private int sensorCount; // 등록된 센서 개수
}
