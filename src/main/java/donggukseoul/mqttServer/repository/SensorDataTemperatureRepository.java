package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.entity.SensorDataTemperature;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SensorDataTemperatureRepository extends JpaRepository<SensorDataTemperature, Long> {
    Page<SensorDataTemperature> findBySensorId(String sensorId, Pageable pageable);
    SensorDataTemperature findFirstBySensorIdOrderByTimestampDesc(String sensorId);

    Page<?> findBySensorIdAndTimestampBetween(String sensorId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
