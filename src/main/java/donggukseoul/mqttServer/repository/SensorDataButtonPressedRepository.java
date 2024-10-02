package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.entity.SensorDataButtonPressed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SensorDataButtonPressedRepository extends JpaRepository<SensorDataButtonPressed, Long> {
    Page<SensorDataButtonPressed> findBySensorId(String sensorId, Pageable pageable);

}