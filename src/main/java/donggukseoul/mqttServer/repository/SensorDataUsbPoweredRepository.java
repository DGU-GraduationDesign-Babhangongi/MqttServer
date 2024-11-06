package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.entity.SensorDataTvoc;
import donggukseoul.mqttServer.entity.SensorDataUsbPowered;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SensorDataUsbPoweredRepository extends JpaRepository<SensorDataUsbPowered, Long> {
    Page<SensorDataUsbPowered> findBySensorId(String sensorId, Pageable pageable);
    SensorDataUsbPowered findFirstBySensorIdOrderByTimestampDesc(String sensorId);

    Page<?> findBySensorIdAndTimestampBetween(String sensorId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}