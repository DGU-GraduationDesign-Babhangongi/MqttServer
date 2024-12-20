package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BuildingRepository extends JpaRepository<Building, Long> {

    Optional<Building> findByName(String name);

    Optional<Building> findBySchool(String school);
}