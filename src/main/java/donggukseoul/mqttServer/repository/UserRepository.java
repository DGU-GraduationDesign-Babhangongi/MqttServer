package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUsername(String username);

    User findByUsername(String username);

}
