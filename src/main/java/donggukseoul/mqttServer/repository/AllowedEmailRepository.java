package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.entity.AllowedEmail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllowedEmailRepository extends JpaRepository<AllowedEmail, Long> {
    boolean existsByEmail(String email);

    void deleteByEmail(String email);
}