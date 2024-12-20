package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {
}
