package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.entity.SensorDataPm2_5MassConcentration;
import donggukseoul.mqttServer.entity.SensorDataTvoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SensorDataTvocRepository extends JpaRepository<SensorDataTvoc, Long> {
    Page<SensorDataTvoc> findBySensorId(String sensorId, Pageable pageable);
    SensorDataTvoc findFirstBySensorIdOrderByTimestampDesc(String sensorId);

    Page<?> findBySensorIdAndTimestampBetween(String sensorId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}