package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.entity.SensorDataWaterDetection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorDataWaterDetectionRepository extends SensorDataRepository<SensorDataWaterDetection> {
    Page<SensorDataWaterDetection> findBySensorId(String sensorId, Pageable pageable);
    SensorDataWaterDetection findFirstBySensorIdOrderByTimestampDesc(String sensorId);

}
