package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.entity.SensorDataAmbientNoise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorDataAmbientNoiseRepository extends JpaRepository<SensorDataAmbientNoise, Long> {
    Page<SensorDataAmbientNoise> findBySensorId(String sensorId, Pageable pageable);
}