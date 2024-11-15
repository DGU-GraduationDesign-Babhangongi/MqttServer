package donggukseoul.mqttServer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fan_status_logs")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FanStatusLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int classroom; // 강의실 번호
    private String timestamp; // 팬 상태 변경 시간
    private String fanStatus; // "on" 또는 "off"
}
