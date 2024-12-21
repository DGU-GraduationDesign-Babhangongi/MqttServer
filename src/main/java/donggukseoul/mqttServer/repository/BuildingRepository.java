package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BuildingRepository extends JpaRepository<Building, Long> {

    Optional<Building> findByName(String name);

    List<Building> findBySchool(String school);
}