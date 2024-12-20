package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.entity.FloorPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FloorPlanRepository extends JpaRepository<FloorPlan, Long> {
    Optional<FloorPlan> findByBuilding_IdAndFloor(Long buildingId, int floor);
}
