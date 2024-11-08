package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.dto.SensorDataDTO;
import donggukseoul.mqttServer.entity.SensorDataAmbientNoise;
import donggukseoul.mqttServer.entity.SensorDataAqmScores;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorDataAqmScoresRepository extends JpaRepository<SensorDataAqmScores, Long> {
    Page<SensorDataAqmScores> findBySensorId(String sensorId, Pageable pageable);
    SensorDataAqmScores findFirstBySensorIdOrderByTimestampDesc(String sensorId);
    Page<?> findBySensorIdAndTimestampBetween(String sensorId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

//    List<SensorDataDTO> findBySensorIdAndTimestampBetween(String sensorId, LocalDateTime startDate, LocalDateTime endDate);
}