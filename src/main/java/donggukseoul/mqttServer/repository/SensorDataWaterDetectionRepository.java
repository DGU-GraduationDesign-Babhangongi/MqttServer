package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.entity.SensorDataUsbPowered;
import donggukseoul.mqttServer.entity.SensorDataWaterDetection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface SensorDataWaterDetectionRepository extends JpaRepository<SensorDataWaterDetection, Long> {
    Page<SensorDataWaterDetection> findBySensorId(String sensorId, Pageable pageable);
    SensorDataWaterDetection findFirstBySensorIdOrderByTimestampDesc(String sensorId);

    Page<?> findBySensorIdAndTimestampBetween(String sensorId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}