package donggukseoul.mqttServer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FloorPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int floor;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "building_id")
    private Building building;
}