package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRepository extends JpaRepository<Building, Long> {
}