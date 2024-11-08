package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.dto.SensorDataDTO;
import donggukseoul.mqttServer.entity.SensorDataPm2_5MassConcentration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorDataPm2_5MassConcentrationRepository extends JpaRepository<SensorDataPm2_5MassConcentration, Long> {

    Page<SensorDataPm2_5MassConcentration> findBySensorId(String sensorId, Pageable pageable);

    SensorDataPm2_5MassConcentration findFirstBySensorIdOrderByTimestampDesc(String sensorId);

    @Query("SELECT new donggukseoul.mqttServer.dto.SensorDataDTO(sd.sensorId, sd.timestamp, sd.value, 'PM2_5MassConcentration') " +
            "FROM SensorDataPm2_5MassConcentration sd WHERE sd.sensorId = :sensorId AND sd.timestamp BETWEEN :startDate AND :endDate")
    List<SensorDataDTO> findAllBySensorIdAndTimestampBetween(
            @Param("sensorId") String sensorId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    Page<?> findBySensorIdAndTimestampBetween(String sensorId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

}
