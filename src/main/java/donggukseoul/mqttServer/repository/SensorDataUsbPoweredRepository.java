package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.dto.SensorDataDTO;
import donggukseoul.mqttServer.entity.SensorDataUsbPowered;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorDataUsbPoweredRepository extends JpaRepository<SensorDataUsbPowered, Long> {
    Page<SensorDataUsbPowered> findBySensorId(String sensorId, Pageable pageable);
    SensorDataUsbPowered findFirstBySensorIdOrderByTimestampDesc(String sensorId);

//    @Query("SELECT new donggukseoul.mqttServer.dto.SensorDataDTO(sd.sensorId, sd.timestamp, CASE WHEN sd.value = true THEN 1.0 ELSE 0.0 END, 'UsbPowered') " +
//            "FROM SensorDataUsbPowered sd WHERE sd.sensorId = :sensorId AND sd.timestamp BETWEEN :startDate AND :endDate")
//    List<SensorDataDTO> findAllBySensorIdAndTimestampBetween(
//            @Param("sensorId") String sensorId,
//            @Param("startDate") LocalDateTime startDate,
//            @Param("endDate") LocalDateTime endDate);

    Page<?> findBySensorIdAndTimestampBetween(String sensorId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

}
