package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUsername(String username);

    User findByUsername(String username);

//    Optional<User> findOptionalByUsername(String username);

    List<User> findByAreaOfResponsibility(String building);

    boolean existsByEmail(String email);
}
