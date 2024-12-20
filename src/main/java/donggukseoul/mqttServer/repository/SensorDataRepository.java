package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.entity.SensorData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SensorDataRepository extends JpaRepository<SensorData, Long> {

    Page<SensorData> findBySensorType(String type, Pageable pageable);

    Page<SensorData> findBySensorIdAndSensorType(String sensorId, String type, Pageable pageable);

    List<SensorData> findTop10BySensorIdOrderByTimestampDesc(String sensorId);

    List<SensorData> findByTimestampAfter(LocalDateTime timestamp);
    Page<SensorData> findBySensorIdAndSensorTypeInAndTimestampBetween(
            String sensorId, List<String> sensorTypes, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

}
