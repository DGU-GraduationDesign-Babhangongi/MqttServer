package donggukseoul.mqttServer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int maxFloor;
    private String imageUrl; // 건물 이미지 링크 추가
    private String school; // 학교 정보 추가


    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FloorPlan> floorPlans;
}