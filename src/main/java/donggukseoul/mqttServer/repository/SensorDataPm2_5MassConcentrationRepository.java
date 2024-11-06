package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.entity.SensorDataIaqIndex;
import donggukseoul.mqttServer.entity.SensorDataPm2_5MassConcentration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SensorDataPm2_5MassConcentrationRepository extends JpaRepository<SensorDataPm2_5MassConcentration, Long> {
    Page<SensorDataPm2_5MassConcentration> findBySensorId(String sensorId, Pageable pageable);
    SensorDataPm2_5MassConcentration findFirstBySensorIdOrderByTimestampDesc(String sensorId);

    Page<?> findBySensorIdAndTimestampBetween(String sensorId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
