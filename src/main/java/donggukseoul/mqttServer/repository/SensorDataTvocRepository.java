package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.dto.SensorDataDTO;
import donggukseoul.mqttServer.entity.SensorDataTvoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorDataTvocRepository extends JpaRepository<SensorDataTvoc, Long> {
    Page<SensorDataTvoc> findBySensorId(String sensorId, Pageable pageable);
    SensorDataTvoc findFirstBySensorIdOrderByTimestampDesc(String sensorId);

    @Query("SELECT new donggukseoul.mqttServer.dto.SensorDataDTO(sd.sensorId, c.building, c.name, sd.timestamp, sd.value, 'TVOC') " +
            "FROM SensorDataTvoc sd JOIN Classroom c ON sd.sensorId = c.sensorId WHERE sd.sensorId = :sensorId AND sd.timestamp BETWEEN :startDate AND :endDate")
    List<SensorDataDTO> findAllBySensorIdAndTimestampBetween(
            @Param("sensorId") String sensorId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT new donggukseoul.mqttServer.dto.SensorDataDTO(sd.sensorId, c.building, c.name, sd.timestamp, sd.value, 'TVOC') " +
            "FROM SensorDataTvoc sd JOIN Classroom c ON sd.sensorId = c.sensorId WHERE sd.timestamp > :timestamp")
    List<SensorDataDTO> findAllByTimestampAfter(@Param("timestamp") LocalDateTime timestamp);

}
