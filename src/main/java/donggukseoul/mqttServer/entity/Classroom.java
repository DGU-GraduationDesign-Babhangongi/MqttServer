package donggukseoul.mqttServer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "classroom")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer floor;
    private String building;
    private String sensorId;

    @ManyToMany
    @JoinTable(
            name = "classroom_favorites",
            joinColumns = @JoinColumn(name = "classroom_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> favoritedByUsers = new HashSet<>();

    // 추가 메서드
    public void addFavoritedByUser(User user) {
        favoritedByUsers.add(user);
        user.getFavoriteClassrooms().add(this);
    }

    public void removeFavoritedByUser(User user) {
        favoritedByUsers.remove(user);
        user.getFavoriteClassrooms().remove(this);
    }
}
