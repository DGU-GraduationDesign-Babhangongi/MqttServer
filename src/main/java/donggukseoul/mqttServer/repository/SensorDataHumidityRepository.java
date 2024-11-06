package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.entity.SensorDataButtonPressed;
import donggukseoul.mqttServer.entity.SensorDataHumidity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SensorDataHumidityRepository extends JpaRepository<SensorDataHumidity, Long> {
    Page<SensorDataHumidity> findBySensorId(String sensorId, Pageable pageable);
    SensorDataHumidity findFirstBySensorIdOrderByTimestampDesc(String sensorId);

    Page<?> findBySensorIdAndTimestampBetween(String sensorId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
