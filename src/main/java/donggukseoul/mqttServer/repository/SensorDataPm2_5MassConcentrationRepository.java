package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.entity.SensorDataPm2_5MassConcentration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorDataPm2_5MassConcentrationRepository extends JpaRepository<SensorDataPm2_5MassConcentration, Long> {
    Page<SensorDataPm2_5MassConcentration> findBySensorId(String sensorId, Pageable pageable);

}
