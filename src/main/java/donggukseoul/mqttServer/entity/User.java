package donggukseoul.mqttServer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String email;

    private String areaOfResponsibility;

//    private String securityCode;

    private boolean alarmStatus;

    private String role;

    @ManyToMany(mappedBy = "favoritedByUsers")
    private Set<Classroom> favoriteClassrooms = new HashSet<>();
}
