package donggukseoul.mqttServer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "device")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // Air conditioner, Ventilator, Air cleaner
    private boolean status; // ON/OFF

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;
}