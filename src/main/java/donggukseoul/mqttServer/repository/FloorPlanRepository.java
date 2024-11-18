package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.entity.FloorPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FloorPlanRepository extends JpaRepository<FloorPlan, Long> {
}