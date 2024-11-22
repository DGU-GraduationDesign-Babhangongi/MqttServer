package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.dto.SensorDataDTO;
import donggukseoul.mqttServer.entity.SensorDataHumidity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorDataHumidityRepository extends JpaRepository<SensorDataHumidity, Long> {
    Page<SensorDataHumidity> findBySensorId(String sensorId, Pageable pageable);
    SensorDataHumidity findFirstBySensorIdOrderByTimestampDesc(String sensorId);

    @Query("SELECT new donggukseoul.mqttServer.dto.SensorDataDTO(sd.sensorId, c.building, c.name, sd.timestamp, sd.value, 'Humidity') " +
            "FROM SensorDataHumidity sd JOIN Classroom c ON sd.sensorId = c.sensorId WHERE sd.sensorId = :sensorId AND sd.timestamp BETWEEN :startDate AND :endDate")
    List<SensorDataDTO> findAllBySensorIdAndTimestampBetween(
            @Param("sensorId") String sensorId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT new donggukseoul.mqttServer.dto.SensorDataDTO(sd.sensorId, c.building, c.name, sd.timestamp, sd.value, 'Humidity') " +
            "FROM SensorDataHumidity sd JOIN Classroom c ON sd.sensorId = c.sensorId WHERE sd.timestamp > :timestamp")
    List<SensorDataDTO> findAllByTimestampAfter(@Param("timestamp") LocalDateTime timestamp);

}
